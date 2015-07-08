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

package org.itsnat.impl.core.event.client;

import javax.servlet.ServletRequest;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.CommModeImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.event.ItsNatEventImpl;
import org.itsnat.impl.core.req.RequestStfulDocument;
import org.itsnat.impl.core.req.RequestImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ClientItsNatEventStfulImpl extends ItsNatEventImpl
{
    /**
     * Creates a new instance of ClientItsNatEventStfulImpl
     */
    public ClientItsNatEventStfulImpl(RequestStfulDocument request)
    {
        super(request);
    }

    @Override
    public int getCommMode()
    {
        return CommModeImpl.getCommMode(getCommModeDeclared());
    }

    public abstract int getCommModeDeclared();
    
    public RequestStfulDocument getRequestStfulDocument()
    {
        return (RequestStfulDocument)getSource();
    }

    @Override    
    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return getRequestStfulDocument().getItsNatStfulDocument();
    }

    public RequestImpl getRequest()
    {
        return (RequestImpl)getRequestStfulDocument();
    }

    public ServletRequest getServletRequest()
    {
        return getItsNatServletRequestImpl().getServletRequest();
    }

    public ItsNatServletRequestImpl getItsNatServletRequestImpl()
    {
        return getRequest().getItsNatServletRequest();
    }

    @Override
    public Object getExtraParam(String name)
    {
        Object value = super.getExtraParam(name);
        if (value != null)
            return value;
        return getItsNatServletRequestImpl().getServletRequest().getParameter(name);
    }

    @Override
    public Object[] getExtraParamMultiple(String name)
    {
        Object[] value = super.getExtraParamMultiple(name);
        if (value != null)
            return value;
        return getItsNatServletRequestImpl().getServletRequest().getParameterValues(name);
    }
}
