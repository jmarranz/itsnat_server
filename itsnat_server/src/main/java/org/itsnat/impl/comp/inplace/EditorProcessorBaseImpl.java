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

import java.util.EventObject;
import javax.swing.CellEditor;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.impl.comp.ItsNatElementComponentImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.listener.EventListenerSerializableInternal;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public abstract class EditorProcessorBaseImpl implements CellEditorListener,EventListenerSerializableInternal
{
    protected CellEditor cellEditor;
    protected Element cellElem;
    protected Node children; // Si son varios es un DocumentFragment, puede ser null si no hay hijos
    protected ItsNatElementComponentImpl compParent;
    protected String editorActivatorEvent = "dblclick";
    protected boolean editing = false;
    protected boolean registeredActivatorEvent = false;
    protected boolean editingEnabled = true; // Es útil para evitar *temporalmente* por ejemplo que se tienda a abrir "accidentalmente" una nueva edición cuando otra está todavía en activo
    protected ItsNatComponent compEditor;

    /** Creates a new instance of EditorProcessorBaseImpl */
    public EditorProcessorBaseImpl(ItsNatElementComponentImpl compParent)
    {
        this.compParent = compParent;
    }

    public void dispose()
    {
        setCellEditor(null);
    }

    public CellEditor getCellEditor()
    {
        return cellEditor;
    }

    public void setCellEditor(CellEditor cellEditor)
    {
        if (this.cellEditor != null)
        {
            this.cellEditor.removeCellEditorListener(this);
            this.cellEditor = null;
        }

        this.cellEditor = cellEditor;

        if (cellEditor != null)
        {
            registerActivatorEvent();
            cellEditor.addCellEditorListener(this);
        }
        else
            unregisterActivatorEvent(); // Pues no hay editor que lanzar
    }

    public void registerActivatorEvent()
    {
        if (registeredActivatorEvent || (cellEditor == null))
            return; // ya registrado o no merece la pena

        String type = getEditorActivatorEvent();
        if (type != null)
        {
            compParent.addEventListener(type,this); // Llama a enableEventListener(type); si es necesario
            this.registeredActivatorEvent = true;
        }
    }

    public void unregisterActivatorEvent()
    {
        if (!registeredActivatorEvent) return; // ya desregistrado

        String type = getEditorActivatorEvent();
        if (type != null)
        {
            compParent.removeEventListener(type,this);
            this.registeredActivatorEvent = false;
        }
    }

    public void enableEventListenersByDoc()
    {
        registerActivatorEvent();
    }

    public void disableEventListeners(boolean updateClient)
    {
        unregisterActivatorEvent();
    }

    public void handleEvent(Event evt)
    {
        startEdition(evt);
    }

    public String getEditorActivatorEvent()
    {
        return editorActivatorEvent;
    }

    public void setEditorActivatorEvent(String editorActivatorEvent)
    {
        finalizeEdition(false); // Terminar una posible sesión actualmente abierta pues en edición se desactiva el evento de edición en el componente padre, un cambio en medio de la edición daría problemas

        unregisterActivatorEvent();

        this.editorActivatorEvent = editorActivatorEvent;

        registerActivatorEvent();
    }

    public void editingStopped(ChangeEvent e)
    {
        finalizeEdition(true);
    }

    public void editingCanceled(ChangeEvent e)
    {
        finalizeEdition(false);
    }

    public boolean isEditing()
    {
        return editing;
    }

    public void finalizeEdition(boolean acceptNewValue)
    {
        if (!isEditing()) return;

        // Reactivamos lo que desactivamos en afterShow()
        String type = getEditorActivatorEvent();
        if (type != null) compParent.enableEventListener(type);  // Lo haría indirectamente el registerActivatorEvent() pero así queda más claro y más simétrico respecto al afterShow
        registerActivatorEvent(); 

        DOMUtilInternal.removeAllChildren(cellElem); // Se quita el/los elemento/s editor/es, en teoría se detectará y automáticamente los demás listeners se quitan en el cliente
        if (children != null) cellElem.appendChild(children); // Restauramos el markup original antes de editar

        if (acceptNewValue)
        {
            CellEditor cellEditor = getCellEditor();
            Object value = cellEditor.getCellEditorValue();
            try
            {
                acceptNewValue(value);
            }
            finally
            {
                clearCurrentContext(); // para que quede bien se acepte o no el nuevo valor (podría ser vetado)
            }
        }
        else clearCurrentContext();

        this.children = null;
        this.cellElem = null;
        this.compEditor = null;
        this.editing = false;
    }

    public abstract void acceptNewValue(Object value);

    public abstract void clearCurrentContext();

    public void startEdition(Event evt)
    {
        if (prepareEdition(evt))
            openEditor(evt);
    }

    public boolean prepareEdition(Event evt)
    {
        // Finalizamos antes cualquier sesión pendiente
        // Esto es necesario pues se ha comprobado que algunas
        // veces no se genera el blur y se puede empezar a editar otro
        // dejando a medias el uno. Esto ocurre sobre todo en el Internet Explorer
        if (!prepareEdition())
            return false;

        return getCellEditor().isCellEditable((EventObject)evt);
    }

    public boolean prepareEdition()
    {
        if (!isEditingEnabled())
            return false; // Puede haber ya una sesión abierta que no quiere que la "molesten"

        finalizeEdition(false); // Terminar una posible sesión actualmente abierta

        CellEditor cellEditor = getCellEditor();
        return (cellEditor != null);
    }

    public boolean isEditingEnabled()
    {
        return editingEnabled;
    }

    public void setEditingEnabled(boolean editingEnabled)
    {
        this.editingEnabled = editingEnabled;
    }

    public boolean isEditable()
    {
        return (getCellEditor() != null);
    }

    public void beforeShow(Element cellElem)
    {
        this.cellElem = cellElem;
        this.children = DOMUtilInternal.extractChildren(cellElem); // Devuelve un DocumentFragment si hay varios hijos , puede ser null (vacío)
    }

    public void afterShow(ItsNatComponent compEditor)
    {
        // Quitamos el evento para que al hacer doble click sobre el componente
        // editor no suponga que al recibirlo el componente padre abra otro editor
        unregisterActivatorEvent();

        String type = getEditorActivatorEvent();
        if (type != null) compParent.disableEventListener(type);
        // disableEventListener es para evitar que se envíen eventos al componente padre mientras estamos editando (especialmente si el activador es "click").
        // Además soluciona un bug de edición inplace usando select combo en S60WebKit, en ese caso con click como activador
        // al hacer click en el select para cambiar la selección si el evento va al servidor el S60WebKit se lia
        // y no muestra la ventana de selección (quizás la muestra y la quita instantáneamente)

        this.compEditor = compEditor;
        this.editing = true;
    }

    protected abstract void openEditor(Event evt);

}
