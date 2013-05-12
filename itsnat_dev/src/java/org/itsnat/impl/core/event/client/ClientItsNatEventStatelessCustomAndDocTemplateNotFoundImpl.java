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

import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ItsNatEventStateless;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.event.ItsNatEventImpl;
import org.itsnat.impl.core.req.RequestEventStatelessImpl;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class ClientItsNatEventStatelessCustomAndDocTemplateNotFoundImpl extends ItsNatEventImpl implements ItsNatEventStateless
{
    /**
     * Creates a new instance of ClientItsNatEventStfulImpl
     */
    public ClientItsNatEventStatelessCustomAndDocTemplateNotFoundImpl(RequestEventStatelessImpl request)
    {
        super(request);
    }

    public RequestEventStatelessImpl getRequestEventStateless()
    {
        return (RequestEventStatelessImpl)getSource();        
    }
    
    public int getCommMode()
    {
        return getRequestEventStateless().getCommMode();
    }

    public ItsNatServletRequestImpl getItsNatServletRequestImpl()
    {
        return getRequestEventStateless().getItsNatServletRequest();
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
    
    @Override
    public ItsNatStfulDocumentImpl getItsNatStfulDocument() 
    {
        return null;
    }

    public long getTimeStamp()
    {
        throw new ItsNatException("Not supported in this context");
    }

    public short getEventPhase()
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void preventDefault()
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void stopPropagation()
    {
        throw new ItsNatException("Not supported in this context");
    }

    public boolean getBubbles()
    {
        throw new ItsNatException("Not supported in this context");
    }

    public boolean getCancelable()
    {
        throw new ItsNatException("Not supported in this context");
    }

    public String getType()
    {
        throw new ItsNatException("Not supported in this context");
    }

    public void initEvent(String eventTypeArg, boolean canBubbleArg, boolean cancelableArg)
    {
        throw new ItsNatException("Not supported in this context");
    }

    public EventTarget getCurrentTarget()
    {
        throw new ItsNatException("Not supported in this context");
    }

    public EventTarget getTarget()
    {
        throw new ItsNatException("Not supported in this context");
    }
    
}
