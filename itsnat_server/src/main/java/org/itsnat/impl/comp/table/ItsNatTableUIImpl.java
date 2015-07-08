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

package org.itsnat.impl.comp.table;

import org.itsnat.impl.comp.ItsNatElementComponentUIImpl;
import org.itsnat.comp.table.ItsNatTable;
import org.itsnat.comp.table.ItsNatTableHeaderUI;
import org.itsnat.impl.core.domutil.ElementTableImpl;
import java.util.ArrayList;
import java.util.Iterator;
import org.itsnat.comp.table.ItsNatTableCellRenderer;
import org.itsnat.comp.table.ItsNatTableHeader;
import org.itsnat.comp.table.ItsNatTableStructure;
import org.itsnat.comp.table.ItsNatTableCellUI;
import org.itsnat.comp.table.ItsNatTableUI;
import org.itsnat.impl.core.domutil.ElementGroupManagerImpl;
import org.itsnat.impl.core.domutil.ElementListBaseImpl;
import org.itsnat.impl.core.domutil.ElementListImpl;
import org.itsnat.impl.core.domutil.TableCellElementInfoMasterImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatTableUIImpl extends ItsNatElementComponentUIImpl implements ItsNatTableUI
{
    protected boolean enabled = true;
    protected ElementTableImpl tableMgr;

    /**
     * Creates a new instance of ItsNatTableUIImpl
     */
    public ItsNatTableUIImpl(ItsNatTableImpl parentComp)
    {
        super(parentComp);

        ItsNatTableStructure structure = parentComp.getItsNatTableStructure();
        Element tableElem = parentComp.getElement();
        Element bodyElem = structure.getBodyElement(parentComp,tableElem);

        ItsNatTableStructureCoreAdapterImpl structAdapter;
        structAdapter = new ItsNatTableStructureCoreAdapterImpl(structure,parentComp,null);

        ElementGroupManagerImpl factory = getItsNatDocumentImpl().getElementGroupManagerImpl();
        this.tableMgr = factory.createElementTableInternal(bodyElem,true,structAdapter,null);
    }

    public Element getBodyElement()
    {
        return tableMgr.getParentElement(); // El <tbody> en el caso de <table>
    }

    public ItsNatTableHeaderUI getItsNatTableHeaderUI()
    {
        ItsNatTableHeader header = getItsNatTable().getItsNatTableHeader();
        if (header == null)
            return null;
        return header.getItsNatTableHeaderUI();
    }

    public ItsNatTable getItsNatTable()
    {
        return (ItsNatTable)parentComp;
    }

    public ItsNatTableCellRenderer getItsNatTableCellRenderer()
    {
        return getItsNatTable().getItsNatTableCellRenderer();
    }

    public ItsNatTableCellUI getItsNatTableCellUIFromNode(Node node)
    {
        TableCellElementInfoMasterImpl cellInfo = (TableCellElementInfoMasterImpl)tableMgr.getTableCellElementInfoFromNode(node);
        return ItsNatTableCellUIImpl.getItsNatTableCellUI(cellInfo,this);
    }

    public ItsNatTableCellUI getItsNatTableCellUIAt(int row,int column)
    {
        TableCellElementInfoMasterImpl cellInfo = (TableCellElementInfoMasterImpl)tableMgr.getTableCellElementInfoAt(row,column);
        return ItsNatTableCellUIImpl.getItsNatTableCellUI(cellInfo,this);
    }

    public int getRowCount()
    {
        return tableMgr.getRowCount();
    }

    public int getColumnCount()
    {
        return tableMgr.getColumnCount();
    }

    /**
     * ESTE METODO ANTES ERA PUBLICO
     * 
     * Increases or shrinks the number of rows to fit the new size.
     *
     * <p>If the new size is bigger new row elements are added at the end, if the size
     * is lower tail row elements are removed.</p>
     *
     * @param rows the new number of rows.
     * @see org.itsnat.core.domutil.ElementTable#setRowCount(int)
     */
    public void setRowCount(int rows)
    {
        tableMgr.setRowCount(rows);
    }
    
    /**
     * ESTE METODO ANTES ERA PUBLICO
     * Increases or shrinks the number of columns to fit the new size.
     *
     * <p>If the new size is bigger new columns are added at the end, if the size
     * is lower tail columns are removed.</p>
     *
     * @param columns the new number of columns.
     * @see org.itsnat.core.domutil.ElementTable#setColumnCount(int)
     */
    public void setColumnCount(int columns)
    {
        tableMgr.setColumnCount(columns);
    }

    /**
     * ESTE METODO ANTES ERA PUBLICO
     *
     * Renders the specified value into the cell element with the given row and column position.
     *
     * @param row row of the cell.
     * @param column column of the cell.
     * @param value the value to render.
     * @see org.itsnat.core.domutil.ElementTable#setCellValueAt(int,int,Object)
     */
    public void setCellValueAt(int rowIndex,int columnIndex,Object aValue,boolean isSelected,boolean hasFocus)
    {
        Element cellElem = getCellElementAt(rowIndex,columnIndex);
        setElementValueAt(rowIndex,columnIndex,aValue,isSelected,hasFocus,cellElem,false);
    }

    public void setElementValueAt(int rowIndex,int columnIndex,Object aValue,boolean isSelected,boolean hasFocus,Element cellElem,boolean isNew)
    {
        Element cellContentElem = tableMgr.getCellContentElementAt(rowIndex,columnIndex,cellElem);

        tableMgr.prepareRendering(cellContentElem,isNew);

        ItsNatTableCellRenderer renderer = getItsNatTableCellRenderer();
        if (renderer != null)
            renderer.renderTableCell(getItsNatTable(),rowIndex,columnIndex,aValue,isSelected,hasFocus,cellContentElem,isNew);
    }

    /**
     * ESTE METODO ANTES ERA PUBLICO
     *
     * Inserts a new row element at the specified position rendering
     * the specified cell values.
     *
     * @param row row index of the new row element.
     * @param values the cell values to render as markup.
     * @return the parent element of the new row.
     * @see org.itsnat.core.domutil.ElementTable#insertRowAt(int,Object[])
     */
    public Element insertRowAt(int row,Object[] values)
    {
        Element rowElem = tableMgr.insertRowAt(row);

        boolean[] selected = null;
        boolean[] hasFocus = null;
        if (values!= null)
        {
            selected = new boolean[values.length];
            hasFocus = new boolean[values.length];
        }
        setRowValuesAt(row,values,selected,hasFocus,rowElem,true);
        return rowElem;
    }

    /**
     * ESTE METODO ANTES ERA PUBLICO
     *
     * Renders the specified values into the row element with the given position.
     *
     * @param row index of the row element.
     * @param values the row values to render.
     * @param isSelected array with the current selection state of the row cells.
     * @param hasFocus array with the current focus state of the row cells. Current ItsNat implementation ever passes false values.
     * @see org.itsnat.core.domutil.ElementTable#setRowValuesAt(int,Object[])
     */
    public void setRowValuesAt(int row,Object[] values,boolean[] selected,boolean[] hasFocus)
    {
        Element rowElem = tableMgr.getRowElementAt(row);
        setRowValuesAt(row,values,selected,hasFocus,rowElem,false);
    }

    protected void setRowValuesAt(int row,Object[] values,boolean[] selected,boolean[] hasFocus,Element rowElem,boolean isNew)
    {
        if (values != null)
        {
            ElementListImpl columns = (ElementListImpl)tableMgr.getColumnsOfRowElementList(row,rowElem);
            // Si la longitud de values es menor que el número de columnas dará error
            int i = 0;
            for(Element columElem : columns.getInternalElementListFree())
            {
                setElementValueAt(row,i,values[i],selected[i],hasFocus[i],columElem,isNew);

                i++;
            }
        }
    }

    /**
     * ESTE METODO ANTES ERA PUBLICO
     *
     * Inserts a new column at the specified position,
     * and renders the specified values.
     *
     * @param column index of the new column.
     * @param values the column values to render.
     * @see org.itsnat.core.domutil.ElementTable#insertColumnAt(int,Object[])
     */
    public void insertColumnAt(int column,Object[] columnData)
    {
        tableMgr.insertColumnAt(column);

        boolean[] selected = null;
        boolean[] hasFocus = null;
        if (columnData!= null)
        {
            selected = new boolean[columnData.length];
            hasFocus = new boolean[columnData.length];
        }

        setColumnValuesAt(column,columnData,selected,hasFocus,true);
    }

    /**
     * ESTE METODO ANTES ERA PUBLICO
     *
     * Renders the specified values into the column with the given position.
     *
     * @param column index of the column.
     * @param values the column values to render.
     * @param isSelected array with the current selection state of the column cells.
     * @param hasFocus array with the current focus state of the column cells. Current ItsNat implementation ever passes false values.
     * @see org.itsnat.core.domutil.ElementTable#setColumnValuesAt(int,Object[])
     */
    public void setColumnValuesAt(int column,Object[] columnData,boolean[] selected,boolean[] hasFocus)
    {
        setColumnValuesAt(column,columnData,selected,hasFocus,false);
    }

    public void setColumnValuesAt(int column,Object[] columnData,boolean[] selected,boolean[] hasFocus,boolean isNew)
    {
        if (columnData != null)
        {
            ElementListImpl rows = tableMgr.getRowElementList();

            if (!rows.isEmpty())
            {
                // Si la longitud de columnData es menor que el número de columnas dará error
                int row = 0;
                for(Element rowElem : rows.getInternalElementListFree())
                {
                    ElementListBaseImpl columns = tableMgr.getColumnsOfRowElementList(row,rowElem);
                    Element cellElem = columns.getElementAt(column);
                    setElementValueAt(row,column,columnData[row],selected[row],hasFocus[row],cellElem,isNew);

                    row++;
                }
            }
        }
    }

    public void unrenderCell(int rowIndex,int columnIndex)
    {
        ItsNatTableCellRenderer renderer = getItsNatTableCellRenderer();
        if (renderer == null) return;

        Element cellElem = getCellElementAt(rowIndex,columnIndex);
        Element cellContentElem = tableMgr.getCellContentElementAt(rowIndex,columnIndex,cellElem);
        renderer.unrenderTableCell(getItsNatTable(),rowIndex,columnIndex,cellContentElem);
    }

    public void unrenderAllCells()
    {
        ItsNatTableCellRenderer renderer = getItsNatTableCellRenderer();
        if (renderer == null) return;

        int rowCount = getRowCount();
        int colCount = getColumnCount();
        for(int row = 0; row < rowCount; row++)
            for(int col = 0; col < colCount; col++)
                unrenderCell(row,col);
    }

    /**
     * ESTE METODO ANTES ERA PUBLICO
     *
     * Removes the specified row element.
     *
     * @param row index of the row element to remove.
     * @see org.itsnat.core.domutil.ElementTableBase#removeRowAt(int)
     */
    public void removeRowAt(int row)
    {
        ItsNatTableCellRenderer renderer = getItsNatTableCellRenderer();
        if (renderer != null)
        {
            int colCount = getColumnCount();
            for(int col = 0; col < colCount; col++)
                unrenderCell(row,col);
        }

        tableMgr.removeRowAt(row);
    }

    /**
     * ESTE METODO ANTES ERA PUBLICO
     *
     * Removes the specified column.
     *
     * @param column index of the column to remove.
     * @see org.itsnat.core.domutil.ElementTableBase#removeColumnAt(int)
     */
    public void removeColumnAt(int column)
    {
        ItsNatTableCellRenderer renderer = getItsNatTableCellRenderer();
        if (renderer != null)
        {
            int rowCount = getRowCount();
            for(int row = 0; row < rowCount; row++)
                unrenderCell(row,column);
        }

        tableMgr.removeColumnAt(column);
    }

    /**
     * ESTE METODO ANTES ERA PUBLICO
     *
     * Removes all row elements. The table body is now empty.
     *
     * @see org.itsnat.core.domutil.ElementTableBase#removeAllRows()
     */
    public void removeAllRows()
    {
        unrenderAllCells();

        tableMgr.removeAllRows();
    }

    /**
     * ESTE METODO ANTES ERA PUBLICO
     *
     * Removes all columns. The table body remains as a row list with no cells.
     *
     * @see org.itsnat.core.domutil.ElementTableBase#removeAllColumns()
     */
    public void removeAllColumns()
    {
        unrenderAllCells();

        tableMgr.removeAllColumns();
    }

    public Element getRowElementAt(int row)
    {
        return tableMgr.getRowElementAt(row);
    }

    public Element getRowContentElementAt(int row)
    {
        return tableMgr.getRowContentElementAt(row);
    }

    public Element getCellElementAt(int row, int column)
    {
        return tableMgr.getCellElementAt(row,column);
    }

    public Element getCellContentElementAt(int row, int column)
    {
        return tableMgr.getCellContentElementAt(row,column);
    }

    public Element[] getCellElementsOfRow(int row)
    {
        return tableMgr.getCellElementsOfRow(row);
    }

    public Element[] getCellElementsOfColumn(int column)
    {
        return tableMgr.getCellElementsOfColumn(column);
    }

    public boolean isUsePatternMarkupToRender()
    {
        return tableMgr.isUsePatternMarkupToRender();
    }

    public void setUsePatternMarkupToRender(boolean value)
    {
        tableMgr.setUsePatternMarkupToRender(value);
    }

    public void setEnabled(boolean b)
    {
        this.enabled = b;
    }

    public boolean isEnabled()
    {
        return enabled;
    }
}
