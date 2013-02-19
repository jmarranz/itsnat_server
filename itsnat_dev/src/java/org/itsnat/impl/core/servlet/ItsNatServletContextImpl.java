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

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import javax.servlet.ServletContext;
import org.itsnat.core.ItsNat;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatServletContext;
import org.itsnat.core.ItsNatSessionCallback;
import org.itsnat.core.ItsNatVariableResolver;
import org.itsnat.impl.core.*;
import org.itsnat.impl.core.util.MapUniqueId;

/**
 *
 * @author jmarranz
 */
public class ItsNatServletContextImpl extends ItsNatUserDataImpl implements ItsNatServletContext
{
    public static final long SESSION_EXPLICIT_SERIALIZE_ONE_FRAGMENT = 0;   // siempre un sólo fragmento

    protected ItsNatImpl itsNat;
    protected ServletContext servletContext; 
    protected final Map<String,ItsNatSessionImpl> sessionsByStandardId = new HashMap<String,ItsNatSessionImpl>();
    protected Map<String,ItsNatSessionImpl> sessionsByItsNatId = new HashMap<String,ItsNatSessionImpl>();
    protected ItsNatServletContextUniqueIdGenImpl idGenerator = new ItsNatServletContextUniqueIdGenImpl(this);
    protected Random random = new Random();
    protected int maxOpenDocumentsBySession = 10;
    protected boolean maxOpenDocumentsBySession_WasSet = false;
    protected boolean sessionReplicationCapable = false;
    protected boolean sessionReplicationCapable_WasSet = false;
    protected boolean sessionSerializeCompressed = false;
    protected boolean sessionSerializeCompressed_WasSet = false;
    protected boolean sessionExplicitSerialize = false;
    protected boolean sessionExplicitSerialize_WasSet = false;
    protected long sessionExplicitSerializeFragmentSize = SESSION_EXPLICIT_SERIALIZE_ONE_FRAGMENT; // X > 0 => tamaño en bytes del fragmento
    protected boolean sessionExplicitSerializeFragmentSize_WasSet = false;
    protected boolean configurationFrozen = false;

    /** Creates a new instance of ItsNatServletContextImpl */
    public ItsNatServletContextImpl(ItsNatImpl itsNat,ServletContext servletContext)
    {
        super(true);

        this.itsNat = itsNat;
        this.servletContext = servletContext;
    }

    public ItsNatImpl getItsNatImpl()
    {
        return itsNat;
    }

    public ItsNat getItsNat()
    {
        return itsNat;
    }

    public String getURLRootPath()
    {
        // YA NO SE USA, pero lo conservamos por si acaso en el futuro lo
        // necesitamos

        // Es usado como forma de definir de forma única el ServletContext
        // de otros ServletContext de otras aplicaciones. Util en serialización
        // aunque yo creo que es siempre un SINGLETON pues lo normal es que
        // haya un class loader por aplicación web.
        try { return servletContext.getResource("/").getPath(); }
        catch(MalformedURLException ex) { throw new ItsNatException(ex); }
    }

    public ItsNatServletContextUniqueIdGenImpl getUniqueIdGenerator()
    {
        return idGenerator;
    }

    public int getNewToken()
    {
        // Parece ser que nextInt() es multihilo, pues se basa en next(int)
        // que en la documentación se dice que está ya "synchronized" (lo cual
        // no es verdad en la implementación al usarse otra técnica que evita
        // usar synchronized, pero lo importante es que es multihilo).
        // http://java.sun.com/j2se/1.4.2/docs/api/java/util/Random.html#next(int)
        // http://www.velocityreviews.com/forums/t367261-javautilrandomnextint-thread-safety.html
        return random.nextInt(65536); // 65536 = 2^16 (es más rápido cuando es potencia de 2)
    }

    public ServletContext getServletContext()
    {
        return servletContext;
    }

    public void checkConfigChange(boolean changed,boolean wasSet)
    {
        // Esto nos sirve por un lado para congelar los valores de configuración,
        // sobre todo en el caso de que un segundo servlet cambie un valor por defecto
        // que el primer servlet no cambió pero en donde ya tenemos sesiones creadas

        // DESACTIVADO POR SI ACASO HAY SESIONES GUARDADAS QUE SE CARGAN AL ARRANCAR EL SERV.
        // ANTES DE INICIAR LOS SERVLETS LO QUE IMPEDIRÍA HACER CAMBIOS DE CONF. :
        // Por otro lado para evitar que dos servlets metan la pata y definan valores
        // diferentes pisándo el valor definido explícitamente por el otro.
        if (changed) // Cambio
        {
            /* if (configurationFrozen) throw new ItsNatException("New value tries to change the current configuration however some session has been created with current configuration");
            else */ if (wasSet) throw new ItsNatException("Incoherent new value, current configuration value cannot be changed again or maybe two servlets are defining different values");
        }
    }

    public void checkConfigChange(boolean currValue,boolean newValue,boolean wasSet)
    {
        checkConfigChange(currValue != newValue,wasSet);
    }

    public void checkConfigChange(int currValue,int newValue,boolean wasSet)
    {
        checkConfigChange(currValue != newValue,wasSet);
    }

    public void checkConfigChange(long currValue,long newValue,boolean wasSet)
    {
        checkConfigChange(currValue != newValue,wasSet);
    }

    public int getMaxOpenDocumentsBySession()
    {
        return maxOpenDocumentsBySession;
    }

