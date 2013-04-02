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

package org.itsnat.impl.core.req;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import static org.itsnat.impl.core.req.RequestImpl.ITSNAT_ACTION_EVENT;
import org.itsnat.impl.core.servlet.ItsNatServletImpl;
import org.itsnat.impl.core.servlet.ItsNatServletResponseImpl;

/**
 *
 * @author jmarranz
 */
public class RequestEventStatelessImpl extends RequestImpl
{
    /**
     * Creates a new instance of RequestNormalLoadDocImpl
     */
    public RequestEventStatelessImpl(ItsNatServletRequestImpl itsNatRequest)
    {
        super(itsNatRequest);
    }

    public static RequestEventStatelessImpl createRequestEventStateless(ItsNatServletRequestImpl itsNatRequest)
    {
        return new RequestEventStatelessImpl(itsNatRequest);
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocumentReferrer()
    {
        // Aunque sea la carga de una página, no hay listeners que pudieran aprovechar el referrer
        return null;
    }

    @Override    
    public void processRequest(ClientDocumentStfulImpl clientDocStateless)
    {
        ItsNatServletImpl itsNatServlet = itsNatRequest.getItsNatServletImpl();
        ItsNatServletRequestImpl itsNatRequest = getItsNatServletRequest();
        ItsNatServletResponseImpl itsNatResponse = itsNatRequest.getItsNatServletResponseImpl();               
        ServletRequest request = itsNatRequest.getServletRequest();
        ServletResponse response = itsNatResponse.getServletResponse();        
        
        request.setAttribute("itsnat_action",ITSNAT_ACTION_EVENT_STATELESS_PHASE_LOAD);
        ItsNatServletRequestImpl itsNatRequestLoadPhase = itsNatServlet.processRequestInternal(request,response,null);
        ClientDocumentImpl clientDoc = itsNatRequestLoadPhase.getClientDocumentImpl();        
        if (clientDoc != null && clientDoc instanceof ClientDocumentStfulImpl) 
        {
            // No necesitamos un ResponseEventStatelessImpl
            
            request.setAttribute("itsnat_action",ITSNAT_ACTION_EVENT);
            request.setAttribute("itsnat_eventType","stateless");           
            
            ItsNatServletRequestImpl itsNatRequestEventPhase = itsNatServlet.processRequestInternal(request,response,(ClientDocumentStfulImpl)clientDoc);
        }
        else
        {
            // Puede ser el caso de ClientDocumentNoServerDocDefaultImpl
            processClientDocumentNotCreated();
        }
    }

    public void processClientDocumentNotCreated()    
    {
        // HACER
    }    
    
    protected boolean isMustNotifyEndOfRequestToSession()
    {
        // Así nos ahorramos una serialización inútil, estamos en stateless
        return false;
    }
}
