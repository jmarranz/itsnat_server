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

package org.itsnat.impl.comp.layer;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import org.itsnat.comp.ItsNatComponentUI;
import org.itsnat.comp.layer.ItsNatModalLayer;
import org.itsnat.core.ItsNatDOMException;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.NameValue;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.comp.ItsNatElementComponentImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByClientDefaultImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByClientImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByDocDefaultImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByDocImpl;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulMapImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.listener.EventListenerSerializableInternal;
import org.itsnat.impl.core.event.ItsNatEventImpl;
import org.itsnat.impl.core.event.ItsNatEventListenerChainImpl;
import org.itsnat.impl.core.listener.EventListenerUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatModalLayerImpl extends ItsNatElementComponentImpl implements ItsNatModalLayer
{
    protected ClientDocumentStfulMapImpl clientDocMap;
    protected boolean cleanBelow;
    protected int zIndex;
    protected float opacity;
    protected String background;
    protected boolean builtInElement = false;
    protected boolean boundToTree = false;
    protected ItsNatModalLayerImpl previous;
    protected LinkedHashSet<Element> bodyElementsBefore; // Los elementos bajo body que estaban antes en el caso de primer layer o los que se añadieron tras añadir el anterior layer. Es decir los elementos que fueron "ocultados" por este layer no ocultos antes
    protected EventListener detectUnexpectedEventListener;
    protected LinkedList<EventListener> unexpectedEventListeners;

    public ItsNatModalLayerImpl(Element element,boolean cleanBelow,int zIndex,float opacity,String background,NameValue[] artifacts,ItsNatStfulDocComponentManagerImpl compMgr)
    {
        super(element,artifacts,compMgr);

        constructor(element,cleanBelow,zIndex,opacity,background);
    }

    public ItsNatModalLayerImpl(Element element,NameValue[] artifacts,ItsNatStfulDocComponentManagerImpl compMgr)
    {
        super(element,artifacts,compMgr);

        boolean cleanBelow = getBooleanArtifactOrAttribute("cleanBelow",false);
        int zIndex = getIntegerArtifactOrAttribute("zIndex",compMgr.getItsNatModalLayers().size() + 1); // Todavía no ha sido registrado este modal layer
        float opacity = getFloatArtifactOrAttribute("opacity",(float)1);
        String background = getStringArtifactOrAttribute("background",getDefaultBackground());

        constructor(element,cleanBelow,zIndex,opacity,background);
    }

    public void constructor(Element element,boolean cleanBelow,int zIndex,float opacity,String background)
    {
        this.cleanBelow = cleanBelow;
        this.zIndex = zIndex;
        this.opacity = opacity;
        this.background = background;

        this.clientDocMap = new ClientDocumentStfulMapImpl(getItsNatStfulDocument());

        if (element == null) // Usaremos un <div> creado ex profeso
        {
            this.builtInElement = true;
            this.boundToTree = false;
        }
        else
        {
            this.builtInElement = false;
            this.boundToTree = DOMUtilInternal.isNodeInside(element,getItsNatDocument().getDocument());
            if (boundToTree && (element.getParentNode() != getVisualRootElement()))
                    throw new ItsNatDOMException("Provided element to the modal layer must be child of the visual root node (<body> in X/HTML)",element);
        }
    }

    @Override
    public void init()
    {
        super.init();

        ItsNatStfulDocComponentManagerImpl compMgr = getItsNatStfulDocComponentManager();
        LinkedList<ItsNatModalLayerImpl> layers = compMgr.getItsNatModalLayers();
        if (!layers.isEmpty())
            this.previous = layers.getLast();
        layers.add(this);

        if (!boundToTree)
            getVisualRootElement().appendChild(getElement());

        postInsertLayer();

        registerUnexpectedEventListenerDetection(); // Lo último
    }

    @Override
    protected void disposeEffective(boolean updateClient)
    {
        super.disposeEffective(updateClient);

        unregisterUnexpectedEventListenerDetection();

        ItsNatModalLayerClientDocImpl[] compClients = getAllItsNatModalLayerClientDoc();
        for(int i = 0; i < compClients.length; i++)
            compClients[i].preRemoveLayer();

        Element element = getElement();
        getVisualRootElement().removeChild(element);

        compClients = getAllItsNatModalLayerClientDoc();
        for(int i = 0; i < compClients.length; i++)
            compClients[i].postRemoveLayer();

        ItsNatStfulDocComponentManagerImpl compMgr = getItsNatStfulDocComponentManager();
        compMgr.getItsNatModalLayers().remove(this);
    }

    protected void registerUnexpectedEventListenerDetection()
    {
        this.detectUnexpectedEventListener = new EventListenerSerializableInternal()
        {
            public void handleEvent(Event evt)
            {
                dispatchEventReceivedElementHidden(evt);
            }
        };
        getItsNatStfulDocument().addEventListener(detectUnexpectedEventListener);
    }

    protected void unregisterUnexpectedEventListenerDetection()
    {
        getItsNatStfulDocument().removeEventListener(detectUnexpectedEventListener);
        this.detectUnexpectedEventListener = null;
    }

    protected void dispatchEventReceivedElementHidden(Event evt)
    {
        if (!hasUnexpectedEventListeners()) return; // No hay listeners, nada que hacer

        // Cada layer chequea los elementos "que ocultó"
        // No podemos evitar que sean "pulsados" pues incluso en los
        // navegadores de desktop podemos llegar a elementos ocultos
        // usando la tecla "tab" y "pulsar" usando "ENTER".
        ItsNatEvent itsNatEvt = (ItsNatEvent)evt;
        LinkedHashSet<Element> bodyElementsBefore = getBodyElementsBefore();
        if (!bodyElementsBefore.isEmpty())
        {
            for(Element elem : bodyElementsBefore)
            {
                dispatchEventReceivedElementHidden(evt,elem);
                if (itsNatEvt.getItsNatEventListenerChain().isStopped())
                    break; // No seguimos. Este chequeo no es estrictamente necesario pues se chequea más tarde pero ahorra llamadas
            }
        }
    }

    protected void dispatchEventReceivedElementHidden(Event evt,Element parentElem)
    {
        // El nodo target, si definido, es siempre hijo o el mismo que el currentTarget, no es necesario chequear los dos.
        EventTarget target = evt.getTarget();
        if (target != null)
            dispatchEventReceivedElementHidden(evt,(Node)target, parentElem); // AbstractView es también un Node en ItsNat, no hay problema en el cast.
        else
            dispatchEventReceivedElementHidden(evt,(Node)evt.getCurrentTarget(),parentElem);
    }

    protected void dispatchEventReceivedElementHidden(Event evt,Node node,Element parentElem)
    {
        if (!DOMUtilInternal.isChildOrSame(node,parentElem)) return;

        @SuppressWarnings("unchecked")
        ItsNatEventListenerChainImpl<EventListener> chain = ((ItsNatEventImpl)evt).getItsNatEventListenerChainImpl();
        if (getUnexpectedEventListenerList(chain)) // Se ha añadido alguno
            EventListenerUtil.handleEventListeners(evt,chain);
    }

    public boolean hasUnexpectedEventListeners()
    {
        if (unexpectedEventListeners == null) return false;
        return !unexpectedEventListeners.isEmpty();
    }

    public LinkedList<EventListener> getUnexpectedEventListenerList()
    {
        if (unexpectedEventListeners == null)
            this.unexpectedEventListeners = new LinkedList<EventListener>();
        return unexpectedEventListeners;
    }

    public boolean getUnexpectedEventListenerList(ItsNatEventListenerChainImpl<EventListener> chain)
    {
        return chain.addFirstListenerList(unexpectedEventListeners); // Puede ser null
    }

    public void addUnexpectedEventListener(EventListener listener)
    {
        LinkedList<EventListener> listeners = getUnexpectedEventListenerList();
        listeners.add(listener);
    }

    public void removeUnexpectedEventListener(EventListener listener)
    {
        LinkedList<EventListener> listeners = getUnexpectedEventListenerList();
        listeners.remove(listener);
    }


    public abstract String getDefaultBackground();

    public ItsNatCompNormalEventListenersByDocImpl createItsNatCompNormalEventListenersByDoc()
    {
        return new ItsNatCompNormalEventListenersByDocDefaultImpl(this);
    }

    public ItsNatCompNormalEventListenersByClientImpl createItsNatCompNormalEventListenersByClient(ClientDocumentImpl clientDoc)
    {
        return new ItsNatCompNormalEventListenersByClientDefaultImpl(this,clientDoc);
    }

    public boolean isCleanBelowMode()
    {
        return cleanBelow;
    }

    public float getOpacity()
    {
        return opacity;
    }

    public String getBackground()
    {
        return background;
    }

    public int getZIndex()
    {
        return zIndex;
    }

    public ItsNatModalLayerImpl getPreviousItsNatModalLayer()
    {
        return previous;
    }

    public ItsNatStfulDocComponentManagerImpl getItsNatStfulDocComponentManager()
    {
        // No tiene sentido usar este componente en un documento no AJAX (XML)
        return (ItsNatStfulDocComponentManagerImpl)componentMgr;
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        // No tiene sentido usar este componente en un documento no AJAX (XML)
        return (ItsNatStfulDocumentImpl)getItsNatDocumentImpl();
    }

    public Object createDefaultStructure()
    {
        return null;
    }

    public ItsNatComponentUI createDefaultItsNatComponentUI()
    {
        return null;
    }

    public void bindDataModel()
    {
    }

    public void unbindDataModel()
    {
    }

    public void initialSyncUIWithDataModel()
    {
    }

    public boolean isEnabled()
    {
        // HACER?
        return true;
    }

    public void setEnabled(boolean b)
    {
        // HACER?
    }

    public Object createDefaultModelInternal()
    {
        return null;
    }

    public ParamTransport[] getInternalParamTransports(String type,ClientDocumentImpl clientDoc)
    {
        return null;
    }

    public void postInsertLayer()
    {
        calcBodyElementListBefore();

        ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();

        ClientDocumentStfulImpl[] allClient = itsNatDoc.getAllClientDocumentStfulsCopy();
        for(int i = 0; i < allClient.length; i++)
        {
            ClientDocumentStfulImpl clientDoc = allClient[i];
            ClientDocumentStfulDelegateImpl clientDocDeleg = clientDoc.getClientDocumentStfulDelegate();
            if (!(clientDocDeleg instanceof ClientDocumentStfulDelegateWebImpl)) continue; // Sería raro pero lo mismo nos sale un Droid
            ItsNatModalLayerClientDocImpl compClient = createItsNatModalLayerClientDoc((ClientDocumentStfulDelegateWebImpl)clientDocDeleg);
            clientDocMap.put(clientDoc, compClient);

            compClient.postInsertLayer();
        }
    }

    @Override
    public void addClientDocumentAttachedClient(ClientDocumentAttachedClientImpl clientDoc)
    {
        super.addClientDocumentAttachedClient(clientDoc);

        ClientDocumentStfulDelegateImpl clientDocDeleg = clientDoc.getClientDocumentStfulDelegate();
        if (!(clientDocDeleg instanceof ClientDocumentStfulDelegateWebImpl)) throw new ItsNatException("Unexpected droid document"); // Sería raro pero lo mismo nos sale un Droid        
        
        ItsNatModalLayerClientDocImpl compClient = createItsNatModalLayerClientDoc((ClientDocumentStfulDelegateWebImpl)clientDocDeleg);
        clientDocMap.put(clientDoc, compClient);

        compClient.attachClientToComponent();
    }

    @Override
    public void removeClientDocumentAttachedClient(ClientDocumentAttachedClientImpl clientDoc)
    {
        super.removeClientDocumentAttachedClient(clientDoc);

        clientDocMap.remove(clientDoc);
    }

    public ItsNatModalLayerClientDocImpl[] getAllItsNatModalLayerClientDoc()
    {
        ItsNatModalLayerClientDocImpl[] clients = new ItsNatModalLayerClientDocImpl[clientDocMap.size()];
        clientDocMap.fillAllValues(clients);
        return clients;
    }

    public ItsNatModalLayerClientDocImpl getItsNatModalLayerClientDoc(ClientDocumentStfulImpl clientDoc)
    {
        return (ItsNatModalLayerClientDocImpl)clientDocMap.get(clientDoc);
    }

    public LinkedHashSet<Element> getBodyElementsBefore()
    {
        return bodyElementsBefore;
    }

    private void calcBodyElementListBefore()
    {
//        if (!cleanBelow) return;

        // Se llama una sola vez

        ItsNatStfulDocComponentManagerImpl compMgr = getItsNatStfulDocComponentManager();

        LinkedHashSet<Element> bodyElementsBefore = new LinkedHashSet<Element>();
        Element child = ItsNatTreeWalker.getFirstChildElement(getVisualRootElement());
        while(child != null)
        {
            bodyElementsBefore.add(child);
            child = ItsNatTreeWalker.getNextSiblingElement(child);
        }

        // Quitamos el layer actual recién insertado
        bodyElementsBefore.remove(getElement());

        LinkedList<ItsNatModalLayerImpl> layerList = compMgr.getItsNatModalLayers();
        for(ItsNatModalLayerImpl currLayer : layerList)
        {
            if (currLayer == this) break; // Hemos llegado al de ahora
            LinkedHashSet<Element> currSet = currLayer.getBodyElementsBefore();
            if (currSet != null) // Puede que no fuera un layer en modo cleanBelow
                bodyElementsBefore.removeAll(currSet);
        }

        this.bodyElementsBefore = bodyElementsBefore;
    }

    public Element getVisualRootElement()
    {
        return getItsNatStfulDocument().getVisualRootElement();
    }

    public abstract ItsNatModalLayerClientDocImpl createItsNatModalLayerClientDoc(ClientDocumentStfulDelegateWebImpl clientDoc);
}
