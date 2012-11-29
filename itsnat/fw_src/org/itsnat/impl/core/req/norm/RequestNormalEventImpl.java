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

package org.itsnat.impl.core.req.norm;

import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.req.*;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.*;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentNoServerDocDefaultImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatDocSynchronizerImpl;
import org.itsnat.impl.core.resp.ResponseEventDoNothingImpl;
import org.itsnat.impl.core.resp.norm.ResponseNormal;
import org.itsnat.impl.core.resp.norm.ResponseNormalEventImpl;
import org.itsnat.impl.core.resp.norm.ResponseNormalEventErrorLostClientDocImpl;
import org.itsnat.impl.core.resp.norm.ResponseNormalEventErrorLostSessionImpl;

/**
 *
 * @author jmarranz
 */
public abstract class RequestNormalEventImpl extends RequestEventImpl implements RequestNormal
{
    protected int evtType;

    /**
     * Creates a new instance of RequestNormalEventImpl
     */
    public RequestNormalEventImpl(int evtType,ItsNatServletRequestImpl itsNatRequest)
    {
        super(itsNatRequest);

        this.evtType = evtType;
    }

    public static RequestNormalEventImpl createRequestNormalEvent(int evtType,ItsNatServletRequestImpl itsNatRequest)
    {
        switch(evtType)
        {
            case DOMSTD_EVENT:
            case TIMER_EVENT:
            case CONTINUE_EVENT:
            case USER_EVENT:
                return new RequestDOMEventDefaultImpl(evtType,itsNatRequest);

            case ASYNC_RET_EVENT:
            case COMET_RET_EVENT:
                return new RequestGenericTaskEventImpl(evtType,itsNatRequest);

            default:
                throw new ItsNatException("Malformed URL/request, bad itsnat_eventType: " + evtType);
        }
    }

    public ResponseNormal getResponseNormal()
    {
        return (ResponseNormal)response;
    }

    public ResponseNormalEventImpl getResponseNormalEvent()
    {
        return (ResponseNormalEventImpl)response;
    }

    public ClientDocumentStfulImpl getClientDocumentStfulById(String clientId)
    {
        // Puede ser un cliente owner o control remoto (no readonly)
        ItsNatSessionImpl session = getItsNatSession();
        return session.getClientDocumentStfulById(clientId);
    }

    public void processClientDocument(ClientDocumentStfulImpl clientDoc)
    {
        if (!clientDoc.canReceiveSOMENormalEvents())
        {
            // Primer chequeo de seguridad para evitar que clientes de control remoto
            // envíen eventos cuando no están autorizados. En teoría no se envió
            // código JavaScript para ello (registro de listener) pero un malicioso usuario
            // podría intentarlo enviando requests AJAX "a pelo".
            throw new ItsNatException("Security violation attempt");
        }

        String listenerId = getAttrOrParamExist("itsnat_listener_id");
        processClientDocument(listenerId,clientDoc);
    }

    public void processClientDocument(final String listenerId,final ClientDocumentStfulImpl clientDoc)
    {
        ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();

        ItsNatDocSynchronizerImpl syncTask = new ItsNatDocSynchronizerImpl()
        {
            protected void syncMethod()
            {
                processClientDocumentThreadSync(listenerId,clientDoc);
            }
        };
        syncTask.exec(itsNatDoc);
    }

    public void processClientDocumentThreadSync(String listenerId,ClientDocumentStfulImpl clientDoc)
    {
        bindClientToRequest(clientDoc);

        try
        {
            this.response = createResponseNormalEvent(listenerId,clientDoc);

            if (response != null)
                response.process();
        }
        finally
        {
            unbindRequestFromDocument();
        }
    }

    public abstract ResponseNormalEventImpl createResponseNormalEvent(String listenerId,ClientDocumentStfulImpl clientDoc);

    public void processLostSessionOrClientUnloading()
    {
        // No hacer nada
        ItsNatSessionImpl session = getItsNatSession();
        ClientDocumentNoServerDocDefaultImpl clientDoc = new ClientDocumentNoServerDocDefaultImpl(session);

        bindClientToRequest(clientDoc,false);  // El documento es nulo, no se vincula por tanto

        this.response = new ResponseEventDoNothingImpl(this);
        response.process();
    }

    public void processClientDocumentNotFoundError(String clientId)
    {
        ItsNatSessionImpl session = getItsNatSession();
        ClientDocumentNoServerDocDefaultImpl clientDoc = new ClientDocumentNoServerDocDefaultImpl(session);

        bindClientToRequest(clientDoc,false);  // El documento es nulo, por tanto no se vincula el request al doc

        this.response = new ResponseNormalEventErrorLostClientDocImpl(this,clientId);
        response.process();
    }

    public void processLostSessionError(String sessionId,String sessionToken)
    {
        ItsNatSessionImpl session = getItsNatSession();
        ClientDocumentNoServerDocDefaultImpl clientDoc = new ClientDocumentNoServerDocDefaultImpl(session);

        bindClientToRequest(clientDoc,false);  // El documento es nulo, por tanto no se vincula el request al doc

        this.response = new ResponseNormalEventErrorLostSessionImpl(this);
        response.process();
    }

    public abstract boolean isLoadEvent();
}
