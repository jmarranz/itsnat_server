/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2011 Jose Maria Arranz Santamaria, Spanish citizen

  This software is free software; you can redistribute it and/or modify it
  under the terms of the GNU Lesser General Public License as
  published by the Free Software Foundation; either version 3 of
  the License, or (at your option) any later version.
  This software is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details. You should have received
  a copy of the GNU Lesser General Public License along with this program.
  If not, see <http://www.gnu.org/licenses/>.
*/
package org.itsnat.impl.core.servlet.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.servlet.DeserialPendingTask;
import org.itsnat.impl.core.servlet.ItsNatServletContextImpl;
import org.itsnat.impl.core.servlet.ItsNatServletImpl;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.servlet.ItsNatServletResponseImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionSerializeContainerImpl;
import org.itsnat.impl.core.util.MapListImpl;

/**
 *
 * @author jmarranz
 */
public class ItsNatHttpSessionReplicationCapableImpl extends ItsNatHttpSessionImpl
{
    public static final String SESSION_ATTRIBUTE_NAME = "itsnat_session";
    public static final String SESSION_ATTRIBUTE_NAME_FRAGMENT_COUNT = "itsnat_session_fragment_count";
    public static final String SESSION_ATTRIBUTE_NAME_FRAGMENT_NUM = "itsnat_session_fragment_";

    protected ItsNatHttpSessionReplicationCapableImpl(HttpSession session,ItsNatServletContextImpl context,Browser browser)
    {
        super(session,context,browser);

        this.serialContainer = new ItsNatSessionSerializeContainerImpl(this);  // Por ahora la serialización sólo tiene sentido en este contexto (http session en modo replication capable)

        // Es el caso del comienzo del primer request, el atributo de estado probablemente
        // no haya sido definido (caso de un nodo).
        // Yo creo que no es necesario pues se hace también al final del request
        // De todas maneras no hay problema, la aplicación no se ha usado por parte
        // del usuario y estará vacía la sesión
        writeItsNatHttpSessionToAttribute();
    }


    protected void writeItsNatHttpSessionToAttribute()
    {
        // http://blog.stringbuffer.com/2009/04/http-sessions-and-google-app-enginej.html
        try
        {
            synchronized(session) // Si requiere serialización no está mal sincronizar
            {
                if (context.isSessionExplicitSerialize())
                {
                    long size = context.getSessionExplicitSerializeFragmentSize(); // Se supone que no cambia, devuelve siempre el mismo valor

                    // Al deserializar se obtiene siempre una instancia nueva
                    byte[] stream = serializeSession(serialContainer);

                    if (size == ItsNatServletContextImpl.SESSION_EXPLICIT_SERIALIZE_ONE_FRAGMENT)
                    {
                        session.setAttribute(SESSION_ATTRIBUTE_NAME,stream);
                    }
                    else
                    {

                        // Dividimos en trozos de tamaño size (> 0)
                        // size es long por si acaso pero por ahora el máximo valor que
                        // serializamos es del tamaño int de acuerdo con el tipo de datos del length
                        // del array
                        int sizeInt = (int)size;
                        int numFrag = stream.length / sizeInt; // Recuerda que es división entera
                        int modulo = (stream.length % sizeInt);
                        if (modulo != 0) numFrag++; // Uno más
                        // Llegados aquí numFrag nunca será cero porque nunca el array estará vacío (length == 0)

                        // Antes limpiamos los atributos actuales excedentes para que no queden trozos perdidos
                        Integer numFragObjOld = (Integer)session.getAttribute(SESSION_ATTRIBUTE_NAME_FRAGMENT_COUNT);
                        if (numFragObjOld != null)
                        {
                            // Eliminamos sólo los excedentes pues con los demás siempre puede GAE
                            // evitar su transmisión si detecta que el contenido es el mismo
                            int numFragOld = numFragObjOld.intValue();
                            for(int i = numFrag; i < numFragOld; i++)
                                session.removeAttribute(SESSION_ATTRIBUTE_NAME_FRAGMENT_NUM + i);
                        }

                        session.setAttribute(SESSION_ATTRIBUTE_NAME_FRAGMENT_COUNT,new Integer(numFrag));
                        for(int i = 0; i < numFrag; i++)
                        {
                            int sizeFrag = (i != numFrag - 1) ? sizeInt : modulo;
                            byte[] fragment = new byte[sizeFrag];
                            System.arraycopy(stream, i * sizeInt, fragment, 0, sizeFrag);
                            session.setAttribute(SESSION_ATTRIBUTE_NAME_FRAGMENT_NUM + i,fragment);
                        }
                    }
                }
                else  // No serialización explícita
                {
                    session.setAttribute(SESSION_ATTRIBUTE_NAME,serialContainer);
                }
            }
        }
        catch(Exception ex)
        {
            // Evitamos guardar en la sesión (si el error ocurrió al serializar)
            ItsNatSessionSerializeContainerImpl.showError(ex,false);
        }
    }

