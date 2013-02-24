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

package org.itsnat.comp.table;

import javax.swing.ListSelectionModel;
import org.itsnat.comp.*;

/**
 * Is the base interface of table header components.
 *
 * <p>A table header component depends on a table component and is created automatically
 * by the table whether the table markup has a header (for instance a &lt;thead&gt;).</p>
 *
 * <p>A header component is not very different to a list but column values (header data model)
 * are managed by the <code>javax.swing.table.TableModel</code> data model of the parent table
 * (see <code>DefaultTableModel.addColumn(Object)</code>).</code>
 *
 * <p>Any change to the header data model is notified to the component and the markup
 * is rendered again. The data model ever mandates over the markup,
 * any initial markup content (initial header cell) is removed.</p>
 *
 * <p>Almost a header cell must be present in the markup, this cell is used as pattern
 * to create new cells, and is removed because by default the header model is empty.</p>
 *
 * <p>If a header column is clicked the column is selected.</p>
 *
 * <p>There is no default header cell decoration when a complete column is selected,
 * use a column selection model listener to detect when a column is selected.</p>
 *
 * <p>Header cell indexes start in 0.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatTableHeader extends ItsNatElementComponent
{
    /**
     * Returns the table component this header belongs to.
     *
     * @return the parent table component. Never is null.
     */
    public ItsNatTable getItsNatTable();

    /**
     * Returns the user interface manager of this component.
     *
     * @return the user interface manager.
     */
    public ItsNatTableHeaderUI getItsNatTableHeaderUI();

    /**
     * Returns the current component renderer. This renderer converts a column value of the table header to markup.
     *
     * @return the current renderer. By default uses the default renderer ({@link ItsNatComponentManager#createDefaultItsNatTableHeaderCellRenderer()})
     * @see #setItsNatTableHeaderCellRenderer(ItsNatTableHeaderCellRenderer)
     */
    public ItsNatTableHeaderCellRenderer getItsNatTableHeaderCellRenderer();

    /**
     * Sets the component renderer.
     *
     * @param renderer the new renderer.
     * @see #getItsNatTableHeaderCellRenderer()
     */
    public void setItsNatTableHeaderCellRenderer(ItsNatTableHeaderCellRenderer renderer);

    /**
     * Returns the current header selection model. This selection model is used
     * to manage only the selection state of header columns.
     *
     * <p>By default if a header cell is clicked the selection model is used
     * to remember this selection, may be used to do typical tasks like to sort
     * the rows by the selected column.</p>
     *
     * @return the current selection model. By default a <code>javax.swing.DefaultListSelectionModel</code> instance
     *      in <code>ListSelectionModel.SINGLE_SELECTION</code> mode.
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
     * Returns the index of the first selected header column. If the component
     * allows multiple selection returns the first one.
     *
     * @return index of the first selected header column. -1 if none is selected.
     */
    public int getSelectedIndex();

    /**
     * Selects the specified header column. If this component allows multiple selection,
     * the current selection is cleared before.
     *
     * @param index the index of the item to select
     */
    public void setSelectedIndex(int index);
}
