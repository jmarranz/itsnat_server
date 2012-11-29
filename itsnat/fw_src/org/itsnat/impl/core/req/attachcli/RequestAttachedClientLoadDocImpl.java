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

package org.itsnat.impl.core.req.attachcli;

import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.servlet.ItsNatServletContextImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.CommMode;
import org.itsnat.core.event.ItsNatAttachedClientEvent;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientCometImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientErrorImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientNotRefreshImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientTimerImpl;
import org.itsnat.impl.core.doc.BoundElementDocContainerImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatDocSynchronizerImpl;
import org.itsnat.impl.core.event.client.ItsNatAttachedClientEventCometImpl;
import org.itsnat.impl.core.event.client.ItsNatAttachedClientEventImpl;
import org.itsnat.impl.core.event.client.ItsNatAttachedClientEventNotRefreshImpl;
import org.itsnat.impl.core.event.client.ItsNatAttachedClientEventTimerImpl;
import org.itsnat.impl.core.listener.ItsNatAttachedClientEventListenerUtil;
import org.itsnat.impl.core.req.RequestLoadDocImpl;
import org.itsnat.impl.core.req.shared.RequestDelegateLoadDocImpl;
import org.itsnat.impl.core.resp.attachcli.ResponseAttachedClient;
import org.itsnat.impl.core.resp.attachcli.ResponseAttachedClientLoadDocErrorNotDocImpl;
import org.itsnat.impl.core.resp.attachcli.ResponseAttachedClientLoadDocErrorNotDocInContainerImpl;
import org.itsnat.impl.core.resp.attachcli.ResponseAttachedClientLoadDocImpl;
import org.itsnat.impl.core.resp.attachcli.ResponseAttachedClientLoadDocErrorNotSessionImpl;
import org.itsnat.impl.core.resp.attachcli.ResponseAttachedClientLoadDocErrorParentElementNotFoundImpl;
import org.itsnat.impl.core.util.MiscUtil;

/**
 *
 * @author jmarranz
 */
public class RequestAttachedClientLoadDocImpl extends RequestLoadDocImpl implements RequestAttachedClient
{
    protected RequestDelegateLoadDocImpl delegate;

    /**
     * Creates a new instance of RequestAttachedClientLoadDocImpl
     */
    public RequestAttachedClientLoadDocImpl(ItsNatServletRequestImpl itsNatRequest)
    {
        super(itsNatRequest);

        this.delegate = new RequestDelegateLoadDocImpl(this);
    }