    public void endOfRequestBeforeSendCode()
    {
        // No se usa, antes se usaba (erróneamente) en la simulación de serialización
        // eliminar en el futuro, lo dejamos por si acaso
    }

    public void endOfRequest()
    {
        // Es el caso por ejemplo de GAE, se supone que GAE "serializa" las
        // requests incluso entre JVMs

        // De esta manera notificamos a la sesión nativa que serialice
        // de nuevo la sesión, por ejemplo en GAE, pues GAE sólo serializa
        // cuando se actualiza via setAttribute de acuerdo con un uso interno de HttpSessionBindingListener
        // http://groups.google.com/group/google-appengine/browse_thread/thread/94b6e2b38ddfe59

        writeItsNatHttpSessionToAttribute();
    }

    protected static ItsNatSessionSerializeContainerImpl readItsNatSessionSerializeContainerFromSessionAttribute(HttpSession session,ItsNatServletContextImpl context)
    {
        // Puede devolver null, no se ha guardado todavía en el atributo (nueva sesión)

        try
        {
            if (context.isSessionExplicitSerialize())
            {
                long size = context.getSessionExplicitSerializeFragmentSize(); // Se supone que no cambia, devuelve siempre el mismo valor

                byte[] stream;
                if (size == ItsNatServletContextImpl.SESSION_EXPLICIT_SERIALIZE_ONE_FRAGMENT)
                {
                    stream = (byte[])session.getAttribute(SESSION_ATTRIBUTE_NAME);
                    if (stream == null) return null;
                }
                else
                {
                    // size > 0
                    Integer numFragObj = (Integer)session.getAttribute(SESSION_ATTRIBUTE_NAME_FRAGMENT_COUNT);
                    if (numFragObj == null) return null; // Nueva sesión, no se ha guardado
                    int numFrag = numFragObj.intValue();
                    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                    for(int i = 0; i < numFrag; i++)
                    {
                        byte[] fragment = (byte[])session.getAttribute(SESSION_ATTRIBUTE_NAME_FRAGMENT_NUM + i);
                        byteArray.write(fragment);
                    }
                    stream = byteArray.toByteArray();
                }

                return deserializeSession(stream);
            }
            else // No serialización explícita
            {
                return (ItsNatSessionSerializeContainerImpl)session.getAttribute(SESSION_ATTRIBUTE_NAME);
            }
        }
        catch(Exception ex)
        {
            // Este error puede darse por ejemplo a causa de un cambio de configuración del ItsNatServletContextImpl
            // existiendo sesiones por ejemplo ya serializadas a archivo en el contenedor de servlets (por un rearranque).
            // En ese caso es posible que sea el cast el que falle por ejemplo (por cambio de método de serialización)
            // Así devolviendo null desecharemos esta sesión "silenciosamente" sin provocar un error.
            ItsNatSessionSerializeContainerImpl.showError(ex,false);
            return null;
        }
    }

