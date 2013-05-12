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
import org.itsnat.impl.core.clientdoc.ClientDocumentWithoutDocumentDefaultImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import static org.itsnat.impl.core.req.RequestImpl.ITSNAT_ACTION_EVENT;
import static org.itsnat.impl.core.req.RequestImpl.ITSNAT_ACTION_EVENT_STATELESS_PHASE_LOAD_DOC;
import org.itsnat.impl.core.resp.ResponseEventStatelessCustomAndDocTemplateNotFoundImpl;
import org.itsnat.impl.core.servlet.ItsNatServletImpl;
import org.itsnat.impl.core.servlet.ItsNatServletResponseImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;

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
        ItsNatServletRequestImpl itsNatRequest = getItsNatServletRequest();
              
        String docName = itsNatRequest.getAttrOrParam("itsnat_doc_name");        
        if (docName != null)
        {
             processDocumentTemplateSpecified();
        }
        else
        {
            // Segunda oportunidad de definir el itsnat_doc_name en un ItsNatServletRequestListener, de esta manera podemos usar el global event listener a modo de dispatcher, aunque hayamos generado código JavaScript recuerda que al final el resultado de ésto es también JavaScript
            ItsNatServletImpl itsNatServlet = itsNatRequest.getItsNatServletImpl();
            itsNatServlet.dispatchItsNatServletRequestListeners(itsNatRequest);            
            
            docName = itsNatRequest.getAttrOrParam("itsnat_doc_name");        
            if (docName != null)
            {
                 processDocumentTemplateSpecified();
            }             
            else
            {
                processCustom();
            }
        }
    }

    protected void processDocumentTemplateSpecified()
    {
        ItsNatServletImpl itsNatServlet = itsNatRequest.getItsNatServletImpl();            
        ItsNatServletResponseImpl itsNatResponse = itsNatRequest.getItsNatServletResponseImpl();               
        ServletRequest request = itsNatRequest.getServletRequest();
        ServletResponse response = itsNatResponse.getServletResponse();             

        request.setAttribute("itsnat_action",ITSNAT_ACTION_EVENT_STATELESS_PHASE_LOAD_DOC);            
        ItsNatServletRequestImpl itsNatRequestLoadPhase = itsNatServlet.processRequestInternal(request,response,null);
        ClientDocumentImpl clientDoc = itsNatRequestLoadPhase.getClientDocumentImpl();        
        if (clientDoc instanceof ClientDocumentStfulImpl) 
        {
            // No necesitamos un ResponseEventStatelessImpl

            request.setAttribute("itsnat_action",ITSNAT_ACTION_EVENT);
            request.setAttribute("itsnat_eventType","stateless");           

            ItsNatServletRequestImpl itsNatRequestEventPhase = itsNatServlet.processRequestInternal(request,response,(ClientDocumentStfulImpl)clientDoc);
        }
        else
        {
            // Puede ser el caso de ClientDocumentWithoutDocumentDefaultImpl
            processDocumentTemplateNotFound(clientDoc);
        }        
    }
    
    protected void processCustom()
    {
        ItsNatSessionImpl session = getItsNatSession();
        ClientDocumentWithoutDocumentDefaultImpl clientDoc = new ClientDocumentWithoutDocumentDefaultImpl(session);

        bindClientToRequest(clientDoc,false);  // El documento es nulo, por tanto no se vincula el request al doc

        this.response = new ResponseEventStatelessCustomAndDocTemplateNotFoundImpl(this);
        response.process();      
    }
    
    public void processDocumentTemplateNotFound(ClientDocumentImpl clientDoc)    
    {
        bindClientToRequest(clientDoc,false);  // El documento es nulo, por tanto no se vincula el request al doc

        this.response = new ResponseEventStatelessCustomAndDocTemplateNotFoundImpl(this);
        response.process(); 
    }    
    
    protected boolean isMustNotifyEndOfRequestToSession()
    {
        // Así nos ahorramos una serialización inútil, estamos en stateless
        return false;
    }
    
    public int getCommMode()
    {
        return getCommMode(getItsNatServletRequest());     
    }    
    
    public int getEventTimeout()
    {
        return getEventTimeout(getItsNatServletRequest());     
    }        
    
    public static int getCommMode(ItsNatServletRequestImpl itsNatRequest)
    {
        return Integer.parseInt(itsNatRequest.getAttrOrParamExist("itsnat_commMode"));     
    }
    
    public static int getEventTimeout(ItsNatServletRequestImpl itsNatRequest)
    {
        return Integer.parseInt(itsNatRequest.getAttrOrParamExist("itsnat_eventTimeout"));      
    }    
}
