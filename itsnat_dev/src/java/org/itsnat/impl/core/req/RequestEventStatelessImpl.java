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
import org.itsnat.impl.core.resp.ResponseEventStatelessImpl_ELIMINAR;
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

    public void processRequest(ClientDocumentImpl clientDocStateless)
    {
        ItsNatServletImpl itsNatServlet = itsNatRequest.getItsNatServletImpl();
        ItsNatServletRequestImpl itsNatRequest = getItsNatServletRequest();
        ItsNatServletResponseImpl itsNatResponse = itsNatRequest.getItsNatServletResponseImpl();               
        ServletRequest request = itsNatRequest.getServletRequest();
        ServletResponse response = itsNatResponse.getServletResponse();        
        
        request.setAttribute("itsnat_action",ITSNAT_ACTION_EVENT_STATELESS_PHASE_LOAD);
        ItsNatServletRequestImpl itsNatRequestLoadPhase = itsNatServlet.processRequestInternal(request,response,null);
        ClientDocumentStfulImpl clientDoc = (ClientDocumentStfulImpl)itsNatRequestLoadPhase.getClientDocumentImpl();
        if (clientDoc != null) 
        {
            clientDoc.getNodeCacheRegistry().clearCache();
            
            request.setAttribute("itsnat_action",ITSNAT_ACTION_EVENT);
            request.setAttribute("itsnat_eventType","stateless");            
            
            ItsNatServletRequestImpl itsNatRequestEventPhase = itsNatServlet.processRequestInternal(request,response,clientDoc);            
        }
        else
            processClientDocumentNotCreated();
    }

    public void processClientDocument_ELIMINAR(ClientDocumentImpl clientDoc)    
    {
        // No hace falta sincronizar el ItsNatDocument porque se ha creado nuevo para procesar el evento
        
        bindClientToRequest(clientDoc);

        try
        {
            ItsNatServletImpl itsNatServlet = itsNatRequest.getItsNatServletImpl();            
            
            
            this.response = new ResponseEventStatelessImpl_ELIMINAR(this);
            response.process();
        }
        finally
        {
            unbindRequestFromDocument();
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