    protected static ItsNatHttpSessionImpl readItsNatHttpSessionFromAttribute(HttpSession session,ItsNatServletContextImpl itsNatContext,ItsNatHttpServletRequestImpl itsNatRequest)
    {
        try
        {
            ItsNatSessionSerializeContainerImpl serialContainer = readItsNatSessionSerializeContainerFromSessionAttribute(session,itsNatContext);
            if (serialContainer == null) return null; // No se ha guardado todavía en el atributo (nueva sesión)

            ItsNatHttpSessionReplicationCapableImpl itsNatSession = (ItsNatHttpSessionReplicationCapableImpl)serialContainer.getItsNatSession();
            // Puede ser nula la sesión, significa que la lectura fue errónea (así soportamos cambios en el código sin eliminar manualmente las sesiones guardadas en GAE)
            if (itsNatSession == null) return null;

            // A partir de aquí itsNatSession NO es nulo

            if (itsNatSession.session == null) // Es una instancia nueva resultado de de-serializar
            {
                // Pasamos por aquí inmediatamente después de de-serializar

                // Fue obtenido por serialización desde otro nodo o bien al cargar el servidor
                // antes del request, la sesión nativa no está definida todavía porque es transient
                itsNatSession.session = session;

                itsNatSession.executeDeserialPendingTasks(itsNatContext,itsNatRequest);

                itsNatContext.addItsNatSession(itsNatSession);
            }

            return itsNatSession; // En teoría devolvemos aquí una sesión bien deserializada y formada
        }
        catch(Exception ex)
        {
            // Hay que excluir cualquier posibilidad de que por cambios de esquema, de registro de templates
            // de lo que sea ocurra una excepción en el proceso de deserialización de una sesión guardada
            // probablemente antes de los cambios dados, pues lo fácil es deshechar la sessión y punto
            // sin que GAE o ItsNat crea que ha habido un error (lo que ocurre si dejamos propagar la excepción)
            ItsNatSessionSerializeContainerImpl.showError(ex,false);
            return null;
        }
    }

