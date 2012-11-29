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

package org.itsnat.comp.list;

import org.itsnat.comp.*;
import org.itsnat.comp.list.ItsNatListUI;
import javax.swing.ListModel;

/**
 * Is the base interface of list based components.
 *
 * <p>A generic list component manages a <code>javax.swing.ListModel</code>
 * data list, item values are rendered as markup using a special object, the renderer,
 * and may be optionally edited "in place" using a user defined editor.</p>
 *
 * <p>Any change to the data model is notified to the component and the markup
 * is rendered again. The data model ever mandates over the markup, any initial markup content (initial child elements) is removed.</p>
 *
 * <p>This component family is the "componentized" version of {@link org.itsnat.core.domutil.ElementList} and
 * follows a similar philosophy.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatList extends ItsNatElementComponent
{
    /**
     * Returns the user interface manager of this component.
     *
     * @return the user interface manager.
     */
    public ItsNatListUI getItsNatListUI();

    /**
     * Returns the current data model of this component.
     *
     * @return the current data model
     * @see #setListModel(javax.swing.ListModel)
     */
    public ListModel getListModel();

    /**
     * Changes the data model of this component.
     *
     * <p>Current data model is disconnected from this component, and the new
     * data model is bound to this component, every change is tracked and
     * updates the user interfaces accordingly.</p>
     *
     * <p>If the specified data model is the same instance as the current data model,
     * then is reset, component listener is removed and added again. Use this technique if
     * you want to add a data model listener to be executed <i>before</i> the default component listener.
     *
     * @param dataModel the new data model.
     * @see #getListModel()
     */
    public void setListModel(ListModel dataModel);

    /**
     * Creates a data model instance appropriated to this component. This instance
     * is not bound to the component.
     *
     * @return a new data model instance.
     */
    public ListModel createDefaultListModel();

    /**
     * Returns the index of the first selected list item. If the component
     * allows multiple selection returns the first one.
     *
     * @return index of the first selected list item. -1 if none is selected.
     */
    public int getSelectedIndex();

    /**
     * Selects the specified list item. If this component allows multiple selection,
     * the current selection is cleared before.
     *
     * @param index the index of the item to select
     */
    public void setSelectedIndex(int index);

    /**
     * Returns the zero based position in the list of the specified value.
     * If the value is repeated returns the first position.
     *
     * @param obj the object value to search.
     * @return the zero based position of the value, -1 if not in the list.
     */
    public int indexOf(Object obj);

    /**
     * Returns the component structure.
     *
     * @return the component structure.
     */
    public ItsNatListStructure getItsNatListStructure();

    /**
     * Returns the current component renderer. This renderer converts a list item value to markup.
     *
     * @return the current renderer. By default uses the default renderer ({@link ItsNatComponentManager#createDefaultItsNatListCellRenderer()})
     * @see #setItsNatListCellRenderer(ItsNatListCellRenderer)
     */
    public ItsNatListCellRenderer getItsNatListCellRenderer();

    /**
     * Sets the component renderer.
     *
     * @param renderer the new renderer.
     * @see #getItsNatListCellRenderer()
     */
    public void setItsNatListCellRenderer(ItsNatListCellRenderer renderer);

    /**
     * Returns the current list item editor. This object is used to edit in place
     * a list item value.
     *
     * @return the current editor. By default uses the default editor
     *      calling ({@link ItsNatComponentManager#createDefaultItsNatListCellEditor(ItsNatComponent)}) with a null parameter.
     * @see #setItsNatListCellEditor(ItsNatListCellEditor)
     */
    public ItsNatListCellEditor getItsNatListCellEditor();

    /**
     * Sets the list item editor.
     *
     * <p>List item edition works very much the same as label edition
     * (see {@link org.itsnat.comp.label.ItsNatLabel#setItsNatLabelEditor(ItsNatLabelEditor)}).</p>
     *
     * <p>Some differences:</p>
     *
     * <p>The edition process starts programmatically by calling {@link #startEditingAt(int)}.</p>
     *
     * <p>The edition takes place inside the list item <i>content</i> element
     * as returned by {@link ItsNatListStructure#getContentElement(ItsNatList,int,org.w3c.dom.Element)}.</p>
     *
     * <p>The new item value is set to the data model
     * calling <code>javax.swing.DefaultListModel.setElementAt(Object,int)</code>,
     * if the data model is not <code>DefaultListModel</code> is the programmer responsibility
     * to set the new value to the data model (detecting when the editor stops editing,
     * see <code>javax.swing.CellEditor.addCellEditorListener(javax.swing.event.CellEditorListener)</code>).
     * </p>
     *
     * @param editor the new editor. May be null (edition disabled).
     * @see #getItsNatListCellEditor()
     */
    public void setItsNatListCellEditor(ItsNatListCellEditor editor);

    /**
     * Used to start programmatically a list item edition process "in place".
     *
     * @see #isEditing()
     */
    public void startEditingAt(int index);

    /**
     * Informs whether a list item value is being edited.
     *
     * @return true if a list item value is being edited.
     *
     * @see #startEditingAt(int)
     */
    public boolean isEditing();

    /**
     * Returns the index of the list item being edited.
     *
     * @return the index of the list item being edited. -1 if none is being edited.
     */
    public int getEditingIndex();

    /**
     * Returns the event type used to activate the list item edition process by the user.
     *
     * <p>If returns null edition activated by events is disabled .</p>
     *
     * @return the event type used to activate the edition. By default is "dblclick".
     * @see #setEditorActivatorEvent(String)
     */
    public String getEditorActivatorEvent();

    /**
     * Sets the event type used to activate the list item edition process by the user.
     *
     * @param eventType the event type used to activate the edition.
     * @see #getEditorActivatorEvent()
     */
    public void setEditorActivatorEvent(String eventType);

    /**
     * Informs whether the in place edition is enabled.
     *
     * @return false if the editing in place is temporally disabled. True by default.
     * @see #setEditingEnabled(boolean)
     */
    public boolean isEditingEnabled();

    /**
     * Enables or disables temporally the in place edition.
     *
     * @param value true to enable in place edition.
     * @see #isEditingEnabled()
     */
    public void setEditingEnabled(boolean value);
}
