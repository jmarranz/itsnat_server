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

package org.itsnat.impl.comp.inplace;

import java.io.Serializable;
import javax.swing.AbstractCellEditor;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.event.EventListenerInternal;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatCellEditorImpl extends AbstractCellEditor implements EventListener,Serializable
{
    protected DelegateComponentEditorImpl delegate;
    protected ItsNatStfulDocComponentManagerImpl componentMgr;
    protected EventListener globalEventListener;
    protected boolean editing = false;

    /**
     * Creates a new instance of ItsNatCellEditorImpl
     */
    public ItsNatCellEditorImpl(ItsNatComponent compEditor,ItsNatStfulDocComponentManagerImpl componentMgr)
    {
        // compEditor puede ser null inicialmente
        this.componentMgr = componentMgr;

        this.delegate = DelegateComponentEditorImpl.createDelegateComponentEditor(compEditor,this);

        this.globalEventListener = new EventListenerInternal()
        {
            public void handleEvent(Event evt)
            {
                ClientDocumentStfulImpl clientDoc = (ClientDocumentStfulImpl)((ItsNatEvent)evt).getClientDocument();
                ItsNatCellEditorClientImpl editClient = ItsNatCellEditorClientImpl.getItsNatHTMLCellEditorClient(clientDoc.getBrowser(),ItsNatCellEditorImpl.this.getCellEditorComponent());
                editClient.handleGlobalEvent(evt, ItsNatCellEditorImpl.this);
            }
        };
    }

    public DelegateComponentEditorImpl getEditorDelegate()
    {
        return delegate;
    }

    public ItsNatDocumentImpl getItsNatDocument()
    {
        return componentMgr.getItsNatDocumentImpl();
    }

    public ItsNatStfulDocComponentManagerImpl getItsNatStfulDocComponentManager()
    {
        return componentMgr;
    }

    public Object getCellEditorValue()
    {
        return delegate.getCellEditorValue();
    }

    public ItsNatComponent getCellEditorComponent()
    {
        return delegate.getCellEditorComponent();
    }

    public Element getCellElement()
    {
        ItsNatComponent compEditor = getCellEditorComponent();
        return (Element)compEditor.getNode().getParentNode();
    }
    
    public ItsNatComponent getCellEditorComponent(Object value,Element cellElem)
    {
        unregisterEventListeners(); // Por si acaso no se recibió el blur en una anterior edición

        ItsNatComponent compEditor = getCellEditorComponent();

        Node nodeEditor = compEditor.getNode();

        delegate.preSetValue(value);  // Antes de insertar

        cellElem.appendChild(nodeEditor);  // Se detecta y se añaden los DOM listeners automáticamente en el componente, cuando se quite del árbol también se detecta y se quitan los listeners antes

        delegate.setValue(value); // Conviene llamar antes que focus() 
        delegate.setFocus();

        registerEventListeners();

        return compEditor;
    }

    public void handleEvent(Event evt)
    {
        String type = evt.getType();
        if (type.equals("blur"))
        {
            stopCellEditing();
        }
        else
        {
            ItsNatComponent compEditor = getCellEditorComponent();
            ItsNatStfulDocumentImpl itsNatDoc = (ItsNatStfulDocumentImpl)getItsNatDocument();
            ClientDocumentStfulImpl[] clientList = itsNatDoc.getAllClientDocumentStfulsCopy();
            for(int i = 0; i < clientList.length; i++)
            {
                ClientDocumentStfulImpl clientDoc = clientList[i];
                ItsNatCellEditorClientImpl editClient = ItsNatCellEditorClientImpl.getItsNatHTMLCellEditorClient(clientDoc.getBrowser(),compEditor);
                editClient.handleEvent(evt, this, clientDoc);
            }
        }
    }

    @Override
    public boolean stopCellEditing()
    {
        unregisterEventListeners();

        return super.stopCellEditing();
    }

    @Override
    public void cancelCellEditing()
    {
        unregisterEventListeners();

        super.cancelCellEditing();
    }

    private void registerEventListeners()
    {
        this.editing = true;

        ItsNatComponent compEditor = getCellEditorComponent();
        compEditor.addEventListener("blur",this);

        // Añadimos un listener click al documento que hace **capture** con el único fin
        // de llamar a blur() del elemento, por ahora todos los elementos editor usados (input, select, textarea) tienen método blur.
        // Lo de que sea capture es para que se procese antes de que el click por ejemplo elimine el elemento editándose.
        // Ni siquiera necesitamos que el evento llegue al servidor, por eso llamamos a "return;"
        // en JavaScript para evitar la llamada sendEvent()
        // En teoría este listener se ha de llamar sólo una vez (después será desregistrado)
        // Esto soluciona el que el no se lance el blur en MSIE (6 y 7) al pulsar fuera en algunas circunstancias
        // tal y como ocurre en el ejemplo ExtJS (al pulsar otra fila) y en general cuando se sale del editor in place
        // muy rápido para editar otro (ocurre incluso en FireFox 3)

        // En este contexto no puede ser otra cosa que un documento AJAX
        ItsNatStfulDocumentImpl itsNatDoc = (ItsNatStfulDocumentImpl)getItsNatDocument();
        ClientDocumentStfulImpl[] clientList = itsNatDoc.getAllClientDocumentStfulsCopy();

        for(int i = 0; i < clientList.length; i++)
        {
            ClientDocumentStfulImpl clientDoc = clientList[i];
            ItsNatCellEditorClientImpl editClient = ItsNatCellEditorClientImpl.getItsNatHTMLCellEditorClient(clientDoc.getBrowser(),compEditor);
            editClient.registerEventListeners(this,clientDoc);
        }

        /*
         * Último recurso cuando por alguna razón no se envía el blur
         */
        itsNatDoc.addEventListener(0,globalEventListener);
    }

    private void unregisterEventListeners()
    {
        if (!editing) return; // No hace falta.
        this.editing = false;

        ItsNatComponent compEditor = getCellEditorComponent();
        compEditor.removeEventListener("blur",this);

        // En este contexto no puede ser otra cosa que un documento AJAX
        ItsNatStfulDocumentImpl itsNatDoc = (ItsNatStfulDocumentImpl)getItsNatDocument();
        ClientDocumentStfulImpl[] clientList = itsNatDoc.getAllClientDocumentStfulsCopy();

        for(int i = 0; i < clientList.length; i++)
        {
            ClientDocumentStfulImpl clientDoc = clientList[i];
            ItsNatCellEditorClientImpl editClient = ItsNatCellEditorClientImpl.getItsNatHTMLCellEditorClient(clientDoc.getBrowser(),compEditor);
            editClient.unregisterEventListeners(this, clientDoc);
        }

        itsNatDoc.removeEventListener(globalEventListener); // No pasa nada por eliminarse mientras se están procesando los globales, el framework está preparado
    }
}