    protected void executeDeserialPendingTasks(ItsNatServletContextImpl itsNatContext,ItsNatServletRequestImpl itsNatRequest) throws Exception
    {
        // Es posible que la deserialización se produjera no ahora sino al cargarse
        // el servidor o al restaurar una sesión que no se ha tocado
        // desde hace tiempo, en ese caso el servlet no estaba
        // iniciado y algunas tareas quedaron pendientes, ahora que está
        // el servlet iniciado podemos hacerlo
        // Ahora bien, este request pertenece a un servlet concreto y puede haber varios
        // servlets, pero tenemos que despertar a todos los servlets AHORA porque de otra forma
        // al terminar este request se intentarán serializar los documentos de otros servlets
        // que ni siquiera están correctamente de-serializados

        DeserialPendingTask sessionTask = getSessionDeserialPendingTask();
        if (sessionTask != null)
        {
            ItsNatServletImpl itsNatServlet = itsNatRequest.getItsNatServletImpl();
            ItsNatServletResponse itsNatResponse = itsNatRequest.getItsNatServletResponse();

            sessionTask.process(itsNatServlet,itsNatRequest,itsNatResponse);

            setSessionDeserialPendingTask(null); // Para liberar memoria
        }

        if (hasDeserialPendingTasks())
        {
            ItsNatServletImpl itsNatServlet = itsNatRequest.getItsNatServletImpl();
            String servletName = itsNatServlet.getName();

            ItsNatServletResponse itsNatResponse = itsNatRequest.getItsNatServletResponse();

            ServletContext context = itsNatContext.getServletContext();

            MapListImpl pendingTasks = getDeserialPendingTasks();

            for(Iterator it = pendingTasks.getMap().entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry entry = (Map.Entry)it.next();
                String currServletName = (String)entry.getKey();
                LinkedList pendingTasksOfServlet = (LinkedList)entry.getValue();
                if (pendingTasksOfServlet == null) continue; // Por si acaso pero es raro que sea nulo

                if (servletName.equals(currServletName))
                {
                    // El servlet que está haciendo la request de verdad
                    for(Iterator it2 = pendingTasksOfServlet.iterator(); it2.hasNext(); )
                    {
                        DeserialPendingTask task = (DeserialPendingTask)it2.next();
                        task.process(itsNatServlet,itsNatRequest,itsNatResponse);
                    }
                }
                else
                {
                    ItsNatServletImpl currItsNatServlet = ItsNatServletImpl.getItsNatServletByName(currServletName);

                    ServletRequest servRequest = itsNatRequest.getServletRequest();
                    ServletResponse servResponse = itsNatResponse.getServletResponse();

                    if (currItsNatServlet == null)
                    {
                        // Despertamos al servlet para que se inicie y se registren los templates etc
                        RequestDispatcher servletDisp = context.getNamedDispatcher(currServletName);
                        // No chequeamos si es null, caso de eliminación de servlet o similar en una nueva versión de la app.
                        // no merece la pena porque la deserialización será errónea, dejamos fallar
                        // aunque perdamos la sessión entera
                        Object currItsNatAction = servRequest.getAttribute("itsnat_action");
                        servRequest.setAttribute("itsnat_action", ItsNatHttpServletImpl.ACTION_SERVLET_WEAK_UP);

                        servletDisp.include(servRequest,servResponse); // Aseguramos así que se inicializa
                        // Lo dejamos como estaba
                        servRequest.removeAttribute("itsnat_action");
                        if (currItsNatAction != null)
                            servRequest.setAttribute("itsnat_action",currItsNatAction);

                        // Ahora debería de estar
                        currItsNatServlet = ItsNatServletImpl.getItsNatServletByName(currServletName);
                    }


                    // Porque este servlet es diferente al que recibe la request, no pasamos
                    // los objetos request y response originales pues los de ItsNat están vinculados
                    // al servlet, tenemos que crear un par "falsos"
                    // el único caso problemático son los templates basados en TemplateSource que son los únicos que necesitan estos objetos
                    ItsNatServletRequestImpl currItsNatServReq = currItsNatServlet.createItsNatServletRequest(servRequest,servResponse,this); // Pasando la sesión como parámetro evitamos que se intente cargar de nuevo
                    ItsNatServletResponseImpl currItsNatServResp = currItsNatServReq.getItsNatServletResponseImpl();

                    for(Iterator it2 = pendingTasksOfServlet.iterator(); it2.hasNext(); )
                    {
                        DeserialPendingTask task = (DeserialPendingTask)it2.next();
                        task.process(currItsNatServlet,currItsNatServReq,currItsNatServResp);
                    }
                }
            }

            clearDeserialPendingTasks(); // Para liberar memoria
        }
    }

    
    public static byte[] serializeSession(ItsNatSessionSerializeContainerImpl serialContainer)
    {
        ByteArrayOutputStream ostream = null;
        try
        {
           ostream = new ByteArrayOutputStream();
           ObjectOutputStream p = new ObjectOutputStream(ostream);
           p.writeObject(serialContainer); // Write the tree to the stream.
           p.flush();
           ostream.close();    // close the file.
        }
        catch (Exception ex)
        {
            try
            {
                if (ostream != null) ostream.close();
            }
            catch(IOException ex2)
            {
                ex.printStackTrace();
                throw new ItsNatException(ex2);
            }
            throw new ItsNatException(ex);
        }

        return ostream.toByteArray();
    }

    public static ItsNatSessionSerializeContainerImpl deserializeSession(byte[] stream)
    {
        ItsNatSessionSerializeContainerImpl serialContainer;
        ByteArrayInputStream istream = null;
        try
        {
            istream = new ByteArrayInputStream(stream);
            ObjectInputStream q = new ObjectInputStream(istream);
            serialContainer = (ItsNatSessionSerializeContainerImpl)q.readObject();
            istream.close();
        }
        catch (Exception ex)
        {
            try
            {
                if (istream != null) istream.close();
            }
            catch(IOException ex2)
            {
                ex.printStackTrace();
                throw new ItsNatException(ex2);
            }
            throw new ItsNatException(ex);
        }

        return serialContainer;
    }
}
