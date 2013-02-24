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
import javax.swing.table.TableModel;
import org.itsnat.comp.ItsNatElementComponent;


/**
 * Is the base interface of table based components.
 *
 * <p>A table component manages a <code>javax.swing.table.TableModel</code>
 * data model, table cell values are rendered as markup using a special object, the renderer,
 * and may be optionally edited "in place" using a user defined editor.</p>
 *
 * <p>Any change to the data model is notified to the component and the markup
 * is rendered again. The data model ever mandates over the markup,
 * any initial markup content (initial rows) is removed.</p>
 *
 * <p>Almost a row with a cell must be present in the markup, this row and cell are used as patterns
 * to create new table rows/cells, and is removed because by default the data model is empty.</p>
 *
 * <p>This component family uses two <code>javax.swing.ListSelectionModel</code> selection models,
 * one for columns, another for rows, to keep track of selection states.
 * When a cell is selected (usually by clicking it) the selection
 * state is updated accordingly using both selection models (notifying to registered listeners).
 *
 * <p>ItsNat tables support all selection modes that Swing JTable supports,
 * as in JTable {@link #setRowSelectionAllowed(boolean)} and {@link #setColumnSelectionAllowed(boolean)}
 * control whether the complete row and column are selected when a cell is selected.</p>
 *
 * <p>There is no default decoration of table cell selection,
 * selection model listeners may be used to decorate the table markup when its
 * selection state changes.</p>
 *
 * <p>By default this component uses the default renderer and editor and
 * a <code>javax.swing.table.DefaultTableModel</code> data model.</p>
 *
 * <p>Row and column indexes start in 0.</p>
 *
 * <p>This component family is the "componentized" version of {@link org.itsnat.core.domutil.ElementTable} and
 * follows a similar philosophy (note the component version provides table header support).</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatTable extends ItsNatElementComponent
{
    /**
     * Returns the current data model of this component.
     *
     * @return the current data model
     * @see #setTableModel(javax.swing.table.TableModel)
     */
    public TableModel getTableModel();

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
     */
    public void setTableModel(TableModel dataModel);

    /**
     * Returns the table header sub-component.
     *
     * @return the table header. Null if this table does not have a header.
     */
    public ItsNatTableHeader getItsNatTableHeader();

    /**
     * Returns the user interface manager of this component.
     *
     * @return the user interface manager.
     */
    public ItsNatTableUI getItsNatTableUI();

    /**
     * Creates a data model instance appropriated to this component. This instance
     * is not bound to the component.
     *
     * @return a new data model instance.
     */
    public TableModel createDefaultTableModel();

    /**
     * Returns the current row selection model.
     *
     * @return the current row selection model. By default a <code>javax.swing.DefaultListSelectionModel</code> instance.
     * @see #setRowSelectionModel(javax.swing.ListSelectionModel)
     */
    public ListSelectionModel getRowSelectionModel();

    /**
     * Sets the new row selection model.
     *
     * <p>If the new selection model is the current defined then is "reset",
     * component listener is removed and added again. Use this technique if
     * you want to add a listener to be executed <i>before</i> the default component listener.
     *
     * @param selectionModel the new row selection model.
     * @see #getRowSelectionModel()
     */
    public void setRowSelectionModel(ListSelectionModel selectionModel);

    /**
     * Returns the current column selection model.
     *
     * @return the current column selection model. By default a <code>javax.swing.DefaultListSelectionModel</code> instance.
     * @see #setColumnSelectionModel(javax.swing.ListSelectionModel)
     */
    public ListSelectionModel getColumnSelectionModel();

    /**
     * Sets the new column selection model.
     *
     * <p>If the new selection model is the current defined then is "reset",
     * component listener is removed and added again. Use this technique if
     * you want to add a listener to be executed <i>before</i> the default component listener.
     *
     * @param selectionModel the new column selection model.
     * @see #getColumnSelectionModel()
     */
    public void setColumnSelectionModel(ListSelectionModel selectionModel);

    /**
     * Clears any row or column selection.
     *
     * <p>Calls <code>ListSelectionModel.clearSelection()</code> method
     * of both, row and column, selection models.
     */
    public void clearSelection();

    /**
     * Sets a new selection mode to the row and column selection models.
     *
     * <p>Calls <code>ListSelectionModel.setSelectionMode()</code> method
     * of both, row and column, selection models. </p>
     *
     * @param selectionMode new selection mode value.
     */
    public void setSelectionMode(int selectionMode);

    /**
     * Informs whether row selection is allowed.
     *
     * @return if row selection is allowed, true by default.
     * @see #setRowSelectionAllowed(boolean)
     */
    public boolean isRowSelectionAllowed();

    /**
     * Sets whether row selection is allowed.
     *
     * @param rowSelectionAllowed true if row selection is allowed.
     * @see #isRowSelectionAllowed()
     * @see #isCellSelected(int,int)
     */
    public void setRowSelectionAllowed(boolean rowSelectionAllowed);

    /**
     * Informs whether column selection is allowed.
     *
     * @return if column selection is allowed, true by default.
     * @see #setColumnSelectionAllowed(boolean)
     */
    public boolean isColumnSelectionAllowed();

    /**
     * Sets whether column selection is allowed.
     *
     * @param columnSelectionAllowed true if column selection is allowed.
     * @see #isColumnSelectionAllowed()
     * @see #isCellSelected(int,int)
     */
    public void setColumnSelectionAllowed(boolean columnSelectionAllowed);

    /**
     * Informs whether cell selection is enabled. A cell may be selected
     * if row or column (or both) selection is enabled.
     *
     * @return if cell selection is enabled. By default is true.
     * @see #setCellSelectionEnabled(boolean)
     */
    public boolean isCellSelectionEnabled();

    /**
     * Enables/disables cell selection. If cell selection is enabled
     * row and column selection are enabled too (if disabled are disabled too).
     *
     * @param cellSelectionEnabled true if cell selection is enabled.
     * @see #isCellSelectionEnabled()
     */
    public void setCellSelectionEnabled(boolean cellSelectionEnabled);

    /**
     * Informs whether the specified cell is selected.
     *
     * <p>A cell is selected if cell selection is enabled ({@link #isCellSelectionEnabled()} returns true)
     * and row selection OR column selection is enabled (not both) and the cell is
     * part of a selected row or column. If row selection AND column selection
     * are enabled the cell is selected only if cell row AND column are selected,
     * this mode (row and column selection enabled) allows selecting individual cells.</p>
     *
     * <p>Summary:</p>
     * <ul>
     *   <li>To select complete rows: row selection enabled and column disabled.
     *      A single cell selection selects all remaining cells in the row</li>
     *   <li>To select complete columns: column selection enabled and row disabled.</li>
     *      A single cell selection selects all remaining cells in the column</li>
     *   <li>To select individual cells: column and row selection enabled.</li>
     * </ul>
     *
     * @param row row position of the cell.
     * @param column column position of the cell.
     * @return true if the specified cell is selected.
     * @see #isCellSelectionEnabled()
     */
    public boolean isCellSelected(int row, int column);

    /**
     * Informs whether the specified row contains almost a cell selected.
     *
     * <p>This method simply delegates to <code>getRowSelectionModel().isSelectedIndex(row)</code>.</p>
     *
     * @param row the row index.
     * @return true if the specified row is selected.
     */
    public boolean isRowSelected(int row);

    /**
     * Informs whether the specified column contains almost a cell selected.
     *
     * <p>This method simply delegates to <code>getColumnSelectionModel().isSelectedIndex(column)</code>.</p>
     *
     * @param column the column index.
     * @return true if the specified column is selected.
     */
    public boolean isColumnSelected(int column);

    /**
     * Sets a new row selection interval specifying an index range.
     *
     * <p>This method simply delegates to <code>getRowSelectionModel().setSelectionInterval(index0,index1)</code>.</p>
     *
     * @param index0 one end of the interval.
     * @param index1 other end of the interval
     */
    public void setRowSelectionInterval(int index0, int index1);

    /**
     * Sets a new column selection interval specifying an index range.
     *
     * <p>This method simply delegates to <code>getColumnSelectionModel().setSelectionInterval(index0,index1)</code>.</p>
     *
     * @param index0 one end of the interval.
     * @param index1 other end of the interval
     */
    public void setColumnSelectionInterval(int index0, int index1);

    /**
     * Returns an array with indices of the current selected rows.
     *
     * @return an array with the current selected rows.
     * @see #setSelectedRows(int[])
     */
    public int[] getSelectedRows();

    /**
     * Sets the current selected rows.
     *
     * @param indices index array of the new selected rows.
     * @see #getSelectedRows()
     */
    public void setSelectedRows(int[] indices);

    /**
     * Returns an array with indices of the current selected columns.
     *
     * @return an array with the current selected columns.
     * @see #setSelectedColumns(int[])
     */
    public int[] getSelectedColumns();

    /**
     * Sets the current selected columns.
     *
     * @param indices index array of the new selected columns.
     * @see #getSelectedColumns()
     */
    public void setSelectedColumns(int[] indices);

    /**
     * Returns the index of the first selected row. If several rows are
     * selected returns the first one.
     *
     * @return index of the first selected row. -1 if none is selected.
     */
    public int getSelectedRow();

    /**
     * Returns the index of the first selected column. If several columns are
     * selected returns the first one.
     *
     * @return index of the first selected column. -1 if none is selected.
     */
    public int getSelectedColumn();

    /**
     * Updates the selection models of the table, depending on the state of the
     * two flags: <code>toggle</code> and <code>extend</code>. Most changes
     * to the selection are the result of mouse events received
     * by the UI are channeled through this method.
     * <p>
     * This implementation uses the following conventions:
     * <ul>
     * <li> <code>toggle</code>: <em>false</em>, <code>extend</code>: <em>false</em>.
     *      Clear the previous selection and ensure the new cell is selected.
     *      This state is used to change the selection by clicking a cell (no shift and ctrl keys held)
     * <li> <code>toggle</code>: <em>false</em>, <code>extend</code>: <em>true</em>.
     *      Extend the previous selection from the anchor to the specified cell,
     *      clearing all other selections.
     *      This state is used to change the selection by clicking a cell with shift key held.
     * <li> <code>toggle</code>: <em>true</em>, <code>extend</code>: <em>false</em>.
     *      If the specified cell is selected, deselect it. If it is not selected, select it.
     *      This state is used to change the selection by clicking a cell with ctrl key held.
     * <li> <code>toggle</code>: <em>true</em>, <code>extend</code>: <em>true</em>.
     *      Leave the selection state as it is, but move the anchor index to the specified location.
     *      This state is used to change the selection by clicking a cell with shift and ctrl keys held.
     * </ul>
     *
     * <p>Note: original documentation (modified) obtained from <code>JTable.changeSelection</code> (Oracle JDK 1.5).</p>
     *
     * @param  rowIndex   affects the selection at <code>row</code>
     * @param  columnIndex  affects the selection at <code>column</code>
     * @param  toggle  see description above
     * @param  extend  if true, extend the current selection
     */
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend);

    /**
     *  Selects all cells in the table.
     */
    public void selectAll();

    /**
     * Returns the component structure.
     *
     * @return the component structure.
     */
    public ItsNatTableStructure getItsNatTableStructure();

    /**
     * Returns the current component renderer. This renderer converts a table cell value to markup.
     *
     * @return the current renderer. By default uses the default renderer ({@link org.itsnat.comp.ItsNatComponentManager#createDefaultItsNatTableCellRenderer()})
     * @see #setItsNatTableCellRenderer(ItsNatTableCellRenderer)
     */
    public ItsNatTableCellRenderer getItsNatTableCellRenderer();

    /**
     * Sets the component renderer.
     *
     * @param renderer the new renderer.
     * @see #getItsNatTableCellRenderer()
     */
    public void setItsNatTableCellRenderer(ItsNatTableCellRenderer renderer);

    /**
     * Returns the current table cell editor. This object is used to edit in place
     * a table cell value.
     *
     * @return the current editor. By default uses the default editor calling ({@link org.itsnat.comp.ItsNatComponentManager#createDefaultItsNatTableCellEditor(ItsNatComponent)}) with a null parameter.
     * @see #setItsNatTableCellEditor(ItsNatTableCellEditor)
     */
    public ItsNatTableCellEditor getItsNatTableCellEditor();

    /**
     * Sets the table cell editor.
     *
     * <p>Table cell edition works very much the same as label edition
     * (see {@link org.itsnat.comp.label.ItsNatLabel#setItsNatLabelEditor(ItsNatLabelEditor)}).</p>
     *
     * <p>Some differences:</p>
     *
     * <p>The edition process starts programmatically by calling {@link #startEditingAt(int,int)}.</p>
     *
     * <p>The edition takes place inside the cell <i>content</i> element
     * as returned by {@link ItsNatTableStructure#getCellContentElement(ItsNatTable,int,int,org.w3c.dom.Element)}.</p>
     *
     * <p>The new cell value is set to the data model
     * calling <code>javax.swing.table.TableModel.setValueAt(Object,int,int)</code>.</p>
     *
     * @param editor the new editor. May be null (edition disabled).
     * @see #getItsNatTableCellEditor()
     */
    public void setItsNatTableCellEditor(ItsNatTableCellEditor editor);

    /**
     * Used to start programmatically a table cell edition process "in place".
     *
     * @see #isEditing()
     */
    public void startEditingAt(int row,int column);

    /**
     * Informs whether a table cell value is being edited.
     *
     * @return true if a table cell value is being edited.
     *
     * @see #startEditingAt(int,int)
     */
    public boolean isEditing();

    /**
     * Returns the row of the cell being edited.
     *
     * @return the row of the cell being edited. -1 if none is being edited.
     */
    public int getEditingRow();

    /**
     * Returns the column of the cell being edited.
     *
     * @return the column of the cell being edited. -1 if none is being edited.
     */
    public int getEditingColumn();

    /**
     * Returns the event type used to activate the table cell edition process by the user.
     *
     * <p>If returns null edition activated by events is disabled .</p>
     *
     * @return the event type used to activate the edition. By default is "dblclick".
     * @see #setEditorActivatorEvent(String)
     */
    public String getEditorActivatorEvent();

    /**
     * Sets the event type used to activate the table cell edition process by the user.
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

    /**
     * Returns if the keyboard is used to select items.
     *
     * <p>If false ItsNat simulates the CTRL key is ever pressed
     * when an item is selected.
     * </p>
     * <p>This feature is useful for mobile devices without keyboard and tactile
     * screens to select multiple items (if set to false).
     * </p>
     * <p>Default value is defined by {@link org.itsnat.comp.ItsNatComponentManager#isSelectionOnComponentsUsesKeyboard()}
     * </p>
     *
     * @return the current state.
     * @see #setSelectionUsesKeyboard(boolean)
     */
    public boolean isSelectionUsesKeyboard();

    /**
     * Sets if the keyboard is used to select items.
     *
     * @param value the new state.
     * @see #isSelectionUsesKeyboard()
     */
    public void setSelectionUsesKeyboard(boolean value);

    /**
     * Informs whether a joystick is enough to control the component
     * (some kind of mouse, pointer or stylus not present or not necessary).
     *
     * <p>If the component is in joystick mode, the "content element" of every
     * table cell has an event listener associated. By this way table cells
     * are "live" elements and can traversed using a joystick in mobile devices
     * without a mouse, pointer or stylus.
     * </p>
     *
     * <p>Default value is defined by {@link org.itsnat.core.ItsNatDocument#isJoystickMode()}
     * </p>
     *
     * @return true if joystick mode is on.
     * @see #setJoystickMode(boolean)
     * @see ItsNatTableStructure#getCellContentElement(ItsNatTable, int, int, org.w3c.dom.Element)
     */
    public boolean isJoystickMode();

    /**
     * Informs whether a joystick is enough to control the component
     * (some kind of mouse, pointer or stylus not present or not necessary).
     *
     * @param value true to enable joystick mode.
     * @see #isJoystickMode()
     */
    public void setJoystickMode(boolean value);
}
