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

package org.itsnat.impl.core.doc;

import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.mut.doc.DocMutationEventListenerXMLImpl;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.CodeToSendListener;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.core.event.ItsNatAttachedClientEventListener;
import org.itsnat.core.script.ScriptUtil;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.impl.comp.mgr.ItsNatXMLDocComponentManagerImpl;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentXMLImpl;
import org.itsnat.impl.core.mut.doc.DocMutationEventListenerImpl;
import org.itsnat.impl.core.domutil.DefaultElementGroupManagerImpl;
import org.itsnat.impl.core.domutil.ElementGroupManagerImpl;
import org.itsnat.impl.core.template.xml.ItsNatXMLDocumentTemplateVersionImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 * Los métodos que no dan error es para conseguir que los componentes
 * funcionen con documentos XML sin eventos a costa de no hacer nada
 * en los métodos relacionados con eventos.
 *
 * @author jmarranz
 */
public class ItsNatXMLDocumentImpl extends ItsNatDocumentImpl
{

    /** Creates a new instance of ItsNatXMLDocumentImpl */
    public ItsNatXMLDocumentImpl(Document doc,ItsNatXMLDocumentTemplateVersionImpl docLoader,Browser browser,String requestURL,ItsNatSessionImpl parentSession,boolean stateless)
    {
        super(doc,docLoader,browser,requestURL,parentSession,stateless);
    }

    @Override
    public DocMutationEventListenerImpl createInternalMutationEventListener()
    {
        return new DocMutationEventListenerXMLImpl(this);
    }

    @Override
    public ClientDocumentImpl createClientDocumentOwner(Browser browser,ItsNatSessionImpl ownerSession)
    {
        return new ClientDocumentXMLImpl(this,browser,ownerSession);
    }

    @Override
    public ItsNatDocComponentManagerImpl createItsNatComponentManager()
    {
        return new ItsNatXMLDocComponentManagerImpl(this);
    }

    
    @Override
    public ScriptUtil getScriptUtil()
    {
        throw new ItsNatException("JavaScript utilities are not available for XML documents",this);
    }

    @Override
    public int getCommMode()
    {
        return 0; // Ignored
    }

    @Override
    public void setCommMode(int commMode)
    {
        // Ignored
    }

    @Override
    public long getEventTimeout()
    {
        return 0;
    }

    @Override
    public void setEventTimeout(long timeout)
    {
        // Ignored
    }

    @Override
    public long getEventDispatcherMaxWait()
    {
        return 0;
    }

    @Override
    public void setEventDispatcherMaxWait(long wait)
    {
        // Ignored
    }

    @Override
    public int getMaxOpenClientsByDocument()
    {
        return 0;
    }

    @Override
    public void setMaxOpenClientsByDocument(int value)
    {
        // Ignored
    }

    @Override
    public ClientDocumentImpl[] getAllClientDocumentsCopy()
    {
        return new ClientDocumentImpl[] { getClientDocumentOwnerImpl() };
    }

    @Override
    public void addEventListener(EventTarget target, String type, EventListener listener, boolean useCapture)
    {
        // Ignored
    }

    @Override
    public void addEventListener(EventTarget target, String type, EventListener listener, boolean useCapture, int commMode)
    {
        // Ignored
    }

    @Override
    public void addEventListener(EventTarget target, String type, EventListener listener, boolean useCapture, ParamTransport extraParam)
    {
        // Ignored
    }

    @Override
    public void addEventListener(EventTarget target, String type, EventListener listener, boolean useCapture, ParamTransport[] extraParams)
    {
        // Ignored
    }

    @Override
    public void addEventListener(EventTarget target, String type, EventListener listener, boolean useCapture, String preSendCode)
    {
        // Ignored
    }

    @Override
    public void addEventListener(EventTarget target, String type, EventListener listener, boolean useCapture, int commMode, ParamTransport[] extraParams, String preSendCode, long eventTimeout)
    {
        // Ignored
    }

