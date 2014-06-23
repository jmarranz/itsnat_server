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

package org.itsnat.impl.core.resp;

import org.itsnat.impl.core.event.client.ClientItsNatEventStatelessCustomAndDocTemplateNotFoundImpl;
import org.itsnat.impl.core.listener.EventListenerUtil;
import org.itsnat.impl.core.req.RequestEventStatelessImpl;
import org.itsnat.impl.core.servlet.ItsNatServletImpl;

/**
 *
 * @author jmarranz
 */
public class ResponseEventStatelessCustomAndDocTemplateNotFoundImpl extends ResponseImpl implements ResponseScript
{
    protected ResponseEventDelegateImpl delegate;
    
    /** Creates a new instance of ResponseNormalLoadDocImpl */
    public ResponseEventStatelessCustomAndDocTemplateNotFoundImpl(RequestEventStatelessImpl request)
    {
        super(request);
        
        this.delegate = new ResponseEventDelegateImpl(this);        
    }

    public RequestEventStatelessImpl getRequestEventStatelessImpl()
    {
        return (RequestEventStatelessImpl)request;
    }

    public boolean isLoadByScriptElement()
    {
        return true;
    }

    @Override    
    protected void processResponse()
    {
        try
        {
            processEvent();

            delegate.sendPendingCode();
        }
        catch(RuntimeException ex)
        {
            delegate.processResponseOnException(ex);
        }
    }

    protected void processEvent() 
    {
        RequestEventStatelessImpl request = getRequestEventStatelessImpl();
        ItsNatServletImpl itsNatServlet = request.getItsNatServletRequest().getItsNatServletImpl();
            
        if (itsNatServlet.hasGlobalEventListenerListeners()) // No miramos más pues sabemos que no hay documento y el ClientDocument es temporal y el programador no tuvo la oportunidad de registrar listeners 
        {
            ClientItsNatEventStatelessCustomAndDocTemplateNotFoundImpl evt = new ClientItsNatEventStatelessCustomAndDocTemplateNotFoundImpl(request);
            EventListenerUtil.handleEventIncludingGlobalListeners(null,evt);
        } 
    }    
}