    public static RequestAttachedClientLoadDocImpl createRequestAttachedClientLoadDoc(ItsNatServletRequestImpl itsNatRequest)
    {
        return new RequestAttachedClientLoadDocImpl(itsNatRequest);
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocumentReferrer()
    {
        // El referrer sólo tiene sentido en tiempo de carga de una página para que tenga acceso a la anterior
        return null;
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return (ItsNatStfulDocumentImpl)getItsNatDocument();
    }

    public ResponseAttachedClient getResponseAttachedClient()
    {
        return (ResponseAttachedClient)response;
    }

    public ResponseAttachedClientLoadDocImpl getResponseAttachedClientLoadDoc()
    {
        return (ResponseAttachedClientLoadDocImpl)response;
    }

    public ClientDocumentAttachedClientImpl getParentClientDocumentAttachedClient()
    {
        ItsNatSessionImpl sessionParent = delegate.getBoundParentItsNatSession();
        if (sessionParent == null) return null;

        String clientParentId = getAttrOrParam("itsnat_client_parent_id");
        if (clientParentId == null) return null; // No tiene padre.
        return sessionParent.getClientDocumentAttachedClientById(clientParentId);
    }

    public void processRequest()
    {
        // Procesamos el request suministrado en un URL

        ItsNatStfulDocumentImpl itsNatDoc = null;
        final ClientDocumentAttachedClientImpl clientDoc;

        // Vemos si este request proviene de un iframe/object/embed/applet
        ClientDocumentAttachedClientImpl parentClientDoc = getParentClientDocumentAttachedClient();
        if (parentClientDoc != null) // Sí
        {
            ItsNatStfulDocumentImpl parentItsNatDoc = parentClientDoc.getItsNatStfulDocument();

            BoundElementDocContainerImpl bindInfo = delegate.getBoundElementDocContainer(parentItsNatDoc);
            if (bindInfo == null)
            {
                processParentElementNotFound();
                return;
            }

            // Obtenemos el documento a través del documento padre en un caso de binding a través de iframe/object
            // Si devuelve null es que todavía no se ha cargado por parte del iframe/object cliente owner.
            itsNatDoc = (ItsNatStfulDocumentImpl)bindInfo.getContentItsNatDocument();

            // El timeout NO es necesario pasarlo como parámetro pues
            // ya lo tenemos aquí, en el cliente.
            long timeout = parentClientDoc.getWaitDocTimeout();

            if ((itsNatDoc == null)&&(timeout > 0)) // Podríamos considerar timeout = -1 como espera indefinida pero es peligroso permitir esto como parámetro "libre". Consideramos el valor 0 como "no esperar"
            {
                // Probamos en intervalos de tiempo hasta el timeout, pero no
                // en intervalos regulares sino en intervalos más cortos al ppio
                // y más largos al final, a partir de un intervalo inicial
                // esperar sucesivamente algo más (el doble sucesivamente es demasiado rápido
                // al ppio y demasiado lento al final pues es una progresión geométrica):
                // t + 2t + 3t + 4t ... = timeout => (1+2+3+4...)t = timeout
                // Si an = a1 + (n ? 1)d (donde a1 = 1, d = 1) la suma
                // de n términos es: n*(a1+an)/2 => n(1+n)/2
                // http://es.wikipedia.org/wiki/Progresi%C3%B3n_aritm%C3%A9tica
                // Podemos considerar n=50 intentos => 50(1+50)/2 = 1275
                // => 1275 * t = timeout => t = timeout / 1275

                long lapse = 0;
                long t = timeout / 1275; // División entera
                if (t == 0) t = 1; // 1 ms el mínimo
                for(int i = 1; i <= 50; i++)
                {
                    try { Thread.sleep(t); } catch(Exception ex) { throw new ItsNatException(ex); }

                    itsNatDoc = (ItsNatStfulDocumentImpl)bindInfo.getContentItsNatDocument();
                    if (itsNatDoc != null) break;

                    lapse = lapse + t;
                    if (lapse >= timeout) break;

                    t = ((i+1) * timeout) / 1275;
                    if (t == 0) t = 1; // 1 ms el mínimo
                }
            }

            if ((itsNatDoc == null) || itsNatDoc.isInvalid()) // El caso itsNatDoc.isInvalid() devolviendo true es muy raro porque al invalidarse se desregistra de su sesión pero puede ocurrir, así tenemos la seguridad de que la request se realiza sobre un documento válido
            {
                processChildDocInDocContainerNotFound();
                return;
            }

            // Las propiedades de attachment las heredamos del cliente padre
            String refreshMethod = parentClientDoc.getRefreshMethod();
            int refreshInterval = -1;
            if (parentClientDoc instanceof ClientDocumentAttachedClientTimerImpl)
                refreshInterval = ((ClientDocumentAttachedClientTimerImpl)parentClientDoc).getRefreshInterval();
            int commMode = parentClientDoc.getCommModeDeclared();
            long eventTimeout = parentClientDoc.getEventTimeout();
            long waitDocTimeout = parentClientDoc.getWaitDocTimeout();
            boolean readOnly = parentClientDoc.isReadOnly();

            clientDoc = requestAuthorization(itsNatDoc,refreshMethod,refreshInterval,commMode,eventTimeout,waitDocTimeout,readOnly);
        }
        else
        {
            // La sesión que se quiere monitorizar no tiene porqué ser la misma del monitorizador
            String targetSessionId = getAttrOrParamExist("itsnat_session_id"); // Debe existir (sino da error)
            ItsNatServletContextImpl context = itsNatRequest.getItsNatServletContext();
            ItsNatSessionImpl targetSession = context.findItsNatSessionByItsNatId(targetSessionId);
            if (targetSession == null)
            {
                processTargetSessionNotFound(targetSessionId);
                return;
            }

            // El documento a monitorizar está registrado en la sesión remota:
            String targetDocId = getAttrOrParamExist("itsnat_doc_id"); // Debe existir (sino da error)
            itsNatDoc = targetSession.getItsNatStfulDocumentById(targetDocId);
            if ((itsNatDoc == null) || itsNatDoc.isInvalid()) // El caso itsNatDoc.isInvalid() devolviendo true es muy raro porque al invalidarse se desregistra de su sesión pero puede ocurrir, así tenemos la seguridad de que la request se realiza sobre un documento válido
            {
                processTargetDocumentNotFound(targetSessionId,targetDocId);
                return;
            }

            clientDoc = requestAuthorization(itsNatDoc);
        }

        if (clientDoc == null) return; // Autorización denegada

        ItsNatDocSynchronizerImpl syncTask = new ItsNatDocSynchronizerImpl()
        {
            protected void syncMethod()
            {
                processThreadSync(clientDoc);
            }
        };
        syncTask.exec(itsNatDoc);
    }

    public void processThreadSync(ClientDocumentAttachedClientImpl clientDoc)
    {
        ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();

        if (itsNatDoc.isInvalid()) // Así tenemos la seguridad completa de que la request se realiza sobre un documento válido (pues el doc está bloqueado y ya no puede invalidarse desde otro hilo)
        {
            String targetSessionId = itsNatDoc.getClientDocumentOwnerImpl().getItsNatSessionImpl().getId();
            String targetDocId = itsNatDoc.getId();
            processTargetDocumentNotFound(targetSessionId,targetDocId);
            return;
        }

        try
        {
            bindClientToRequest(clientDoc);

            clientDoc.setPhase(ItsNatAttachedClientEvent.LOAD);

            try
            {
                this.response = ResponseAttachedClientLoadDocImpl.createResponseAttachedClientLoadDoc(this);
                response.process();

                // Salvo que se lanzara una excepción estamos obligados a
                // servir el documento aunque el proceso del evento LOAD
                // haya supuesto la invalidación del documento (=>observador inválido)

                // Pero si por alguna rara los listeners han "rechazado" el evento (setAccepted(false))
                // el cliente estará invalidado, en este caso estamos OBLIGADOS a desregistrar el cliente remoto
                // El resultado es que se sirve la página pero no se refrescará, el listener tiene la oportunidad de informar al usuario si quiere.
                // Una utilidad de este caso sería la de "ver" la página del usuario remoto
                // aunque no se tenga intención de monitorizarla.
                if (clientDoc.isInvalid())
                {
                    clientDoc.setPhase(ItsNatAttachedClientEvent.UNLOAD); // Da igual realmente
                    clientDoc.invalidateAndUnregister(); // Desregistra de la sesión (lo que falta)
                }
                else
                    clientDoc.setPhase(ItsNatAttachedClientEvent.REFRESH);
            }
            catch(RuntimeException ex)
            {
                clientDoc.invalidateAndUnregister();

                throw ex; // Para que la página retornada sea la excepción
            }
        }
        finally
        {
            unbindRequestFromDocument();
        }
    }

    public ClientDocumentAttachedClientImpl requestAuthorization(ItsNatStfulDocumentImpl itsNatDoc)
    {
        // Evaluamos si está autorizado para controlar remótamente
        // EL DOCUMENTO NO ESTA SINCRONIZADO PARA PODER PREGUNTAR AL USUARIO MONITORIZADO
        int commMode;
        String commModeStr = getAttrOrParam("itsnat_comm_mode");  // Opcional
        if (commModeStr != null)
            commMode = Integer.parseInt(commModeStr);
        else
            commMode = itsNatDoc.getCommMode();

        long eventTimeout;
        String eventTimeoutStr = getAttrOrParam("itsnat_event_timeout");  // Opcional
        if (eventTimeoutStr != null)
            eventTimeout = Long.parseLong(eventTimeoutStr);
        else
            eventTimeout = itsNatDoc.getEventTimeout();

        long waitDocTimeout;
        String waitDocTimeoutStr = getAttrOrParam("itsnat_wait_doc_timeout");  // Opcional
        if (waitDocTimeoutStr != null)
            waitDocTimeout = Long.parseLong(waitDocTimeoutStr);
        else
            waitDocTimeout = 0; // No esperar

        boolean readOnly;
        String readOnlyStr = getAttrOrParam("itsnat_read_only");  // Opcional
        if (readOnlyStr != null)
        {
            try{ readOnly = MiscUtil.getBoolean(readOnlyStr); }
            catch(Exception ex) { throw new ItsNatException(ex); }
        }
        else readOnly = true; // Por defecto

        String refreshMethod = getAttrOrParam("itsnat_refresh_method");
        if (refreshMethod == null) refreshMethod = "none";

        int refreshInterval = -1;
        if (refreshMethod.equals("timer"))
            refreshInterval = Integer.parseInt(getAttrOrParamExist("itsnat_refresh_interval"));

        return requestAuthorization(itsNatDoc,refreshMethod,refreshInterval,commMode,eventTimeout,waitDocTimeout,readOnly);
    }


    public ClientDocumentAttachedClientImpl requestAuthorization(ItsNatStfulDocumentImpl itsNatDoc,String refreshMethod,int refreshInterval,int commMode,long eventTimeout,long waitDocTimeout,boolean readOnly)
    {
        // Evaluamos si está autorizado para controlar remótamente
        // EL DOCUMENTO NO ESTA SINCRONIZADO PARA PODER PREGUNTAR AL USUARIO MONITORIZADO
        Browser browser = itsNatDoc.getItsNatStfulDocumentTemplateVersion().getBrowser(itsNatRequest); // No hay problema de sincronización (multihilo) en estas llamadas

        ItsNatSessionImpl itsNatSession = itsNatRequest.getItsNatSessionImpl();

        // Phase ItsNatAttachedClientEvent.REQUEST;
        ClientDocumentAttachedClientImpl clientDoc = null;

        if (refreshMethod.equals("timer"))
        {
            clientDoc = new ClientDocumentAttachedClientTimerImpl(readOnly,refreshInterval,commMode,eventTimeout,waitDocTimeout,browser,itsNatSession,itsNatDoc);
        }
        else if (refreshMethod.equals("comet"))
        {
            clientDoc = new ClientDocumentAttachedClientCometImpl(readOnly,commMode,eventTimeout,waitDocTimeout,browser,itsNatSession,itsNatDoc);
        }
        else if (refreshMethod.equals("none"))
        {
            clientDoc = new ClientDocumentAttachedClientNotRefreshImpl(readOnly,commMode,eventTimeout,waitDocTimeout,browser,itsNatSession,itsNatDoc);
        }
        else throw new ItsNatException("Unrecognized refresh method: \"" + refreshMethod + "\"",itsNatRequest);

        bindClientToRequest(clientDoc,false);

        ItsNatAttachedClientEventImpl requestEvent = createItsNatAttachedClientEvent(clientDoc);

        // clientDoc por defecto está en modo REQUEST y acepted = false

        boolean wasProcessed = ItsNatAttachedClientEventListenerUtil.handleEventIncludingGlobalListeners(requestEvent);
        if (!requestEvent.isAccepted())
        {
            if (wasProcessed) return null; // El código del usuario tuvo oportunidad para hacer lo que sea
            else throw new ItsNatException("Request to remote control the document/page \"" + itsNatDoc.getItsNatDocumentTemplateImpl().getName() + "\" is not accepted",itsNatRequest);
        }

        commMode = clientDoc.getCommModeDeclared();
        if (!clientDoc.isReadOnly() && (clientDoc instanceof ClientDocumentAttachedClientCometImpl) &&
            ((commMode == CommMode.XHR_ASYNC_HOLD) || (commMode == CommMode.XHR_SYNC) ||
             (commMode == CommMode.SCRIPT_HOLD)) )
        {
            // No podemos permitir que el evento Comet al volver deje bloqueado el cliente pues dicho
            // request puede quedarse indefinidamente en el servidor, impidiendo los eventos normales del usuario en el cliente remoto.
            // No pasa nada por lanzar una excepción, el programador tuvo la oportunidad de detectar esto y esta situación
            // se detectará siempre en tiempo de desarrollo.
            throw new ItsNatException("Sync modes ASYNC_HOLD and SYNC and SCRIPT_HOLD are not valid in full remote control and Comet");
        }

        return clientDoc;
    }

    public void processTargetSessionNotFound(String targetSessionId)
    {
        prepareErrorResponse();

        this.response = new ResponseAttachedClientLoadDocErrorNotSessionImpl(targetSessionId,this);
        response.process();
    }

    public void processTargetDocumentNotFound(String targetSessionId,String targetDocId)
    {
        prepareErrorResponse();

        this.response = new ResponseAttachedClientLoadDocErrorNotDocImpl(targetSessionId,targetDocId,this);
        response.process();
    }

    public void processChildDocInDocContainerNotFound()
    {
        prepareErrorResponse();

        this.response = new ResponseAttachedClientLoadDocErrorNotDocInContainerImpl(this);
        response.process();
    }

    public void processParentElementNotFound()
    {
        prepareErrorResponse();

        this.response = new ResponseAttachedClientLoadDocErrorParentElementNotFoundImpl(this);
        response.process();
    }

    public void prepareErrorResponse()
    {
        ItsNatSessionImpl session = getItsNatSession();
        ClientDocumentAttachedClientErrorImpl clientDoc = new ClientDocumentAttachedClientErrorImpl(session);
        clientDoc.setPhase(ItsNatAttachedClientEvent.REQUEST);

        bindClientToRequest(clientDoc,false);  // El documento es nulo, no se vincula por tanto
    }

    public ItsNatAttachedClientEventImpl createItsNatAttachedClientEvent(ClientDocumentAttachedClientImpl clientDoc)
    {
        if (clientDoc instanceof ClientDocumentAttachedClientTimerImpl)
            return new ItsNatAttachedClientEventTimerImpl(this);
        else if (clientDoc instanceof ClientDocumentAttachedClientCometImpl)
            return new ItsNatAttachedClientEventCometImpl(this);
        else if (clientDoc instanceof ClientDocumentAttachedClientNotRefreshImpl)
            return new ItsNatAttachedClientEventNotRefreshImpl(this);
        return null; // ERROR
    }
}
