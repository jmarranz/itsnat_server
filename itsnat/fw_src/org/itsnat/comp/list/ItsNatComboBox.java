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

import java.awt.ItemSelectable;
import java.awt.event.ItemListener;
import javax.swing.ComboBoxModel;

/**
 * Is the base interface of combo box components.
 *
 * <p>Items of combo boxes are not editable "in place", for instance a call
 * to {@link #setItsNatListCellEditor(ItsNatListCellEditor)} throws an
 * {@link org.itsnat.core.ItsNatException}.
 * </p>
 *
 * <p>By default this component type uses a <code>javax.swing.DefaultComboBoxModel</code>
 * data model.</p>
 *
 * <p>When the selection item changes the component notifies to all
 * <code>java.awt.event.ItemListener</code> listeners registered (this component
 * implements <code>java.awt.ItemSelectable</code>).</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatComboBox extends ItsNatList,ItemSelectable
{
    /**
     * Returns the current data model of this component.
     *
     * @return the current data model
     * @see #setComboBoxModel(javax.swing.ComboBoxModel)
     */
    public ComboBoxModel getComboBoxModel();

    /**
     * Changes the data model of this component.
     *
     * @param dataModel the new data model.
     * @see #getComboBoxModel()
     * @see #setListModel(javax.swing.ListModel)
     */
    public void setComboBoxModel(ComboBoxModel dataModel);

    /**
     * Adds a new value to the list model.
     *
     * <p>This method only works if the data model is a
     * <code>javax.swing.MutableComboBoxModel</code>.</p>
     *
     * @param anObject the value to add at the end of the list.
     */
    public void addElement(Object anObject);

    /**
     * Inserts a new value in the list model at the specified position.
     *
     * <p>This method only works if the data model is a
     * <code>javax.swing.MutableComboBoxModel</code>.</p>
     *
     * @param index index of the new list item.
     * @param anObject the value to add at the end of the list.
     */
    public void insertElementAt(int index,Object anObject);

    /**
     * Removes the specified value from the list model.
     *
     * <p>This method only works if the data model is a
     * <code>javax.swing.MutableComboBoxModel</code>.</p>
     *
     * @param anObject the value to remove.
     */
    public void removeElement(Object anObject);

    /**
     * Removes a list item at the specified index.
     *
     * <p>This method only works if the data model is a
     * <code>javax.swing.MutableComboBoxModel</code>.</p>
     *
     * @param index index of the item to remove.
     */
    public void removeElementAt(int index);

    /**
     * Removes all items.
     *
     * <p>This method only works if the data model is a
     * <code>javax.swing.MutableComboBoxModel</code>.</p>
     */
    public void removeAllElements();

    /**
     * Returns the current selected item.
     *
     * <p>This method delegates to <code>ComboBoxModel.getSelectedItem()</code>.</p>
     *
     * @return the current selected item or null if none is selected.
     */
    public Object getSelectedItem();

    /**
     * Sets the current selected item.
     *
     * <p>This method delegates to <code>ComboBoxModel.setSelectedItem(Object)</code>.</p>
     *
     * @param anObject the item value to select.
     */
    public void setSelectedItem(Object anObject);

    /**
     * Returns all <code>ItemListener</code> objects registered.
     *
     * @return an array with the registered listeners.
     */
    public ItemListener[] getItemListeners();
}
