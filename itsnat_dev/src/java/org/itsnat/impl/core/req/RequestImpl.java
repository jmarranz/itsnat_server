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

import org.itsnat.impl.core.req.attachsrv.RequestAttachedServerImpl;
import org.itsnat.impl.core.req.script.RequestLoadScriptImpl;
import org.itsnat.impl.core.req.norm.RequestIFrameFileUploadImpl;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.req.norm.RequestNormalLoadDocBaseImpl;
import org.itsnat.impl.core.req.attachcli.RequestAttachedClientLoadDocImpl;
import org.itsnat.impl.core.resp.ResponseImpl;

/**
 *
 * @author jmarranz
 */
public abstract class RequestImpl
{
    protected ItsNatServletRequestImpl itsNatRequest;
    protected ResponseImpl response;
    protected ClientDocumentImpl clientDoc; // Puede ser nulo

    /** Creates a new instance of RequestImpl */
    public RequestImpl(ItsNatServletRequestImpl itsNatRequest)
    {
        this.itsNatRequest = itsNatRequest;
    }

    public static RequestImpl createRequest(String action,ItsNatServletRequestImpl itsNatRequest)
    {
        if (action != null)
        {
            if (action.equals("event"))
                return RequestEventImpl.createRequestEvent(itsNatRequest);
            else if (action.equals("load_script"))
                return RequestLoadScriptImpl.createRequestLoadScript(itsNatRequest);
            else if (action.equals("iframe_file_upload"))
                return RequestIFrameFileUploadImpl.createRequestIFrameFileUpload(itsNatRequest);
            else if (action.equals("attach_client"))
                return RequestAttachedClientLoadDocImpl.createRequestAttachedClientLoadDoc(itsNatRequest);
            else if (action.equals("attach_server"))
                return RequestAttachedServerImpl.createRequestAttachedServer(itsNatRequest);
            else if (action.equals("itsnat_info"))
                return RequestItsNatInfoImpl.createRequestItsNatInfo(itsNatRequest);
            else
                throw new ItsNatException("Unrecognized itsnat_action: \"" + action + "\"");
        }
        else
        {
            String docName = itsNatRequest.getAttrOrParam("itsnat_doc_name");
            if (docName != null)
                return RequestNormalLoadDocBaseImpl.createRequestNormalLoadDocBase(docName,itsNatRequest);
            else
                return RequestCustomImpl.createRequestCustom(itsNatRequest);
        }
    }

    public ItsNatSessionImpl getItsNatSession()
    {
        return itsNatRequest.getItsNatSessionImpl();
    }

    public ItsNatServletRequestImpl getItsNatServletRequest()
    {
        return itsNatRequest;
    }

    public ResponseImpl getResponse()
    {
        return response;
    }

    public void setResponse(ResponseImpl response)
    {
        this.response = response;
    }

    public abstract ItsNatStfulDocumentImpl getItsNatStfulDocumentReferrer();

    
    public String getAttrOrParam(String name)
    {
        return itsNatRequest.getAttrOrParam(name);
    }

    public String getAttrOrParamExist(String name)
    {
        return itsNatRequest.getAttrOrParamExist(name);
    }

    public ItsNatDocumentImpl getItsNatDocument()
    {
        if (clientDoc != null) // Prácticamente en todos los casos es no nulo pero por si acaso (en el caso de request custom es nulo por ejemplo)
            return clientDoc.getItsNatDocumentImpl();
        else
            return null;
    }

    public ClientDocumentImpl getClientDocument()
    {
        return clientDoc; // NUNCA es null si se ha hecho el "binding" 
    }

    public void bindClientToRequest(ClientDocumentImpl clientDoc)
    {
        bindClientToRequest(clientDoc,true);
    }

    public void bindClientToRequest(ClientDocumentImpl clientDoc,boolean bindReqToDoc)
    {
        this.clientDoc = clientDoc;
        if (bindReqToDoc) itsNatRequest.bindRequestToDocument(clientDoc.getItsNatDocumentImpl());
    }

    public void unbindRequestFromDocument()
    {
        itsNatRequest.unbindRequestFromDocument();
    }

    public void process()
    {
        try
        {
            processRequest();
        }
        finally
        {
            // Post processRequest
            notifyEndOfRequestToSession();

            // Aprovechamos el request para hacer limpieza
            ItsNatSessionImpl itsNatSession = itsNatRequest.getItsNatSessionImpl();
            itsNatSession.invalidateLostResources();
        }
    }


    // Devolver false cuando podamos evitar serializaciones de sesión seguidas algunas de ellas inútiles
    // cuando el request no sea ni un request de carga ni un evento normal
    // y por tanto no se ha ejecutado código del usuario que cambie el objeto sesión ItsNat.
    protected abstract boolean isMustNotifyEndOfRequestToSession();

    public void notifyEndOfRequestToSession()
    {
        // La finalidad de esta llamada es que se serialicen en la sesión los cambios que hayamos
        // hecho en el ItsNatDocument de trabajo en el caso de session replication capable
        // activado (ej. para GAE)
        if (isMustNotifyEndOfRequestToSession())
        {
            ItsNatSessionImpl itsNatSession = itsNatRequest.getItsNatSessionImpl();
            itsNatSession.endOfRequest();
        }
    }

    public abstract void processRequest();
}
