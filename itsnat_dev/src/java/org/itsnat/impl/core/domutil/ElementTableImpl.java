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

package org.itsnat.impl.core.domutil;

import org.itsnat.core.ItsNatException;
import org.itsnat.core.domutil.ElementTable;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.itsnat.core.domutil.ElementTableRenderer;
import org.itsnat.core.domutil.ElementTableStructure;
import org.itsnat.core.domutil.ListElementInfo;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ElementTableImpl extends ElementTableBaseImpl implements ElementTable
{
    protected ElementTableRenderer renderer;
    protected ElementTableStructure structure;
    protected ElementListImpl columnListPattern;
    protected DocumentFragment cellContentPatternFragment; // Será recordado como patrón del contenido
    protected boolean usePatternMarkupToRender;

    /**
     * Creates a new instance of ElementTableImpl
     */
    public ElementTableImpl(ItsNatDocumentImpl itsNatDoc,Element parentElement,boolean removePattern,ElementTableStructure structure,ElementTableRenderer renderer)
    {
        super(itsNatDoc,parentElement);

        this.structure = structure;
        this.renderer = renderer;
        this.usePatternMarkupToRender = itsNatDoc.isUsePatternMarkupToRender();

        this.columnListOfRow = new ArrayList(); // Siempre es modo master

        ElementGroupManagerImpl factory = getItsNatDocumentImpl().getElementGroupManagerImpl();
        this.rows = factory.createElementListNoRenderInternal(parentElement,null,!removePattern,false);

        Element rowContentParentPattern = structure.getRowContentElement(this,0,getRowPatternElement());
        this.columnListPattern = factory.createElementListNoRenderInternal(rowContentParentPattern,null,!removePattern,false);

        if (removePattern)
            rows.removeAllElements(); // Quita la primera fila (pattern)
        else
            createAndSyncColumnArrayList();   // Está el pattern (no eliminado)
    }

    public boolean isUsePatternMarkupToRender()
    {
        return usePatternMarkupToRender;
    }

    public void setUsePatternMarkupToRender(boolean usePatternMarkupToRender)
    {
        this.usePatternMarkupToRender = usePatternMarkupToRender;
    }

    public ElementListFreeImpl getRowsAsElementListFree()
    {
        return getRowElementList().getInternalElementListFree();
    }

    public ElementListImpl getRowElementList()
    {
        return (ElementListImpl)rows;
    }

    public ElementTableRenderer getElementTableRenderer()
    {
        return renderer;
    }

    public void setElementTableRenderer(ElementTableRenderer renderer)
    {
        this.renderer = renderer;
    }

    public ElementTableStructure getElementTableStructure()
    {
        return structure;
    }

    public void setElementTableStructure(ElementTableStructure structure)
    {
        this.structure = structure;
    }

    public Element getRowPatternElement()
    {
        return getRowElementList().getChildPatternElement();
    }

    public Element getCellPatternElement()
    {
        return columnListPattern.getChildPatternElement();
    }

    public DocumentFragment getCellContentPatternFragment()
    {
        if (cellContentPatternFragment == null)
        {
            // El cellContentPatternFragment necesario para el renderTable con usePatternMarkupToRender = true
            Element cellPatternContentElem = structure.getCellContentElement(this,0,0,getCellPatternElement());
            if (cellPatternContentElem != null) // Si no se puede no se puede
            {
                Element clonedItemContentElem = (Element)cellPatternContentElem.cloneNode(true); // Necesitamos clonar porque al extraer los nodos hijos se vaciará el contenido
                this.cellContentPatternFragment = DOMUtilInternal.extractChildrenToDocFragment(clonedItemContentElem);
            }
        }

        return cellContentPatternFragment;
    }

    protected void setRowContent(int row,Element rowElem,Object[] rowData,boolean isNew)
    {
        if ((rowData != null)&&(rowData.length > 0))
        {
            ElementListImpl columns = (ElementListImpl)getColumnsOfRowElementList(row,rowElem);
            // Si la longitud de rowData es menor que el número de columnas dará error
            int col = 0;
            for(Iterator it = columns.getInternalElementListFree().iterator(); it.hasNext(); )
            {
                Element columElem = (Element)it.next();
                Object value = rowData[col];
                setCellValueAt(row,col,value,columElem,isNew);

                col++;
            }
        }
    }


    public Element addRow()
    {
        return addRow((Object[])null);
    }

    public Element addRow(List rowData)
    {
        return addRow(toObjectArray(rowData));
    }

    public Element addRow(Object[] rowData)
    {
        int row = rows.getLength();
        Element rowElem = getRowElementList().addElement();

        insertColumnListOfRow(row,rowElem);

        setRowContent(row,rowElem,rowData,true);
        return rowElem;
    }

    public Element insertRowAt(int row)
    {
        return insertRowAt(row,(Object[])null);
    }

    public Element insertRowAt(int row,List rowData)
    {
        return insertRowAt(row,toObjectArray(rowData));
    }

    public Element insertRowAt(int row,Object[] rowData)
    {
        Element rowElem = getRowElementList().insertElementAt(row);
        if (rowElem == null) return null; // Fuera de rango

        insertColumnListOfRow(row,rowElem);

        setRowContent(row,rowElem,rowData,true);
        return rowElem;
    }

    public void setRowValuesAt(int row,List rowData)
    {
        setRowValuesAt(row,toObjectArray(rowData));
    }

    public void setRowValuesAt(int row,Object[] rowData)
    {
        Element rowElem = getRowElementAt(row);
        setRowContent(row,rowElem,rowData,false);
    }

    public ElementListBaseImpl getColumnsOfRowElementList(int row,Element rowElem)
    {
        return (ElementListImpl)columnListOfRow.get(row);
    }

    public ElementListBaseImpl newColumnsOfRowElementList(int row,Element rowElem)
    {
        Element rowContentElem = getRowContentElementAt(row,rowElem);
        Element cellPattern = getCellPatternElement();
        DocumentFragment cellContentPattern = getCellContentPatternFragment();
        ElementGroupManagerImpl factory = getItsNatDocumentImpl().getElementGroupManagerImpl();
        return factory.createElementListInternal(true,rowContentElem,cellPattern,false,cellContentPattern,false,null,null); // No clonamos pues el mismo elemento patrón celda ya fue clonado y es utilizado para crear todas las columnas
    }

    public Element[] addOrInsertColumn(int column,Object[] columnData,boolean add)
    {
        if (add)
            columnListPattern.addElement(); // Añadimos también al patrón para que al añadir una nueva fila tenga el nuevo número de columnas
        else
            columnListPattern.insertElementAt(column); // Añadimos también al patrón para que al añadir una nueva fila tenga el nuevo número de columnas

        ElementListFreeImpl rows = getRowsAsElementListFree();
        if (rows.isEmpty()) return new Element[0]; // Nada más que hacer

        Element[] newCells = new Element[rows.getLength()];

        // Si la longitud de columnData es menor que el número de columnas dará error
        int row = 0;
        for(Iterator it = rows.iterator(); it.hasNext(); )
        {
            Element rowElem = (Element)it.next();

            ElementListImpl columns = (ElementListImpl)getColumnsOfRowElementList(row,rowElem);
            Element columnElem = null;
            if (add)
                columnElem = columns.addElement();
            else
                columnElem = columns.insertElementAt(column);

            newCells[row] = columnElem;
            if (columnData != null)
                setCellValueAt(row,column,columnData[row],columnElem,true);

            row++;
        }

        return newCells;
    }

    public Element[] addColumn()
    {
        return addColumn((Object[])null);
    }

    public Element[] addColumn(List columnData)
    {
        return addColumn(toObjectArray(columnData));
    }

    public Element[] addColumn(Object[] columnData)
    {
        int column = getColumnCount();
        return addOrInsertColumn(column,columnData,true);
    }

    public Element[] insertColumnAt(int column)
    {
        return insertColumnAt(column,(Object[])null);
    }

    public Element[] insertColumnAt(int column,List columnData)
    {
        return insertColumnAt(column,toObjectArray(columnData));
    }

    public Element[] insertColumnAt(int column,Object[] columnData)
    {
        return addOrInsertColumn(column,columnData,false);
    }

    public void setColumnValuesAt(int column,List columnData)
    {
        setColumnValuesAt(column,toObjectArray(columnData));
    }

    public void setColumnValuesAt(int column,Object[] columnData)
    {
        // Nada que hacer al pattern

        ElementListFreeImpl rows = getRowsAsElementListFree();
        if (rows.isEmpty()) return; // Nada que hacer

        // Si la longitud de columnData es menor que el número de columnas dará error
        int row = 0;
        for(Iterator it = rows.iterator(); it.hasNext(); )
        {
            Element rowElem = (Element)it.next();

            ElementListImpl columns = (ElementListImpl)getColumnsOfRowElementList(row,rowElem);
            Element columnElem = columns.getElementAt(column);
            setCellValueAt(row,column,columnData[row],columnElem,false);

            row++;
        }
    }

    public void removeColumnAt(int column)
    {
        ElementTableRenderer renderer = getElementTableRenderer();
        if (renderer != null)
        {
            int rowCount = getRowCount();
            for(int row = 0; row < rowCount; row++)
                unrenderCell(row,column);
        }

        columnListPattern.removeElementAt(column); // Quitamos también al patrón para que al añadir una nueva fila tenga el nuevo número de columnas

        super.removeColumnAt(column);
    }

    public void removeAllColumns()
    {
        unrenderAllCells();

        columnListPattern.removeAllElements(); // Quitamos también al patrón para que al añadir una nueva fila tenga el nuevo número de columnas

        super.removeAllColumns();
    }

    public int getColumnCount()
    {
        return columnListPattern.getLength();
    }

    public Element getCellContentElementAt(int row, int column)
    {
        Element cellElem = getCellElementAt(row,column);
        if (cellElem == null) return null;
        return getCellContentElementAt(row,column,cellElem);
    }

    public Element getCellContentElementAt(int row, int column,Element cellElem)
    {
        return getElementTableStructure().getCellContentElement(this,row,column,cellElem);
    }

    public Element getRowContentElementAt(int row)
    {
        Element rowElem = getRowElementAt(row);
        if (rowElem == null) return null;
        return getRowContentElementAt(row,rowElem);
    }

    public Element getRowContentElementAt(int row,Element rowElem)
    {
        return getElementTableStructure().getRowContentElement(this,row,rowElem);
    }

    public void setCellValueAt(int row, int column,Object value)
    {
        Element cellElem = getCellElementAt(row,column);
        if (cellElem == null) throw new ItsNatException("Out of range",this);

        setCellValueAt(row,column,value,cellElem,false);
    }

    public void setCellValueAt(int row, int column,Object value,Element cellElem,boolean isNew)
    {
        Element cellContentElem = getCellContentElementAt(row,column,cellElem);
        prepareRendering(cellContentElem,isNew);
        ElementTableRenderer renderer = getElementTableRenderer();
        if (renderer != null)
            renderer.renderTable(this,row,column,value,cellContentElem,isNew);
    }

    public void prepareRendering(Element cellContentElem,boolean isNew)
    {
        if (!isNew && isUsePatternMarkupToRender())  // Si es nuevo el markup es ya el del patrón
        {
            // Es una actualización en donde tenemos que usar el markup pattern en vez del contenido actual
            restorePatternMarkupWhenRendering(cellContentElem,getCellContentPatternFragment());
        }
    }

    public Element removeRowAt(int row)
    {
        ElementTableRenderer renderer = getElementTableRenderer();
        if (renderer != null)
        {
            int columnCount = getColumnCount();
            for(int col = 0; col < columnCount; col++)
                unrenderCell(row,col);
        }

        return super.removeRowAt(row);
    }

    public void removeRowRange(int fromIndex, int toIndex)
    {
        ElementTableRenderer renderer = getElementTableRenderer();
        if (renderer != null)
        {
            int columnCount = getColumnCount();
            for(int row = fromIndex; row <= toIndex; row++)
                for(int col = 0; col < columnCount; col++)
                    unrenderCell(row,col);
        }

        super.removeRowRange(fromIndex,toIndex);
    }

    public void removeAllRows()
    {
        unrenderAllCells();

        super.removeAllRows();
    }

    public void unrenderCell(int row,int column)
    {
        ElementTableRenderer renderer = getElementTableRenderer();
        if (renderer == null) return;

        Element cellElem = getCellElementAt(row,column);
        if (cellElem == null) return;

        Element cellContentElem = getCellContentElementAt(row,column,cellElem);
        renderer.unrenderTable(this,row,column,cellContentElem);
    }

    public void unrenderAllCells()
    {
        ElementTableRenderer renderer = getElementTableRenderer();
        if (renderer == null) return;

        int rowCount = getRowCount();
        int columnCount = getColumnCount();
        for(int row = 0; row < rowCount; row++)
            for(int col = 0; col < columnCount; col++)
                unrenderCell(row,col);
    }

    public void setRowCount(int rowCount)
    {
        if (rowCount < 0) throw new ItsNatException("Negative row count: " + rowCount,this);
        int currentRowCount = getRowCount();
        int diff = rowCount - currentRowCount;
        if (diff > 0)
            for(int i = 0; i < diff; i++)
                addRow();
        else if (diff < 0)
            for(int i = currentRowCount - 1; i >= rowCount; i--)
                removeRowAt(i);
    }

    public void setColumnCount(int columnCount)
    {
        if (columnCount < 0) throw new ItsNatException("Negative column count: " + columnCount,this);
        int currentColumnCount = getColumnCount();
        int diff = columnCount - currentColumnCount;
        if (diff > 0)
            for(int i = 0; i < diff; i++)
                addColumn();
        else if (diff < 0)
            for(int i = currentColumnCount - 1; i >= columnCount; i--)
                removeColumnAt(i);
    }

    public void setTableValues(List values)
    {
        for(int i = 0; i < values.size(); i++)
        {
            List rowValues = (List)values.get(i);
            for(int j = 0; j < rowValues.size(); i++)
            {
                Object cellValue = rowValues.get(i);
                setCellValueAt(i,j,cellValue);
            }
        }
    }

    public void setTableValues(Object[][] values)
    {
        for(int i = 0; i < values.length; i++)
        {
            Object[] rowValues = values[i];
            for(int j = 0; j < rowValues.length; j++)
            {
                Object cellValue = rowValues[i];
                setCellValueAt(i,j,cellValue);
            }
        }
    }

    public void moveColumn(int columnIndex, int newIndex)
    {
        if (columnIndex == newIndex) return;

        columnListPattern.moveElement(columnIndex,columnIndex,newIndex);

        ElementListFreeImpl rows = getRowsAsElementListFree();
        if (rows.isEmpty()) return; // Nada que hacer

        int row = 0;
        for(Iterator it = rows.iterator(); it.hasNext(); )
        {
            Element rowElem = (Element)it.next();

            ElementListImpl columns = (ElementListImpl)getColumnsOfRowElementList(row,rowElem);
            columns.moveElement(columnIndex,columnIndex,newIndex);

            row++;
        }
    }

    public TableCellElementInfoImpl getTableCellElementInfo(ListElementInfo rowInfo, ListElementInfo cellInfo)
    {
        return TableCellElementInfoMasterImpl.getTableCellElementInfoMaster((ListElementInfoMasterImpl)rowInfo,(ListElementInfoMasterImpl)cellInfo,this);
    }

    public static Object[] toObjectArray(List data)
    {
        if (data == null) return null;

        Object[] dataArray = new Object[data.size()];
        dataArray = data.toArray(dataArray);
        return dataArray;
    }
}