    @Override
    public void removeEventListener(EventTarget target, String type, EventListener listener, boolean useCapture)
    {
        // Ignored
    }

    @Override
    public void removeEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,boolean updateClient)
    {
        // Ignored
    }

    @Override
    public void addMutationEventListener(EventTarget target, EventListener listener, boolean useCapture)
    {
        // Ignored
    }

    @Override
    public void addMutationEventListener(EventTarget target, EventListener listener, boolean useCapture, int commMode, String preSendCode, long eventTimeout)
    {
        // Ignored
    }

    @Override
    public void removeMutationEventListener(EventTarget target, EventListener listener, boolean useCapture)
    {
        // Ignored
    }

    @Override
    public void addUserEventListener(EventTarget target, String name, EventListener listener, int commMode, ParamTransport[] extraParams, String preSendCode, long eventTimeout)
    {
        // Ignored
    }

    @Override
    public void addUserEventListener(EventTarget target, String name, EventListener listener)
    {
        // Ignored
    }

    @Override
    public void removeUserEventListener(EventTarget target, String name, EventListener listener)
    {
        // Ignored
    }

    @Override
    public void addCodeToSend(Object code)
    {
        throw new ItsNatException("XML documents have not JavaScript sent from server",this);
    }

    @Override
    public boolean isSendCodeEnabled()
    {
        throw new ItsNatException("XML documents have not JavaScript sent from server",this);
    }

    @Override
    public void disableSendCode()
    {
        throw new ItsNatException("XML documents have not JavaScript sent from server",this);
    }

    @Override
    public void enableSendCode()
    {
        throw new ItsNatException("XML documents have not JavaScript sent from server",this);
    }

    @Override
    public void addCodeToSendListener(CodeToSendListener listener)
    {
        throw new ItsNatException("XML documents have not JavaScript sent from server",this);
    }

    @Override
    public void removeCodeToSendListener(CodeToSendListener listener)
    {
        throw new ItsNatException("XML documents have not JavaScript sent from server",this);
    }

    @Override
    public void addItsNatAttachedClientEventListener(ItsNatAttachedClientEventListener listener)
    {
        throw new ItsNatException("XML documents have not events",this);
    }

    @Override
    public void removeItsNatAttachedClientEventListener(ItsNatAttachedClientEventListener listener)
    {
        throw new ItsNatException("XML documents have not events",this);
    }

    @Override
    public void addEventListener(EventListener listener)
    {
        throw new ItsNatException("XML documents have not events",this);
    }

    @Override
    public void removeEventListener(EventListener listener)
    {
        throw new ItsNatException("XML documents have not events",this);
    }

    @Override
    public boolean dispatchEvent(EventTarget target, Event evt) throws EventException
    {
        throw new ItsNatException("XML documents have not events",this);
    }

    @Override
    public boolean dispatchEventLocally(EventTarget target, Event evt) throws EventException
    {
        throw new ItsNatException("XML documents have not events",this);
    }

    @Override
    public void addReferrerItsNatServletRequestListener(ItsNatServletRequestListener listener)
    {
        throw new ItsNatException("XML documents have not events",this);
    }

    @Override
    public void removeReferrerItsNatServletRequestListener(ItsNatServletRequestListener listener)
    {
        throw new ItsNatException("XML documents have not events",this);
    }

    @Override
    public Event createEvent(String eventType) throws DOMException
    {
        throw new ItsNatException("XML documents have not events",this);
    }

    @Override
    public ElementGroupManagerImpl createElementGroupManager()
    {
        return new DefaultElementGroupManagerImpl(this);
    }

    @Override
    public boolean isDisconnectedChildNodesFromClient(Node node)
    {
        throw new ItsNatException("This feature has no sense in XML documents",this);
    }

    @Override
    public Node disconnectChildNodesFromClient(Node node)
    {
        throw new ItsNatException("This feature has no sense in XML documents",this);
    }

    @Override
    public void reconnectChildNodesToClient(Node node)
    {
        throw new ItsNatException("This feature has no sense in XML documents",this);
    }
}
