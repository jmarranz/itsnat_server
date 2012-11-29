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

package org.itsnat.impl.core.event;

import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatEventListenerChain;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.servlet.ItsNatServletResponseImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.util.UserDataMonoThreadImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatEventImpl extends EventObject implements ItsNatEvent
{
    protected UserDataMonoThreadImpl userData; // No hace falta sincronizar, deberá accederse en monohilo
    protected Map extraParams;
    protected ItsNatEventListenerChainImpl listenerChain;

    public ItsNatEventImpl(Object source)
    {
        super(source); // Recordad que NO puede ser null
    }

    public abstract ItsNatStfulDocumentImpl getItsNatStfulDocument();
    public abstract ItsNatServletRequestImpl getItsNatServletRequestImpl();

    public ItsNatDocument getItsNatDocument()
    {
        return getItsNatDocumentImpl();
    }

    public ItsNatDocumentImpl getItsNatDocumentImpl()
    {
        return getItsNatStfulDocument();
    }

    public ClientDocumentImpl getClientDocumentImpl()
    {
        return getItsNatServletRequestImpl().getClientDocumentImpl();
    }

    public ClientDocument getClientDocument()
    {
        return getClientDocumentImpl();
    }

    public ItsNatServletResponseImpl getItsNatServletResponseImpl()
    {
        ItsNatServletRequestImpl request = getItsNatServletRequestImpl();
        if (request == null)
            return null; // En el caso de ServerItsNatEventImpl puede ser null
        return request.getItsNatServletResponseImpl();
    }

    public ItsNatServletRequest getItsNatServletRequest()
    {
        return getItsNatServletRequestImpl();
    }

    public ItsNatServletResponse getItsNatServletResponse()
    {
        return getItsNatServletResponseImpl();
    }

    public UserDataMonoThreadImpl getUserData()
    {
        if (userData == null)
            this.userData = new UserDataMonoThreadImpl(); // Para ahorrar memoria si no se usa. No es necesario sincronizar pues el evento es manejado por un unico hilo
        return userData;
    }

    public boolean containsUserValueName(String name)
    {
        return getUserData().containsName(name);
    }

    public String[] getUserValueNames()
    {
        return getUserData().getUserDataNames();
    }

    public Object getUserValue(String name)
    {
        return getUserData().getUserData(name);
    }

    public Object setUserValue(String name,Object value)
    {
        return getUserData().setUserData(name,value);
    }

    public Object removeUserValue(String name)
    {
        return getUserData().removeUserData(name);
    }

    public boolean hasExtraParams()
    {
        if (extraParams == null)
            return false;
        return !extraParams.isEmpty();
    }

    public Map getExtraParamMap()
    {
        if (extraParams == null)
            this.extraParams = new HashMap();
        return extraParams;
    }

    public Object getExtraParam(String name)
    {
        if (!hasExtraParams())
            return null;
        return getExtraParamMap().get(name);
    }

    public void setExtraParam(String name,Object value)
    {
        getExtraParamMap().put(name,value);
    }

    public ItsNatEventListenerChain getItsNatEventListenerChain()
    {
        return getItsNatEventListenerChainImpl();
    }

    public ItsNatEventListenerChainImpl getItsNatEventListenerChainImpl()
    {
        return listenerChain;
    }

    public void setItsNatEventListenerChain(ItsNatEventListenerChainImpl listenerChain)
    {
        if (this.listenerChain != null) throw new ItsNatException("INTERNAL ERROR");
        this.listenerChain = listenerChain;
    }

    public void unsetEventListenerChain()
    {
        this.listenerChain = null;
    }

}
