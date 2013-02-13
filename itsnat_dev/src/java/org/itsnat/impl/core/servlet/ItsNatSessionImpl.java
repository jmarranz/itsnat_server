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

package org.itsnat.impl.core.servlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatServletContext;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.ItsNatSession;
import org.itsnat.core.ItsNatVariableResolver;
import org.itsnat.impl.core.*;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedServerImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulOwnerImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.util.HasUniqueId;
import org.itsnat.impl.core.util.MapListImpl;
import org.itsnat.impl.core.util.MapUniqueId;
import org.itsnat.impl.core.util.UniqueId;
import org.itsnat.impl.core.util.UniqueIdGenIntList;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatSessionImpl extends ItsNatUserDataImpl
                        implements ItsNatSession,HasUniqueId
{
    public static final Comparator<ClientDocumentStfulOwnerImpl> COMPARATOR_STFUL_OWNER = new LastRequestComparator<ClientDocumentStfulOwnerImpl>();    
    public static final Comparator<ClientDocumentAttachedClientImpl> COMPARATOR_ATTACHED_CLIENTS = new LastRequestComparator<ClientDocumentAttachedClientImpl>();
    public static final Comparator<ClientDocumentAttachedServerImpl> COMPARATOR_ATTACHED_SERVERS = new LastRequestComparator<ClientDocumentAttachedServerImpl>();    
        
    
    protected transient ItsNatSessionSerializeContainerImpl serialContainer;
    protected transient ItsNatServletContextImpl context; // No serializamos la instancia pero sí serializaremos el Necesita serializarse porque el generador de ids debe estar en todas las JVMs
    protected transient UniqueId idObj; // No se serializa si no se serializa el contexto pues el generador de ids debe ser el mismo objeto que hay en ItsNatServletContextImpl
    protected final UniqueIdGenIntList idGenerator = new UniqueIdGenIntList(true);
    protected final MapUniqueId docsById = new MapUniqueId(idGenerator); // Los ItsNatDocument que son propiedad de esta sesión. Los ids han sido generados por esta sesión. Es auxiliar pues los ClientDocumentOwner de ownerClientsById ya sujetan los ItsNatDocument, sirve para buscar docs por Id
    protected final MapUniqueId ownerClientsById = new MapUniqueId(idGenerator);
    protected final MapUniqueId attachedClientsById = new MapUniqueId(idGenerator); // Sirve para retener los attachedClients para que no sean garbage collected hasta que la sesión se pierda. Los ids han sido generados por esta sesión
    protected final MapUniqueId attachedServersById = new MapUniqueId(idGenerator); // Sirve para guardar provisionalmente datos durante la carga
    protected Browser browser; // El de la primera request, en el ClientDocumentImpl puede cambiar pero nos sirve para los casos en donde no cambia
    protected Referrer referrer;
    protected String token;
    protected transient DeserialPendingTask sessionDeserialPendingTask;
    protected transient MapListImpl<String,DeserialPendingTask> deserialPending;

    /** Creates a new instance of ItsNatSessionImpl */
    public ItsNatSessionImpl(ItsNatServletContextImpl context,Browser browser)
    {
        super(true);

        this.context = context;
        this.browser = browser;
        this.referrer = Referrer.createReferrer(browser);
        this.token = System.currentTimeMillis() + "_" + context.getNewToken();
        // El token NO es el identificador absoluto de la sesión, sirve
        // para asegurar que dos sesiones NO tienen el mismo token
        // ENTRE DIFERENTES CARGAS de la aplicación o del servidor en general.
        // Sirve para detectar que la sesión del usuario en el cliente no
        // se corresponde con la del servidor (normalmente porque se ha creado nueva)
        // es decir la sesión expiró o el servidor se recargó. Apenas sirve para
        // gestionar este caso no es tanto un problema de seguridad aunque ayuda
        // (la verdadera identidad segura es la cookie de la sesión).
        // No pasa nada porque dos sesiones tengan el mismo token aunque
        // sea casi imposible gracias al número aleatorio (en un supercomputador y millones de usuarios podría ser),
        // lo importante es que el token sea CON SEGURIDAD diferente entre sesiones
        // del usuario pues el currentTimeMillis asegura que la sesión nueva tenga un token
        // diferente, dos sesiones seguidas creadas para el mismo usuario en el mismo milisegundo
        // es prácticamente imposible y con el random es imposible, el random ayuda a que ni por suerte
        // (al intentar adivinar el milisegundo actual) pueda acertarse.
        // Este token se ha demostrado útil también en GAE con session replication capable desactivado
        // para detectar que un evento ha sido envíado a un nodo distinto en el que no está la sesión ItsNat
        // pero puede existir una sesión de otro usuario con el mismo id (dicho id es local al nodo realmente)
        // el resultado es que al recargarse la página (es lo propio el evento es huérfano) se
        // crea un nuevo objeto sesión ItsNat en el nuevo nodo.
    }

    public void setItsNatSessionSerializeContainer(ItsNatSessionSerializeContainerImpl serialContainer)
    {
        this.serialContainer = serialContainer;
    }

    public DeserialPendingTask getSessionDeserialPendingTask()
    {
        return sessionDeserialPendingTask;
    }

    public void setSessionDeserialPendingTask(DeserialPendingTask task)
    {
        this.sessionDeserialPendingTask = task;
    }

    public boolean hasDeserialPendingTasks()
    {
        if (deserialPending == null) return false;
        return !deserialPending.isEmpty();
    }

    public MapListImpl<String,DeserialPendingTask> getDeserialPendingTasks()
    {
        if (deserialPending == null) this.deserialPending = new MapListImpl<String,DeserialPendingTask>();
        return deserialPending;
    }

    public void addDeserialPendingTask(String servletName,DeserialPendingTask task)
    {
        getDeserialPendingTasks().add(servletName,task);
    }

    public void clearDeserialPendingTasks()
    {
        if (deserialPending == null) return;
        deserialPending.clear();
        this.deserialPending = null; // Para ahorrar memoria
    }

/*
    public LinkedList getDeserialPendingTasks(String servletName)
    {
        if (deserialPending == null) return null;
        return deserialPending.get(servletName);
    }

    public void clearDeserialPendingTasks(String servletName)
    {
        if (deserialPending == null) return;

        deserialPending.remove(servletName);

        if (deserialPending.isEmpty())
            this.deserialPending = null; // Para ahorrar memoria
    }
*/

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        // Esto es para impedir que progrese una serialización de objetos ItsNat explícita 
        // en la sesión (sea cual sea al final acabará serializándose este objeto sesión)
        // pues no podemos usar "deserial pending tasks",
        // ver notas dentro del método y también ItsNatSessionObjectInputStream:
        ItsNatSessionObjectOutputStream.castToItsNatSessionObjectOutputStream(out);

        out.writeObject(idObj.getId()); // No podemos serializar el UniqueIdGenerator pues pertenece al ItsNatServletContextImpl no serializado

        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        ItsNatSessionObjectInputStream.setItsNatSession(in, this);

        final String id = (String)in.readObject();

        // Es normal que se deserialice antes de un primer request (antes de inicializar servlets), en el caso
        // de recarga de la JVM deserializa las sesiones salvadas.
        DeserialPendingTask task = new DeserialPendingTask()
        {
            public void process(ItsNatServletImpl itsNatServlet,ItsNatServletRequest request, ItsNatServletResponse response)
            {
                ItsNatSessionImpl.this.context = itsNatServlet.getItsNatServletContextImpl();
                ItsNatSessionImpl.this.idObj = new UniqueId(id,context.getUniqueIdGenerator());
            }
        };
        setSessionDeserialPendingTask(task);

        in.defaultReadObject();
    }

    public abstract void endOfRequestBeforeSendCode();

    // Este método es llamado cuando el request finaliza
    // viene a ser el simétrico a getItsNatHttpSession(ItsNatHttpServletRequestImpl
    // en la clase derivada Http.
    public abstract void endOfRequest();

    public void destroy()
    {
        // Así no esperamos al garbage collector (de hecho no sería necesario guardar los session con weak maps)
        ItsNatServletContextImpl context = getItsNatServletContextImpl();
        context.removeItsNatSession(this);

        // Esta limpieza hay que hacerla por si acaso porque algunos navegadores no tienen
        // asegurado el "unload" (también pueden haberse colgado, caído etc)
        // y porque en casos como los CometNotifiers o los visores remotos
        // de documentos de otras sesiones hay dependencias entre sesiones, hilos etc
        // así aseguramos que las dependencias detectan que los documentos son inválidos

        // Ahora invalidamos los clientes (se invalidará el documento también),
        // así evitamos sincronizar un documento dentro de un bloque sincronizado de la sesión (para evitar dead locks)
        // Esto lo hacemos por ejemplo para terminar CometNotifier abiertos
        ClientDocumentStfulOwnerImpl[] clients = getClientDocumentStfulOwnerArray();
        if (clients != null)
        {
            for(int i = 0; i < clients.length; i++)
            {
                ClientDocumentStfulOwnerImpl clientDoc = clients[i];
                ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();
                synchronized(itsNatDoc) // No es necesario sincronizar los padres pues esta acción sólo afecta a este documento
                {
                    clientDoc.setInvalid();
                }
            }
        }

        // Por si acaso pero no es necesario
        synchronized(ownerClientsById)
        {
            ownerClientsById.clear();
            docsById.clear();
        }

        // Ahora los observadores (que pueden serlo de otros documentos)

        ClientDocumentAttachedClientImpl[] attachedClients = getClientDocumentAttachedClientArray();
        if (attachedClients != null)
        {
            for(int i = 0; i < attachedClients.length; i++)
            {
                ClientDocumentAttachedClientImpl clientDoc = attachedClients[i];
                ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();
                synchronized(itsNatDoc) // No es necesario sincronizar los padres pues esta acción sólo afecta a este documento
                {
                    clientDoc.setInvalid(); // Yo creo que no hace falta pero por si acaso
                }
            }
        }

        // Esto es necesario, porque la invalidación (que puede hacerse el documento de otra sesión)
        // no quita el cliente de la sesión propietaria
        synchronized(attachedClientsById)
        {
            attachedClientsById.clear();
        }

        // Con los attached server no hace falta invalidar ni nada
        synchronized(attachedServersById)
        {
            attachedServersById.clear();
        }

        referrer.popItsNatStfulDocument(); // Asegura que se vacía
    }

    public String getToken()
    {
        return token;
    }

    public Browser getBrowser()
    {
        return browser;
    }

    public String getId()
    {
        return idObj.getId();
    }

    public UniqueId getUniqueId()
    {
        return idObj;
    }

    public String getUserAgent()
    {
        // No lo hacemos público porque es más bien del mundo Http, lo ponemos en este nivel por razones prácticas y en futuro con un caso no HTTP se podría consolidar como público
        return browser.getUserAgent();
    }

    protected abstract int getMaxInactiveInterval(); // Idem getUserAgent()

    public long getMaxInactiveIntervalMillisec()
    {
        int maxInterval = getMaxInactiveInterval(); // Devuelve segundos
        if (maxInterval < 0) // No caduca
            return Long.MAX_VALUE; // Equivale a unos 106.000 millones de días, nos vale :)
        return 1000 * maxInterval;
    }

    public abstract Object getStandardSessionObject();
    public abstract String getStandardSessionId();

    public ItsNatServletContext getItsNatServletContext()
    {
        return context;
    }

    public ItsNatServletContextImpl getItsNatServletContextImpl()
    {
        return context;
    }

    public Referrer getReferrer()
    {
        return referrer;
    }

    public ClientDocumentStfulImpl getClientDocumentStfulById(String id)
    {
        ClientDocumentStfulImpl clientDoc = getClientDocumentStfulOwnerById(id);
        if (clientDoc == null)
            clientDoc = getClientDocumentAttachedClientById(id);
        return clientDoc;
    }

    public int getClientDocumentStfulOwnerCount()
    {
        synchronized(ownerClientsById)
        {
           return ownerClientsById.size();
        }
    }

    public ClientDocumentStfulOwnerImpl[] getClientDocumentStfulOwnerArray()
    {
        // La regla es que con la sesión bloqueada NO debe bloquearse nada más
        // por eso formamos y devolvemos un array, para evitar iterar el Map con la sesión bloqueada
        // pues lo normal es que se necesite bloquear a su vez el documento asociado para acceder al objeto
        synchronized(ownerClientsById)
        {
            int size = ownerClientsById.size();
            if (size == 0) return null;

            ClientDocumentStfulOwnerImpl[] res = new ClientDocumentStfulOwnerImpl[size];
            int i = 0;
            for(Iterator<Map.Entry<String,HasUniqueId>> it = ownerClientsById.entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry<String,HasUniqueId> entry = it.next();
                res[i] = (ClientDocumentStfulOwnerImpl)entry.getValue();
                i++;
            }
            return res;
        }
    }

    public ClientDocumentStfulOwnerImpl getClientDocumentStfulOwnerById(String id)
    {
        synchronized(ownerClientsById)
        {
            return (ClientDocumentStfulOwnerImpl)ownerClientsById.get(id);
        }
    }

    public void registerClientDocumentStfulOwner(ClientDocumentStfulOwnerImpl clientDoc)
    {
        ClientDocumentStfulOwnerImpl clientRes;
        ItsNatStfulDocumentImpl docRes;

        ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();
        synchronized(ownerClientsById)
        {
            clientRes = (ClientDocumentStfulOwnerImpl)ownerClientsById.put(clientDoc);
            docRes = (ItsNatStfulDocumentImpl)docsById.put(itsNatDoc);
        }

        if (clientRes != null) throw new ItsNatException("INTERNAL ERROR");
        if (docRes != null) throw new ItsNatException("INTERNAL ERROR");
    }

    public void unregisterClientDocumentStfulOwner(ClientDocumentStfulOwnerImpl clientDoc)
    {
        ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();
        synchronized(ownerClientsById)
        {
            ownerClientsById.remove(clientDoc);
            docsById.remove(itsNatDoc);
        }
    }

    public ItsNatDocument[] getItsNatDocuments()
    {
        return getItsNatStfulDocumentArray();
    }

    public int getItsNatDocumentCount()
    {
        synchronized(ownerClientsById) // docsById está subyugada y sincronizada a ownerClientsById
        {
           return docsById.size();
        }
    }

    public ItsNatStfulDocumentImpl[] getItsNatStfulDocumentArray()
    {
        // La regla es que con la sesión bloqueada NO debe bloquearse nada más
        // por eso formamos y devolvemos un array, para evitar iterar el Map con la sesión bloqueada
        // pues lo normal es que se necesite bloquear a su vez el documento asociado para acceder al objeto
        synchronized(ownerClientsById) // docsById está subyugada y sincronizada a ownerClientsById
        {
            ItsNatStfulDocumentImpl[] res = new ItsNatStfulDocumentImpl[docsById.size()];
            int i = 0;
            for(Iterator<Map.Entry<String,HasUniqueId>> it = docsById.entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry<String,HasUniqueId> entry = it.next();
                res[i] = (ItsNatStfulDocumentImpl)entry.getValue();
                i++;
            }
            return res;
        }
    }

    public ItsNatDocument getItsNatDocumentById(String id)
    {
        return getItsNatStfulDocumentById(id);
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocumentById(String id)
    {
        synchronized(ownerClientsById) // docsById está subyugada y sincronizada a ownerClientsById
        {
            return (ItsNatStfulDocumentImpl)docsById.get(id);
        }
    }

    public UniqueIdGenIntList getUniqueIdGenerator()
    {
        return idGenerator;
    }

    public ClientDocumentAttachedClientImpl getClientDocumentAttachedClientById(String id)
    {
        synchronized(attachedClientsById)
        {
            return (ClientDocumentAttachedClientImpl)attachedClientsById.get(id);
        }
    }

    private void addClientDocumentAttachedClient(ClientDocumentAttachedClientImpl clientDoc)
    {
        ClientDocumentAttachedClientImpl res;
        synchronized(attachedClientsById)
        {
            res = (ClientDocumentAttachedClientImpl)attachedClientsById.put(clientDoc);
        }
        if (res != null) throw new ItsNatException("INTERNAL ERROR"); // Asegura el registro una sola vez
    }

    private boolean removeClientDocumentAttachedClient(ClientDocumentAttachedClientImpl clientDoc)
    {
        ClientDocumentAttachedClientImpl res;
        synchronized(attachedClientsById)
        {
            res = (ClientDocumentAttachedClientImpl)attachedClientsById.remove(clientDoc);
        }
        return (res != null);  // Si true es que fue removido
    }

    public int getClientDocumentAttachedClientCount()
    {
        synchronized(attachedClientsById)
        {
           return attachedClientsById.size();
        }
    }

    public ClientDocumentAttachedClientImpl[] getClientDocumentAttachedClientArray()
    {
        synchronized(attachedClientsById)
        {
            int size = attachedClientsById.size();
            if (size == 0) return null;

            ClientDocumentAttachedClientImpl[] res = new ClientDocumentAttachedClientImpl[size];
            int i = 0;
            for(Iterator<Map.Entry<String,HasUniqueId>> it = attachedClientsById.entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry<String,HasUniqueId> entry = it.next();
                res[i] = (ClientDocumentAttachedClientImpl)entry.getValue();
                i++;
            }
            return res;
        }
    }

    public void registerClientDocumentAttachedClient(ClientDocumentAttachedClientImpl clientDoc)
    {
        addClientDocumentAttachedClient(clientDoc); // Sujeta el objeto, si ya fue registrado dará error

        ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();
        itsNatDoc.addClientDocumentAttachedClient(clientDoc); // permite que se notifiquen los cambios
    }

    public void unregisterClientDocumentAttachedClient(ClientDocumentAttachedClientImpl clientDoc)
    {
        // Se supone que el documento asociado, si no se ha perdido, está sincronizado
        clientDoc.setInvalid(); // Envía el stop al cliente, si ya es inválido no hace nada

        boolean res = removeClientDocumentAttachedClient(clientDoc);
        if (res) // Si res es falso es que ya se desregistró, pues aquí hay que evitar hacerlo varias veces
        {
            ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();
            itsNatDoc.removeClientDocumentAttachedClient(clientDoc); // El documento no lo retiene pero así se quita explícitamente
        }
    }

    public ClientDocumentAttachedServerImpl getClientDocumentAttachedServersById(String id)
    {
        synchronized(attachedServersById)
        {
            return (ClientDocumentAttachedServerImpl)attachedServersById.get(id);
        }
    }

    public void addClientDocumentAttachedServer(ClientDocumentAttachedServerImpl clientDoc)
    {
        ClientDocumentAttachedServerImpl res;
        synchronized(attachedServersById)
        {
            res = (ClientDocumentAttachedServerImpl)attachedServersById.put(clientDoc);
        }
        if (res != null) throw new ItsNatException("INTERNAL ERROR"); // Asegura el registro una sola vez
    }

    public boolean removeClientDocumentAttachedServer(ClientDocumentAttachedServerImpl clientDoc)
    {
        ClientDocumentAttachedServerImpl res;
        synchronized(attachedServersById)
        {
            res = (ClientDocumentAttachedServerImpl)attachedServersById.remove(clientDoc);
        }
        return (res != null);  // Si true es que fue removido
    }

    public int getClientDocumentAttachedServerCount()
    {
        synchronized(attachedServersById)
        {
           return attachedServersById.size();
        }
    }

    public ClientDocumentAttachedServerImpl[] getClientDocumentAttachedServerArray()
    {
        synchronized(attachedServersById)
        {
            int size = attachedServersById.size();
            if (size == 0) return null;

            ClientDocumentAttachedServerImpl[] res = new ClientDocumentAttachedServerImpl[size];
            int i = 0;
            for(Iterator<Map.Entry<String,HasUniqueId>> it = attachedServersById.entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry<String,HasUniqueId> entry = it.next();
                res[i] = (ClientDocumentAttachedServerImpl)entry.getValue();
                i++;
            }
            return res;
        }
    }

    public ItsNatVariableResolver createItsNatVariableResolver()
    {
        return new ItsNatVariableResolverImpl(null,null,null,this,null);
    }

    public Object getVariable(String varName)
    {
        Object value = getAttribute(varName);
        if (value != null)
            return value;

        return getItsNatServletContextImpl().getVariable(varName);
    }

    public void invalidateLostResources()
    {
        // Suponemos que no hay nada sincronizado dependiente de esta sesión
        // (ej. documentos de esta sesión) cuando este método es llamado.

        long currentTime = System.currentTimeMillis();
        long maxInactiveInterval = getMaxInactiveIntervalMillisec();

        cleanExpiredClients(currentTime,maxInactiveInterval);
        cleanExpiredAttachedServerClients(currentTime,maxInactiveInterval);
        cleanExpiredReferrer(currentTime,maxInactiveInterval);

        cleanExcessClientDocumentStfulOwners();
        cleanExcessClientDocumentAttachedServers();
    }

    protected void cleanExpiredClients(long currentTime,long maxInactiveInterval)
    {
        // Aunque este método es fundamentalmente útil para navegadores que
        // no siempre envían el evento unload (ejemplo Opera 9 y muchos otros),
        // puede ocurrir que una página cargada por un navegador que sí lo hace
        // haya sido parada la carga antes de que se ejecute el script de inicio,
        // por lo que el evento "unload" no se ejecutará de todas formas. Por ello ejecutamos
        // esta limpieza para cualquier navegador.

        ClientDocumentStfulOwnerImpl[] clientOwnerList = getClientDocumentStfulOwnerArray();
        if (clientOwnerList != null)
        {
            for(int i = 0; i < clientOwnerList.length; i++)
            {
                ClientDocumentStfulOwnerImpl clientDoc = clientOwnerList[i];
                long lastRequestTime = clientDoc.getLastRequestTime();  // No hace falta sincronizar
                long interval = currentTime - lastRequestTime;
                if (interval > maxInactiveInterval)
                {
                    ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();  // No hace falta sincronizar
                    synchronized(itsNatDoc) // No es necesario sincronizar los padres pues esta acción sólo afecta a este documento
                    {
                        clientDoc.setInvalid();
                    }
                }
                else
                {
                    // Es posible que haya clientes de control remoto zombies asociados al documento de este cliente y que no pertenezcan
                    // a esta sesión, es posible que el usuario cerrara el cliente control remoto
                    // pero el navegador no notificara este cierre (ocurre en algunos), si el usuario sigue
                    // activo en otra página la sesión seguirá viva por lo que el cliente zombie seguirá
                    // recibiendo código JavaScript indefinidamente.
                    // Por tanto los intentamos limpiar aquí:
                    ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();  // No hace falta sincronizar
                    synchronized(itsNatDoc) // No es necesario sincronizar los padres pues esta acción sólo afecta a este documento
                    {
                        if (itsNatDoc.hasClientDocumentAttachedClient())
                        {
                            ClientDocumentAttachedClientImpl[] clientAttachList = itsNatDoc.getClientDocumentAttachedClientArray();
                            for(int j = 0; j < clientAttachList.length; j++)
                            {
                                ClientDocumentAttachedClientImpl clientDocAttached = clientAttachList[j];
                                ItsNatSessionImpl attachedSession = clientDocAttached.getItsNatSessionImpl();
                                if (attachedSession == this) continue; // No merece la pena, se procesará después
                                cleanExpiredClientDocumentAttachedClient(clientDocAttached,currentTime); // currentTime vale pero el maxInactiveInterval depende de la sesión
                            }
                        }

                        // Ya que estamos aprovechamos para limpiar los attached client excedentes
                        // Así eliminamos los clientes attached excedentes de los documentos
                        // guardados en esta sesión estén expirados o no (cada sesión hará lo suyo aunque sean clientes en diferentes sesiones)
                        // obviamente eliminaremos primero los que no han sido tocados durante más tiempo

                        cleanExcessClientDocumentAttachedClients(itsNatDoc);
                    }
                }
            }
        }

        // Eliminamos los ClientDocument observadores que pueden estar zombies
        // porque el navegador no ha notificado que se cerró la ventana.
        // Esta limpieza es redundante (pero necesaria) pues
        // esta impieza ya la hace el cliente propietario del documento (antes de llegar aquí).
        // De hecho el propietario no hace la limpieza de sus auto-observadores (misma sesión) pues
        // ahora se hace aquí (ver "if (attachedSession == this) continue;")
        ClientDocumentAttachedClientImpl[] clientAttachedList = getClientDocumentAttachedClientArray();
        if (clientAttachedList != null)
        {
            for(int i = 0; i < clientAttachedList.length; i++)
            {
                ClientDocumentAttachedClientImpl clientDoc = clientAttachedList[i];
                cleanExpiredClientDocumentAttachedClient(clientDoc,currentTime);
            }
        }
    }

    public static void cleanExpiredClientDocumentAttachedClient(ClientDocumentAttachedClientImpl clientDoc,long currentTime)
    {
        ItsNatSessionImpl session = clientDoc.getItsNatSessionImpl(); // Nos aseguramos así que la sesión en la que se evalua es la propietaria del cliente (por eso el método es estático)
        long maxInactiveInterval = session.getMaxInactiveIntervalMillisec();

        long lastRequestTime = clientDoc.getLastRequestTime();  // No hace falta sincronizar
        long interval = currentTime - lastRequestTime;
        if (interval > maxInactiveInterval)
        {
            ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();
            synchronized(itsNatDoc) // No es necesario sincronizar los padres pues esta acción sólo afecta a este documento
            {
                clientDoc.invalidateAndUnregister(); // Desregistra de la sesión que puede no ser esta
            }
        }
    }


    public void cleanExpiredAttachedServerClients(long currentTime,long maxInactiveInterval)
    {
        // En teoría los clientes attached server son temporales durante el proceso
        // de carga, que se hace en múltiples requests, cuando la carga termina
        // se desregistra. Sin embargo es posible que por errores en el proceso
        // o por un cliente engañoso o lo que sea se quede a medias y por tanto quede
        // registrado.

        ClientDocumentAttachedServerImpl[] clientAttachedList = getClientDocumentAttachedServerArray();
        if (clientAttachedList != null)
        {
            for(int i = 0; i < clientAttachedList.length; i++)
            {
                ClientDocumentAttachedServerImpl clientDoc = clientAttachedList[i];

                // Realmente el getLastRequestTime es el getCreationTime pues se supone que si no hay error
                // los requests del proceso de carga son muy seguidos y no merece la pena actualizar, pero por si acaso.
                // Aunque es una barbaridad de tiempo usar el tiempo de inactividad de la sesión
                // no tenemos otro tiempo en qué basarnos y no merece la pena pedir al usuario
                // un nuevo tiempo cuando esto sólo va a ocurrir en caso de error o de usuario malicioso
                long lastRequestTime = clientDoc.getLastRequestTime();  // No hace falta sincronizar
                long interval = currentTime - lastRequestTime;
                if (interval > maxInactiveInterval)
                {
                    clientDoc.setInvalid();  // Se desregistra de la sesión
                }
            }
        }
    }

    protected void cleanExpiredReferrer(long currentTime,long maxInactiveInterval)
    {
        Referrer referer = getReferrer();
        if (referrer instanceof ReferrerStrong)
        {
            // Caso Opera y similares
            // Referrer es de tipo ReferrerStrong y en el unload
            // de la página la llamada cleanItsNatStfulDocument no hace nada
            // para que el destino pueda hacer pop al referrer
            // por lo que es posible que nadie haya querido el referrer
            // y haya quedado indefinidamente con una referencia strong.

            ItsNatStfulDocumentImpl itsNatDocRef = referer.getItsNatStfulDocument();
            if (itsNatDocRef != null)
            {
                ClientDocumentStfulOwnerImpl clientRef = itsNatDocRef.getClientDocumentStfulOwner();  // No hace falta sincronizar
                long lastRequestTime = clientRef.getLastRequestTime();  // No hace falta sincronizar
                long interval = currentTime - lastRequestTime;
                if (interval > maxInactiveInterval)
                    referer.popItsNatStfulDocument(); // Lo quitamos
            }
        }
    }

    protected void cleanExcessClientDocumentStfulOwners()
    {
        // Eliminamos los documentos excedentes estén expirados o no,
        // obviamente eliminaremos primero los que no han sido tocados durante más tiempo

        int maxLiveDocs = getItsNatServletContextImpl().getMaxOpenDocumentsBySession();
        if ((maxLiveDocs >= 0) && (getClientDocumentStfulOwnerCount() > maxLiveDocs))
        {
            // Solamente maxLiveDocs live documents allowed in this session
            // probablemente es un robot que admite cookies o un navegador no soportado
            // que da error y no recibe el evento unload o bien JavaScript
            // está desactivado aunque sea un navegador permitido
            ClientDocumentStfulOwnerImpl[] clients = getClientDocumentStfulOwnerArray();
            if (clients != null) // Por si acaso hay que tener en cuenta que es multihilo
            {
                int excess = clients.length - maxLiveDocs;

                // Invalidamos los que llevan más tiempo sin usar
                Arrays.sort(clients,COMPARATOR_STFUL_OWNER);
                for(int i = 0; i < excess; i++)
                {
                    ClientDocumentStfulOwnerImpl clientDoc = clients[i];
                    ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();
                    synchronized(itsNatDoc) // No es necesario sincronizar los padres pues esta acción sólo afecta a este documento
                    {
                        clientDoc.setInvalid();
                    }
                }
            }
        }
    }

    public void cleanExcessClientDocumentAttachedClients(ItsNatStfulDocumentImpl itsNatDoc)
    {
        // El documento debe estar sincronizado, se ejecutará en monohilo

        int maxClientAttachedNum;
        int maxClients = itsNatDoc.getMaxOpenClientsByDocument();
        if (maxClients < -1) maxClientAttachedNum = -1; // Ilimitado
        else maxClientAttachedNum = maxClients - 1; // Pues en maxClients se incluye el owner y sólo vamos a limpiar los attached (y no puede ser cero)
        if ((maxClientAttachedNum >= 0) && (itsNatDoc.getClientDocumentAttachedCount() > maxClientAttachedNum))
        {
            ClientDocumentAttachedClientImpl[] clientList = itsNatDoc.getClientDocumentAttachedClientArray();

            int excess = clientList.length - maxClientAttachedNum;

            // Invalidamos los que llevan más tiempo sin usar
            Arrays.sort(clientList,COMPARATOR_ATTACHED_CLIENTS);
            for(int i = 0; i < excess; i++)
            {
                ClientDocumentAttachedClientImpl clientDoc = clientList[i];
                clientDoc.invalidateAndUnregister(); // Se desregistra de su sesión (puede no ser esta)
            }
        }
    }

    protected void cleanExcessClientDocumentAttachedServers()
    {
        // Eliminamos los clientes attached server en proceso de carga que sobren estén
        // expirados o no con el fin de que un usuario malicioso quiera saturarnos la sesión
        // iniciando montones de clientes attached server seguidos pero evitando
        // la carga completa y por tanto evitando su destrucción.
        // Como un cliente attached server cuando termina su vida acaba siendo un documento
        // normal, es razonable evitar que haya más procesos en carga que documentos
        // permitidos en la sesión pues de todas formas cuando acaben serán invalidados
        // obviamente eliminaremos primero los que no han sido tocados durante más tiempo

        int maxLiveDocs = getItsNatServletContextImpl().getMaxOpenDocumentsBySession();
        if ((maxLiveDocs >= 0) && (getClientDocumentAttachedServerCount() > maxLiveDocs))
        {
            // Solamente maxLiveDocs live documents allowed in this session
            // probablemente es un robot que admite cookies o un navegador no soportado
            // que da error y no recibe el evento unload o bien JavaScript
            // está desactivado aunque sea un navegador permitido
            ClientDocumentAttachedServerImpl[] clientList = getClientDocumentAttachedServerArray();
            if (clientList != null) // Por si acaso hay que tener en cuenta que es multihilo
            {
                int excess = clientList.length - maxLiveDocs;

                // Invalidamos los que llevan más tiempo sin usar
                Arrays.sort(clientList,COMPARATOR_ATTACHED_SERVERS);
                for(int i = 0; i < excess; i++)
                {
                    ClientDocumentAttachedServerImpl clientDoc = clientList[i];
                    clientDoc.setInvalid();   // Se desregistra de la sesión
                }
            }
        }
    }


}

class LastRequestComparator<T> implements Comparator<T>
{
    public int compare(T o1, T o2)
    {
        ClientDocumentImpl clientDoc1 = (ClientDocumentImpl)o1;
        ClientDocumentImpl clientDoc2 = (ClientDocumentImpl)o2;
        long t1 = clientDoc1.getLastRequestTime();
        long t2 = clientDoc2.getLastRequestTime();
        if (t1 < t2) return -1;
        else if (t1 > t2) return +1;
        else return 0;
    }
}

