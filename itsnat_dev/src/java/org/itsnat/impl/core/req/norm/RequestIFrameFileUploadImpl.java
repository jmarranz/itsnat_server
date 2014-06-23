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

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.comp.iframe.HTMLIFrameFileUploadImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentWithoutDocumentDefaultImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatDocSynchronizerImpl;
import org.itsnat.impl.core.req.RequestAlreadyLoadedDocImpl;
import org.itsnat.impl.core.resp.norm.ResponseIFrameFileUploadErrorLostClientDocImpl;
import org.itsnat.impl.core.resp.norm.ResponseIFrameFileUploadErrorLostSessionImpl;
import org.itsnat.impl.core.resp.norm.ResponseIFrameFileUploadImpl;
import org.itsnat.impl.core.resp.norm.ResponseNormal;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;

/**
 *
 * @author jmarranz
 */
public class RequestIFrameFileUploadImpl extends RequestAlreadyLoadedDocImpl implements RequestNormal
{
    public RequestIFrameFileUploadImpl(ItsNatServletRequestImpl itsNatRequest)
    {
        super(itsNatRequest);
    }

    public static RequestIFrameFileUploadImpl createRequestIFrameFileUpload(ItsNatServletRequestImpl itsNatRequest)
    {
        // Sólo requests normales
        return new RequestIFrameFileUploadImpl(itsNatRequest);
    }

    public ResponseNormal getResponseNormal()
    {
        return (ResponseNormal)response;
    }

    public ClientDocumentStfulImpl getClientDocumentStfulById(String clientId)
    {
        ItsNatSessionImpl session = getItsNatSession();
        return session.getClientDocumentStfulById(clientId);
    }

    public void processClientDocument(final ClientDocumentStfulImpl clientDoc)
    {
        if (!clientDoc.canReceiveSOMENormalEvents())
        {
            // Aunque un request file upload no es estrictamente un evento
            // lo consideramos como una acción efectuada desde el cliente
            // por lo que no aceptamos el request si por ejemplo viene
            // de un cliente attached read only
            throw new ItsNatException("Security violation attempt");
        }

        final String listenerId = getAttrOrParamExist("itsnat_listener_id");

        // NO registramos el request en el documento porque el file upload es algo que
        // hacemos de forma paralela al proceso normal de la web por lo que la aplicación
        // podrá seguir recibiendo eventos.
        // Por ello tampoco sincronizamos el documento pues obligaría a que el servidor quedara
        // bloqueado hasta que se subiera el archivo, si el programador quiere podrá
        // sincronizar (bloquear) el documento en el proceso del file upload.

        bindClientToRequest(clientDoc,false); // ¡¡ DEBE SER false el segundo parámetro !!

        final HTMLIFrameFileUploadImpl[] listener = new HTMLIFrameFileUploadImpl[1];

        ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();

        // Esta sincronización es solamente para obtener el HTMLIFrameFileUploadImpl
        // a partir del listenerId
        ItsNatDocSynchronizerImpl syncTask = new ItsNatDocSynchronizerImpl()
        {
            protected void syncMethod()
            {
                ClientDocumentStfulDelegateWebImpl clientDocDeleg = (ClientDocumentStfulDelegateWebImpl)clientDoc.getClientDocumentStfulDelegate();
                listener[0] = clientDocDeleg.getHTMLIFrameFileUploadImpl(listenerId);
            }
        };
        syncTask.exec(itsNatDoc);

        if (listener[0] == null)
        {
            // Este caso es muy raro, hemos eliminado el listener tras la petición de file upload,
            // no hacemos nada de la misma manera que ignoramos los requests en otros sitios
            // que han perdido el listener, la página del <iframe> saldrá vacía.
            return;
        }

        this.response = new ResponseIFrameFileUploadImpl(this,listener[0]);
        response.process();

        // NO llamar pues no hemos vinculado este request:  unbindRequestFromDocument();
        // por ello el try/finally no es necesario

    }

    public void processClientDocumentNotFound(String clientId)
    {
        ItsNatSessionImpl session = getItsNatSession();
        ClientDocumentWithoutDocumentDefaultImpl clientDoc = new ClientDocumentWithoutDocumentDefaultImpl(session);

        bindClientToRequest(clientDoc,false);  // El documento es nulo, por tanto no se vincula el request al doc

        this.response = new ResponseIFrameFileUploadErrorLostClientDocImpl(this,clientId);
        response.process();
    }

    public void processLostSession(String sessionId,String sessionToken)
    {
        ItsNatSessionImpl session = getItsNatSession();
        ClientDocumentWithoutDocumentDefaultImpl clientDoc = new ClientDocumentWithoutDocumentDefaultImpl(session);

        bindClientToRequest(clientDoc,false);  // El documento es nulo, por tanto no se vincula el request al doc

        this.response = new ResponseIFrameFileUploadErrorLostSessionImpl(sessionId,sessionToken,this);
        response.process();
    }

}
