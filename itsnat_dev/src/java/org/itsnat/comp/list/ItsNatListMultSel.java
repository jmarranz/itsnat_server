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

import java.util.List;
import javax.swing.ListSelectionModel;

/**
 * Is the base interface of multiple selection lists.
 *
 * <p>This component family uses a <code>javax.swing.ListSelectionModel</code> to keep
 * track of selection states. When a list item is selected (usually by clicking it) the selection
 * state is updated accordingly using the selection model (this one fires any listener registered).</p>
 *
 * <p>By default this component type uses a <code>javax.swing.DefaultListModel</code>
 * data model.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatListMultSel extends ItsNatList
{
    /**
     * Constructs a <code>javax.swing.ListModel</code> from an array of objects and then
     * applies {@link #setListModel(javax.swing.ListModel)} to it.
     *
     * <p>Submitted array must not change, because no changes are detected.</p>
     *
     * @param listData an array of Objects containing the items of the new list model
     * @see #setListData(java.util.List)
     */
    public void	setListData(Object[] listData);


    /**
     * Constructs a <code>javax.swing.ListModel</code> from a <code>java.util.List</code> and then
     * applies {@link #setListModel(javax.swing.ListModel)} to it.
     *
     * <p>Submitted list must not change, because no changes are detected.</p>
     *
     * @param listData a <code>java.util.List</code> containing the items of the new list model
     * @see #setListData(Object[])
     */
    public void	setListData(List<Object> listData);

    /**
     * Returns the current selection model.
     *
     * @return the current selection model. By default a <code>javax.swing.DefaultListSelectionModel</code> instance.
     * @see #setListSelectionModel(javax.swing.ListSelectionModel)
     */
    public ListSelectionModel getListSelectionModel();

    /**
     * Sets the new selection model.
     *
     * <p>If the new selection model is the current defined then is "reset",
     * component listener is removed and added again. Use this technique if
     * you want to add a listener to be executed <i>before</i> the default component listener.
     *
     * @param selectionModel the new selection model.
     * @see #getListSelectionModel()
     */
    public void setListSelectionModel(ListSelectionModel selectionModel);

    /**
     * Returns an array with indices of the current selected elements.
     *
     * @return an array with the current selected elements.
     * @see #setSelectedIndices(int[])
     */
    public int[] getSelectedIndices();


    /**
     * Sets the current selected elements.
     *
     * @param indices index array of the new selected elements.
     * @see #getSelectedIndices()
     */
    public void setSelectedIndices(int[] indices);

}