    public void setMaxOpenDocumentsBySession(int maxOpenDocumentsBySession)
    {
        checkConfigChange(this.maxOpenDocumentsBySession,maxOpenDocumentsBySession,maxOpenDocumentsBySession_WasSet);

        this.maxOpenDocumentsBySession = maxOpenDocumentsBySession;
        this.maxOpenDocumentsBySession_WasSet = true;
    }

    public boolean isSessionReplicationCapable()
    {
        return sessionReplicationCapable;
    }

    public void setSessionReplicationCapable(boolean sessionReplicationCapable)
    {
        checkConfigChange(this.sessionReplicationCapable,sessionReplicationCapable,sessionReplicationCapable_WasSet);

        boolean changed = (this.sessionReplicationCapable != sessionReplicationCapable);
        this.sessionReplicationCapable = sessionReplicationCapable;
        this.sessionReplicationCapable_WasSet = true;
        if (changed) idGenerator.notifySessionReplicationCapableChanged();
    }

    public boolean isSessionSerializeCompressed()
    {
        return sessionSerializeCompressed;
    }

    public void setSessionSerializeCompressed(boolean sessionSerializeCompressed)
    {
        checkConfigChange(this.sessionSerializeCompressed,sessionSerializeCompressed,sessionSerializeCompressed_WasSet);

        this.sessionSerializeCompressed = sessionSerializeCompressed;
        this.sessionSerializeCompressed_WasSet = true;
    }

    public boolean isSessionExplicitSerialize()
    {
        return sessionExplicitSerialize;
    }

    public void setSessionExplicitSerialize(boolean sessionExplicitSerialize)
    {
        checkConfigChange(this.sessionExplicitSerialize,sessionExplicitSerialize,sessionExplicitSerialize_WasSet);

        this.sessionExplicitSerialize = sessionExplicitSerialize;
        this.sessionExplicitSerialize_WasSet = true;
    }

    // No me he decidido todavía a hacerlo público
    public long getSessionExplicitSerializeFragmentSize()
    {
        return sessionExplicitSerializeFragmentSize;
    }

    // No me he decidido todavía a hacerlo público
    public void setSessionExplicitSerializeFragmentSize(long sessionExplicitSerializeFragmentSize)
    {
        if (sessionExplicitSerializeFragmentSize < 0)
            throw new ItsNatException("Expected a positive value or cero: " + sessionExplicitSerializeFragmentSize);

        checkConfigChange(this.sessionExplicitSerializeFragmentSize,sessionExplicitSerializeFragmentSize,sessionExplicitSerializeFragmentSize_WasSet);

        this.sessionExplicitSerializeFragmentSize = sessionExplicitSerializeFragmentSize;
        this.sessionExplicitSerializeFragmentSize_WasSet = true;
    }

    public void addItsNatSession(ItsNatSessionImpl itsNatSession)
    {
        if (!configurationFrozen) this.configurationFrozen = true; // Ya hay una sesión creada, no se puede cambiar la configuración del contexto

        // Cuando se pierda la sesión original (HttpSession) se perderá la ItsNatSessionImpl
        // Object session = itsNatSession.getStandardSessionObject(); // HttpSession
        String builtInId = itsNatSession.getStandardSessionId();

        synchronized(sessionsByStandardId)
        {
            sessionsByStandardId.put(builtInId,itsNatSession);

            MapUniqueId.check(idGenerator,itsNatSession); // Comprobación paranoica porque no podemos usar MapUniqueId
            sessionsByItsNatId.put(itsNatSession.getId(),itsNatSession); // El id puesto como clave es sujetado únicamente por la itsNatSession
        }
    }

    public ItsNatSessionImpl findItsNatSessionByItsNatId(String id)
    {
        ItsNatSessionImpl itsNatSession;
        synchronized(sessionsByStandardId)
        {
            itsNatSession = sessionsByItsNatId.get(id);
        }
        return itsNatSession;
    }

    public ItsNatSessionImpl getItsNatSessionByStandardId(String id)
    {
        // Hay que reconocer que el uso de este método es un cuello de botella
        // por la sincronización global.
        ItsNatSessionImpl itsNatSession;
        synchronized(sessionsByStandardId)
        {
            itsNatSession = sessionsByStandardId.get(id);
        }
        return itsNatSession;
    }

    public void removeItsNatSession(ItsNatSessionImpl itsNatSession)
    {
        // Object session = itsNatSession.getStandardSessionObject(); // HttpSession
        String builtInId = itsNatSession.getStandardSessionId();

        synchronized(sessionsByStandardId)
        {
            sessionsByStandardId.remove(builtInId);
            sessionsByItsNatId.remove(itsNatSession.getId());
        }
    }

    public void enumerateSessions(ItsNatSessionCallback callback)
    {
        synchronized(sessionsByStandardId)
        {
            if (sessionsByStandardId.isEmpty())
                return;

            for(Map.Entry<String,ItsNatSessionImpl> entry : sessionsByStandardId.entrySet())
            {
                ItsNatSessionImpl itsNatSession = entry.getValue();
                if (itsNatSession == null) // ha sido garbage collected
                    continue;
                if (!callback.handleSession(itsNatSession))
                    break;
            }
        }
    }

    public Object getVariable(String varName)
    {
        return getServletContext().getAttribute(varName); // Puede ser null
    }

    public ItsNatVariableResolver createItsNatVariableResolver()
    {
        return new ItsNatVariableResolverImpl(null,null,null,null,this);
    }
}
