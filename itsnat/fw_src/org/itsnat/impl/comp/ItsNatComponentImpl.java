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

package org.itsnat.impl.comp;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.util.HashMap;
import java.util.Map;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.ItsNatComponentUI;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDOMException;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.NameValue;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersAllClientsImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByClientImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByDocImpl;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.impl.core.ItsNatUserDataImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.mut.doc.BeforeAfterMutationRenderListener;
import org.itsnat.impl.core.mut.doc.DocMutationEventListenerImpl;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.MutationEvent;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatComponentImpl extends ItsNatUserDataImpl implements ItsNatComponentInternal,EventListener,BeforeAfterMutationRenderListener
{
    protected ItsNatDocComponentManagerImpl componentMgr;
    protected Map<String,Object> artifacts;
    protected ItsNatCompNormalEventListenersByDocImpl normalEventListenersByDoc;
    protected ItsNatCompNormalEventListenersAllClientsImpl normalEventListenersByClient;
    protected boolean disposed = false;
    protected PropertyChangeSupport changeSupport;
    protected VetoableChangeSupport vetoableChangeSupport;
    protected Object dataModel;
    protected ItsNatComponentUI compUI; // puede ser null pues no todos tienen UI (ej. form)
    protected Node node;
    protected boolean hasMutationEventListener = false;
    protected Event currentEvent;

    /** Creates a new instance of ItsNatComponentImpl */
    public ItsNatComponentImpl(Node node,NameValue[] artifacts,ItsNatDocComponentManagerImpl componentMgr)
    {
        super(false);

        this.componentMgr = componentMgr;

        if (artifacts != null)
        {
            Map<String,Object> artifactMap = getArtifactMap();
            for(int i = 0; i < artifacts.length; i++)
            {
                NameValue artif = artifacts[i];
                artifactMap.put(artif.getName(),artif.getValue());
            }
        }

        if (node == null)
            node = createDefaultNode(); // Puede devolver null
        this.node = node; // Se tolera el caso de que sea nulo para permitir un posterior "attach" (ningún componente estándar por ahora)

        this.normalEventListenersByDoc = createItsNatCompNormalEventListenersByDoc();
        this.normalEventListenersByClient = new ItsNatCompNormalEventListenersAllClientsImpl(this);
    }

    public void init()
    {
        // Debe llamarse desde alguna clase derivada como parte del proceso de creación
        setDefaultModels();
        setDefaultItsNatComponentUI();

        if (node != null)
        {
            ItsNatDocumentImpl itsNatDoc = getItsNatDocumentImpl();
            if (isNodeBoundToDocument())
            {
                enableEventListeners(); // Hay que tener en cuenta que si no estuviera en el árbol del documento daría error
            }
            else
            {
                // Es un componente preparado para ser añadido y quitado del árbol continuamente, es usado por ejemplo por los componentes usados como editores
                DocMutationEventListenerImpl mainListener = itsNatDoc.getDocMutationEventListener();
                mainListener.addBeforeAfterMutationRenderListener(node,this);
                this.hasMutationEventListener = true;

                ItsNatDocComponentManagerImpl compMgr = getItsNatComponentManagerImpl();
                compMgr.addExcludedNodeAsItsNatComponent(node); // Evita que un buildComponents lo añada, aunque el nodo no esté unido al árbol no importa desde el punto de vista del registro de ItsNatDocument
            }
        }

        setEnabled(true);
    }

    public boolean isNodeBoundToDocument()
    {
        return DOMUtilInternal.isNodeInside(node,getItsNatDocumentImpl().getDocument());
    }

    public ItsNatCompNormalEventListenersAllClientsImpl getItsNatCompNormalEventListenersAllClients()
    {
        return normalEventListenersByClient;
    }

    public abstract ItsNatCompNormalEventListenersByDocImpl createItsNatCompNormalEventListenersByDoc();

    public abstract ItsNatCompNormalEventListenersByClientImpl createItsNatCompNormalEventListenersByClient(ClientDocumentImpl clientDoc);

    public ItsNatCompNormalEventListenersByClientImpl getItsNatCompNormalEventListenersByClient(ClientDocumentImpl clientDoc)
    {
        return normalEventListenersByClient.getItsNatCompNormalEventListenersByClient(clientDoc);
    }

    public boolean mayBeInPlaceEditorComponent()
    {    
        return hasMutationEventListener;
    }
    
    public ItsNatCompNormalEventListenersByDocImpl getItsNatCompNormalEventListenersByDoc()
    {
        return normalEventListenersByDoc;
    }

    public void setDefaultModels()
    {
        // Derivar para los selection models
        setDefaultDataModel();
    }

    public boolean isDisposed()
    {
        return disposed;
    }

    public void dispose()
    {
        // Derivar disposeEffective(boolean), no dispose()
        if (!disposed)
            disposeEffective(true);
    }

    protected void disposeEffective(boolean updateClient)
    {
        // updateClient es siempre true por ahora

        unbindModels();

        disableEventListeners(updateClient);

        ItsNatDocComponentManagerImpl compMgr = getItsNatComponentManagerImpl();

        compMgr.removeItsNatComponent(this,false);
        if (hasMutationEventListener)
        {
            ItsNatDocumentImpl itsNatDoc = getItsNatDocumentImpl();
            DocMutationEventListenerImpl mainListener = itsNatDoc.getDocMutationEventListener();
            Node node = getNode();
            mainListener.removeBeforeAfterMutationRenderListener(node,this);
            compMgr.removeExcludedNodeAsItsNatComponent(node);
        }
        this.disposed = true;
    }

    public Node getNode()
    {
        return node;
    }

    public void setNode(Node node)
    {
        // Derivar si se permite "reattachment"
        throw new ItsNatDOMException("This component cannot be reattached",node);
    }

    public abstract ItsNatComponentUI createDefaultItsNatComponentUI();

    public void setDefaultItsNatComponentUI()
    {
        setItsNatComponentUI(createDefaultItsNatComponentUI());
    }

    public abstract Node createDefaultNode();

    public ItsNatComponentManager getItsNatComponentManager()
    {
        return componentMgr;
    }

    public ItsNatDocComponentManagerImpl getItsNatComponentManagerImpl()
    {
        return componentMgr;
    }

    public ItsNatDocumentImpl getItsNatDocumentImpl()
    {
        return getItsNatComponentManagerImpl().getItsNatDocumentImpl();
    }

    public ItsNatDocument getItsNatDocument()
    {
        return getItsNatDocumentImpl();
    }

    public ItsNatComponentUI getItsNatComponentUI()
    {
        return compUI;
    }

    public void setItsNatComponentUI(ItsNatComponentUI compUI)
    {
        this.compUI = compUI;
        initialSyncUIWithDataModel();
    }

    public boolean hasArtifacts()
    {
        if (artifacts == null) return false;
        return !artifacts.isEmpty();
    }

    public Map<String,Object> getArtifactMap()
    {
        if (artifacts == null)
            this.artifacts = new HashMap<String,Object>();
        return artifacts;
    }

    public void registerArtifact(String name,Object value)
    {
        Map<String,Object> artifacts = getArtifactMap();
        artifacts.put(name,value);
    }

    public Object getArtifact(String name)
    {
        if (!hasArtifacts()) return null;

        Map<String,Object> artifacts = getArtifactMap();
        return artifacts.get(name);
    }

    public Object removeArtifact(String name)
    {
        Map<String,Object> artifacts = getArtifactMap();
        return artifacts.remove(name);
    }

    public Object getArtifact(String name,boolean cascade)
    {
        Object artif = getArtifact(name);
        if (cascade && (artif == null))
            artif = getItsNatDocumentImpl().getArtifact(name,true);
        return artif;
    }

    public abstract ParamTransport[] getInternalParamTransports(String type,ClientDocumentImpl clientDoc);

    public void setEventListenerParams(String type,boolean useCapture,int commMode,
            ParamTransport[] extraParams,String preSendCode,long eventTimeout)
    {
        normalEventListenersByDoc.setEventListenerParams(type, useCapture, commMode, extraParams, preSendCode, eventTimeout);
    }

    public void setEventListenerParams(ClientDocument clientDoc,String type,boolean useCapture,int commMode,
            ParamTransport[] extraParams,String preSendCode,long eventTimeout)
    {
        ItsNatCompNormalEventListenersByClientImpl listeners = normalEventListenersByClient.getItsNatCompNormalEventListenersByClient((ClientDocumentImpl)clientDoc);        
        listeners.setEventListenerParams(type, useCapture, commMode, extraParams, preSendCode, eventTimeout);
    }

    public void addEventListener(String type,EventListener listener)
    {
        addEventListener(type, listener,false);
    }

    public void removeEventListener(String type,EventListener listener)
    {
        removeEventListener(type, listener,false);
    }

    public void addEventListener(String type,EventListener listener,boolean before)
    {
        normalEventListenersByDoc.addUserEventListener(type,listener,before);
    }

    public void removeEventListener(String type,EventListener listener,boolean before)
    {
        normalEventListenersByDoc.removeUserEventListener(type,listener,before);
    }

    public void enableEventListener(String type)
    {
        normalEventListenersByDoc.enableEventListener(type);
    }

    public void disableEventListener(String type)
    {
        normalEventListenersByDoc.disableEventListener(type);
    }

    public void addEventListener(ClientDocument clientDoc,String type,EventListener listener)
    {
        addEventListener(clientDoc,type, listener,false);
    }

    public void removeEventListener(ClientDocument clientDoc,String type,EventListener listener)
    {
        removeEventListener(clientDoc,type, listener,false);
    }

    public void addEventListener(ClientDocument clientDoc,String type,EventListener listener,boolean before)
    {
        ItsNatCompNormalEventListenersByClientImpl listeners = normalEventListenersByClient.getItsNatCompNormalEventListenersByClient((ClientDocumentImpl)clientDoc);
        listeners.addUserEventListener(type,listener,before);
    }

    public void removeEventListener(ClientDocument clientDoc,String type,EventListener listener,boolean before)
    {
        ItsNatCompNormalEventListenersByClientImpl listeners = normalEventListenersByClient.getItsNatCompNormalEventListenersByClient((ClientDocumentImpl)clientDoc);
        listeners.removeUserEventListener(type,listener,before);
    }

    public void enableEventListener(ClientDocument clientDoc,String type)
    {
        ItsNatCompNormalEventListenersByClientImpl listeners = normalEventListenersByClient.getItsNatCompNormalEventListenersByClient((ClientDocumentImpl)clientDoc);
        listeners.enableEventListener(type);
    }

    public void disableEventListener(ClientDocument clientDoc,String type)
    {
        ItsNatCompNormalEventListenersByClientImpl listeners = normalEventListenersByClient.getItsNatCompNormalEventListenersByClient((ClientDocumentImpl)clientDoc);
        listeners.disableEventListener(type);
    }

    public void enableEventListener(String type,ItsNatCompNormalEventListenersByClientImpl listeners)
    {
        listeners.enableEventListener(type);
    }

    public void disableEventListener(String type,ItsNatCompNormalEventListenersByClientImpl listeners)
    {
        listeners.disableEventListener(type);
    }

    public void enableEventListeners()
    {
        enableEventListenersByDoc();
        enableEventListenersByClient();
    }

    public void disableEventListeners()
    {
        disableEventListeners(true);
    }

    public void disableEventListeners(boolean updateClient)
    {
        disableEventListenersByDoc(updateClient);
        disableEventListenersByClient(updateClient);
    }

    public void enableEventListenersByDoc()
    {
        // Derivar para añadir los event type concretos.
    }

    public void disableEventListenersByDoc()
    {
        disableEventListenersByDoc(true);
    }

    public void disableEventListenersByDoc(boolean updateClient)
    {
        normalEventListenersByDoc.disableEventListeners(updateClient);
    }

    public void enableEventListenersByClient()
    {
        ItsNatCompNormalEventListenersByClientImpl[] clients = normalEventListenersByClient.getAllItsNatCompNormalEventListenersByClient();
        for(int i = 0; i < clients.length; i++)
            enableEventListenersByClient(clients[i]);
    }

    public void disableEventListenersByClient()
    {
        disableEventListenersByClient(true);
    }

    public void disableEventListenersByClient(boolean updateClient)
    {
        ItsNatCompNormalEventListenersByClientImpl[] clients = normalEventListenersByClient.getAllItsNatCompNormalEventListenersByClient();
        for(int i = 0; i < clients.length; i++)
            disableEventListenersByClient(updateClient,clients[i]);
    }

    public void enableEventListenersByClient(ItsNatCompNormalEventListenersByClientImpl listeners)
    {
       // Derivar para registrar los event type concretos
    }

    public void disableEventListenersByClient(ItsNatCompNormalEventListenersByClientImpl listeners)
    {
        disableEventListenersByClient(true,listeners);
    }

    public void disableEventListenersByClient(boolean updateClient,ItsNatCompNormalEventListenersByClientImpl listeners)
    {
        listeners.disableEventListeners(updateClient);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        if (listener == null)
            return;

        if (changeSupport == null)
            changeSupport = new PropertyChangeSupport(this);

        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        if (listener == null || changeSupport == null)
            return;

        changeSupport.removePropertyChangeListener(listener);
    }

    public PropertyChangeListener[] getPropertyChangeListeners()
    {
        if (changeSupport == null)
            return new PropertyChangeListener[0];

        return changeSupport.getPropertyChangeListeners();
    }

    public void addPropertyChangeListener(String propertyName,PropertyChangeListener listener)
    {
        if (listener == null)
            return;

        if (changeSupport == null)
            changeSupport = new PropertyChangeSupport(this);

        changeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName,PropertyChangeListener listener)
    {
        if (listener == null || changeSupport == null)
            return;

        changeSupport.removePropertyChangeListener(propertyName, listener);
    }

    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName)
    {
        if (changeSupport == null)
            return new PropertyChangeListener[0];

        return changeSupport.getPropertyChangeListeners(propertyName);
    }

    protected void firePropertyChange(String propertyName,Object oldValue, Object newValue)
    {
        PropertyChangeSupport changeSupport = this.changeSupport;
        if ((changeSupport == null) ||
            ((oldValue != null) && (newValue != null) && oldValue.equals(newValue)))
            return;

        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void addVetoableChangeListener(VetoableChangeListener listener)
    {
        if (listener == null)
            return;

        if (vetoableChangeSupport == null)
            vetoableChangeSupport = new VetoableChangeSupport(this);

        vetoableChangeSupport.addVetoableChangeListener(listener);
    }

    public void removeVetoableChangeListener(VetoableChangeListener listener)
    {
        if (listener == null || changeSupport == null)
            return;

        vetoableChangeSupport.removeVetoableChangeListener(listener);
    }

    public VetoableChangeListener[] getVetoableChangeListeners()
    {
        if (vetoableChangeSupport == null)
            return new VetoableChangeListener[0];

        return vetoableChangeSupport.getVetoableChangeListeners();
    }

    protected void fireVetoableChange(String propertyName,Object oldValue, Object newValue) throws PropertyVetoException
    {
        if (vetoableChangeSupport == null)
            return;
        // Incluso aunque sea igual el valor hacemos fire así podemos detectar un intento de cambio (aunque sea al mismo valor) de una propiedad de sólo lectura
        // de igual manera que se hace en JComponent
        vetoableChangeSupport.fireVetoableChange(propertyName, oldValue, newValue);
    }

    public void setDefaultDataModel()
    {
        Object dataModel = createDefaultModelInternal();
        if (dataModel != null)
            setDefaultDataModel(dataModel);
        // Si es null es que no tiene modelo
    }

    public void setDefaultDataModel(Object dataModel)
    {
        setDataModel(dataModel);
    }

    public void unbindModels()
    {
        unbindDataModel();
    }

    public abstract void bindDataModel();
    public abstract void unbindDataModel();

    public Object getDataModel()
    {
        return dataModel;
    }

    public void setDataModel(Object dataModel)
    {
        setDataModel(dataModel,false);
    }

    public void setDataModel(Object dataModel,boolean acceptNull)
    {
        if ((dataModel == null) && !acceptNull)
            throw new ItsNatException("Data model cannot be null",this);

        // Aunque el nuevo modelo sea el mismo que el que ya hay
        // hacemos unbindDataModel() siempre pues de esta manera podemos hacer
        // una especie de reset, éste es útil para poder añadir un listener desde fuera
        // que se llame antes del listener interno en el caso del data model
        // por defecto, si se hace "reset" el listener se quitará y se añadirá de nuevo
        // quedando el último.

        if (this.dataModel != null)
            unbindDataModel();

        this.dataModel = dataModel;

        // El dataModel del usuario manda sobre el DOM
        initialSyncWithDataModel();

        // A partir de ahora los cambios los repercutimos en el DOM por eventos
        // No se debe cambiar el DOM por otra vía que por el objeto dataModel
        bindDataModel();
    }

    public Event getCurrentEventProcessing()
    {
        return currentEvent;
    }

    public void setCurrentEventProcessing(Event currentEvent)
    {
        this.currentEvent = currentEvent;
    }

    public void handleEvent(Event evt)
    {
        if (!isEnabled()) return;

        setCurrentEventProcessing(evt);

        try
        {
            processNormalEventUserListeners(evt,true);
            ItsNatEvent itsNatEvt = ((ItsNatEvent)evt);
            if (!itsNatEvt.getItsNatEventListenerChain().isStopped()) // De esta manera en los eventos del usuario previos podemos parar el comportamiento por defecto del componente
                processNormalEvent(evt);
            processNormalEventUserListeners(evt,false);
        }
        finally
        {
            setCurrentEventProcessing(null);
        }
    }

    public void processNormalEventUserListeners(Event evt,boolean before)
    {
        if (((ItsNatEvent)evt).getItsNatEventListenerChain().isStopped())
            return; // No es estrictamente necesario (se chequea después) pero evita llamadas inútiles
            
        normalEventListenersByDoc.processNormalEventUserListeners(evt,before);

        if (((ItsNatEvent)evt).getItsNatEventListenerChain().isStopped())
            return; // No es estrictamente necesario (se chequea después) pero evita llamadas inútiles

        // Por ahora no hay listeners por cliente, pero en el futuro...
        ItsNatCompNormalEventListenersByClientImpl[] clients = normalEventListenersByClient.getAllItsNatCompNormalEventListenersByClient();
        for(int i = 0; i < clients.length; i++)
            clients[i].processNormalEventUserListeners(evt, before);
    }

    public void processNormalEvent(Event evt)
    {
        // Derivar para hacer lo específico del componente
    }

    public void initialSyncWithDataModel()
    {
        if (getItsNatComponentUI() != null)
            initialSyncUIWithDataModel();
    }

    public void beforeRender(Node node,MutationEvent evt)
    {
        String type = evt.getType();
        if (type.equals("DOMNodeRemoved"))
        {
            // Quitamos el listener porque si se añade de nuevo el nodo
            // al documento, en el cliente será un objeto nuevo por lo que
            // habrá que registrar de nuevo el listener
            disableEventListeners();
        }
    }

    public void afterRender(Node node,MutationEvent evt)
    {
        String type = evt.getType();
        if (type.equals("DOMNodeInserted"))
        {
            enableEventListeners();
        }
    }

    public void addClientDocumentAttachedClient(ClientDocumentAttachedClientImpl clientDoc)
    {
        normalEventListenersByDoc.addClientDocumentAttachedClient(clientDoc);

        ItsNatCompNormalEventListenersByClientImpl listeners = normalEventListenersByClient.addItsNatCompNormalEventListenersByClient(clientDoc);
        if (isNodeBoundToDocument())
            enableEventListenersByClient(listeners);
    }

    public void removeClientDocumentAttachedClient(ClientDocumentAttachedClientImpl clientDoc)
    {
        normalEventListenersByDoc.removeClientDocumentAttachedClient(clientDoc);

        ItsNatCompNormalEventListenersByClientImpl listeners = normalEventListenersByClient.removeItsNatCompNormalEventListenersByClient(clientDoc);
        if (isNodeBoundToDocument())
            disableEventListenersByClient(false,listeners); // updateClient = false para evitar generación inútil de código
    }
}
