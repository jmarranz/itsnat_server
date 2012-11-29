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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.domutil.ElementTableBase;
import org.itsnat.core.domutil.ElementTableRenderer;
import org.itsnat.core.domutil.ListElementInfo;
import org.itsnat.core.domutil.TableCellElementInfo;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableSectionElement;

/**
 *
 * @author jmarranz
 */
public abstract class ElementTableBaseImpl extends ElementGroupImpl implements ElementTableBase
{
    protected ArrayList columnListOfRow; // Acelera el proceso de las columnas de la fila, sólo se usa en modo master pues en modo slave (master=false) pueden añadirse/quitarse filas via DOM y quedaría desincronizada
    protected ElementListBaseImpl rows;
    protected boolean isHTMLTable;

    /** Creates a new instance of ElementTableBaseImpl */
    public ElementTableBaseImpl(ItsNatDocumentImpl itsNatDoc,Element parentElement)
    {
        super(itsNatDoc);

        this.isHTMLTable = (parentElement instanceof HTMLTableSectionElement);
    }

    public ElementListBaseImpl getRowsAsElementListBase()
    {
        return rows;
    }

    public ArrayList getColumnListOfRowArrayList()
    {
        return columnListOfRow;
    }

    public abstract ElementListBaseImpl getColumnsOfRowElementList(int row,Element rowElem);

    public abstract ElementListBaseImpl newColumnsOfRowElementList(int row,Element rowElem);

    public abstract ElementListFreeImpl getRowsAsElementListFree();


    public void createAndSyncColumnArrayList()
    {
        // Sincroniza con el estado actual de la lista, **sólo es llamada en modo master**
        int len = rows.getLength();
        for(int i = 0; i < len; i++)
        {
            Element rowElem = rows.getElementAt(i);
            ElementListBaseImpl columsOfRow = newColumnsOfRowElementList(i,rowElem);
            columnListOfRow.add(columsOfRow);
        }
    }

    public void addColumnListOfRow(Element rowElem)
    {
        if (columnListOfRow != null)
        {
            int row = columnListOfRow.size();
            ElementListBaseImpl columsOfRow = newColumnsOfRowElementList(row,rowElem);
            columnListOfRow.add(columsOfRow);
        }
    }

    public void insertColumnListOfRow(int row,Element rowElem)
    {
        if (columnListOfRow != null)
        {
            ElementListBaseImpl columsOfRow = newColumnsOfRowElementList(row,rowElem);
            columnListOfRow.add(row,columsOfRow);
        }
    }

    public void removeColumnListOfRow(int row)
    {
        if (columnListOfRow != null)
            columnListOfRow.remove(row);
    }

    public void removeColumnListOfRowRange(int fromIndex,int toIndex)
    {
        if (columnListOfRow != null)
        {
            int count = toIndex - fromIndex + 1;
            for(int i = 1; i <= count; i++)
                columnListOfRow.remove(fromIndex);
        }
    }

    public void removeAllColumnListOfRow()
    {
        if (columnListOfRow != null)
            columnListOfRow.clear();
    }

    public Element removeRowAt(int row)
    {
        Element rowElem = rows.removeElementAt(row);
        if (rowElem == null) return null; // Fuera de rango

        removeColumnListOfRow(row);

        return rowElem;
    }

    public void removeRowRange(int fromIndex, int toIndex)
    {
        rows.removeElementRange(fromIndex,toIndex);

        removeColumnListOfRowRange(fromIndex,toIndex);
    }

    public void removeAllRows()
    {
        rows.removeAllElements();

        removeAllColumnListOfRow();
    }

    public void moveRow(int start,int end,int to)
    {
        ElementListFreeImpl rows = getRowsAsElementListFree();
        if (rows.isEmpty()) throw new ItsNatException("Table is empty",this);

        Element rowCurrentElem = getRowElementAt(start);
        int count = end - start + 1;
        List rowsMoved = new ArrayList(count);

        for(int i = start; i <= end; i++)
        {
            // Usamos start y no i porque al borrar se mueven los siguientes
            rowsMoved.add(rowCurrentElem);
            Element elemTmp = rowCurrentElem;
            rowCurrentElem = rows.getNextSiblingElement(start,rowCurrentElem);
            rows.removeElement(start,elemTmp);

            removeColumnListOfRow(start);
        }
        Element refElem = getRowElementAt(to); // puede ser null (añadir al final, hay que tener en cuenta que se han borrado posibles anteriores)
        for(int i = 0; i < count; i++)
        {
            rowCurrentElem = (Element)rowsMoved.get(i);
            int row = to + i;
            rowCurrentElem = rows.insertBeforeElement(row,rowCurrentElem,refElem);

            insertColumnListOfRow(row,rowCurrentElem);
        }
    }

    public void removeColumnAt(int column)
    {
        ElementListFreeImpl rows = getRowsAsElementListFree();
        if (rows.isEmpty()) return; // Nada que hacer

        int row = 0;
        for(Iterator it = rows.iterator(); it.hasNext(); )
        {
            Element rowElem = (Element)it.next();

            ElementListBaseImpl columns = getColumnsOfRowElementList(row,rowElem);
            columns.removeElementAt(column);

            row++;
        }
    }


    public void removeAllColumns()
    {
        ElementListFreeImpl rows = getRowsAsElementListFree();
        if (rows.isEmpty()) return; // Nada que hacer

        int row = 0;
        for(Iterator it = rows.iterator(); it.hasNext(); )
        {
            Element rowElem = (Element)it.next();

            ElementListBaseImpl columns = getColumnsOfRowElementList(row,rowElem);
            columns.removeAllElements();

            row++;
        }
    }

    public int getRowCount()
    {
        return rows.getLength();
    }

    public Element getRowElementAt(int row)
    {
        return rows.getElementAt(row);
    }

    public Element getCellElementAt(int row, int column)
    {
        Element rowElem = getRowElementAt(row);
        if (rowElem == null) return null; // fuera de rango
        ElementListBaseImpl columns = getColumnsOfRowElementList(row,rowElem);
        return columns.getElementAt(column);
    }

    public Element[] getElementRows()
    {
        return rows.getElements();
    }

    public Element[] getCellElementsOfRow(int row)
    {
        Element rowElem = getRowElementAt(row); // Si devuelve null es que está fuera del rango
        if (rowElem == null) return null; // fuera de rango
        ElementListBaseImpl columns = getColumnsOfRowElementList(row,rowElem);
        return columns.getElements();
    }

    public Element[] getCellElementsOfColumn(int column)
    {
        ElementListFreeImpl rows = getRowsAsElementListFree();

        int rowCount = getRowCount();
        Element[] columnElems = createCellElementArray(rowCount);
        if (rowCount == 0) return columnElems; // Array vacío

        int row = 0;
        for(Iterator it = rows.iterator(); it.hasNext(); )
        {
            Element rowElem = (Element)it.next();

            ElementListBaseImpl columns = getColumnsOfRowElementList(row,rowElem);
            Element colElem = columns.getElementAt(column);
            columnElems[row] = colElem; // Puede ser null, caso de que esta fila no tenga ese número de columnas (ElementTableFree)

            row++;
        }

        return columnElems;
    }

    public Element getRowElementFromNode(Node node)
    {
        return rows.getElementFromNode(node);
    }

    public ListElementInfo getRowListElementInfoFromNode(Node node)
    {
        return rows.getListElementInfoFromNode(node);
    }

    public ListElementInfo getRowListElementInfoAt(int index)
    {
        return rows.getListElementInfoAt(index);
    }

    public abstract TableCellElementInfoImpl getTableCellElementInfo(ListElementInfo rowInfo,ListElementInfo cellInfo);

    public TableCellElementInfo getTableCellElementInfoFromNode(Node node)
    {
        // node es un nodo que forma parte de una celda de una fila
        // obtenemos el nodo padre de la celda
        if (node == null) return null;

        ElementListFreeImpl rows = getRowsAsElementListFree();
        if (rows.isEmpty()) return null;

        ListElementInfo rowInfo = getRowListElementInfoFromNode(node);
        if (rowInfo == null) return null; // No encontrado

        // Ahora en qué celda concreta
        int row = rowInfo.getIndex();
        Element rowElem = rowInfo.getElement();
        ElementListBaseImpl columns = getColumnsOfRowElementList(row,rowElem);

        ListElementInfo cellInfo = columns.getListElementInfoFromNode(node);
        if (cellInfo == null) return null; // No debería ocurrir pero por si acaso
        return getTableCellElementInfo(rowInfo,cellInfo);
    }

    public TableCellElementInfo getTableCellElementInfoAt(int row,int column)
    {
        ListElementInfo rowInfo = getRowListElementInfoAt(row);
        if (rowInfo == null) return null; // fuera de rango
        Element rowElem = rowInfo.getElement();
        ElementListBaseImpl columns = getColumnsOfRowElementList(row,rowElem);
        ListElementInfo cellInfo = columns.getListElementInfoAt(column);
        if (cellInfo == null) return null;
        return getTableCellElementInfo(rowInfo,cellInfo);
    }

    public Element getFirstRowElement()
    {
        return rows.getFirstElement();
    }

    public Element getLastRowElement()
    {
        return rows.getLastElement();
    }

    public int indexOfRowElement(Element elem)
    {
        return rows.indexOfElement(elem);
    }

    public int lastIndexOfRowElement(Element elem)
    {
        return rows.lastIndexOfElement(elem);
    }

    // NodeList : visto como una lista de filas

    public Node item(int index)
    {
        return getElementAt(index);
    }

    public int getLength()
    {
        return getRowCount();
    }

    // ElementListBase : visto como una lista de filas

    public boolean isEmpty()
    {
        return rows.isEmpty();
    }

    public Element getElementFromNode(Node node)
    {
        return getRowElementFromNode(node);
    }

    public ListElementInfo getListElementInfoFromNode(Node node)
    {
        return getRowListElementInfoFromNode(node);
    }

    public ListElementInfo getListElementInfoAt(int index)
    {
        return getRowListElementInfoAt(index);
    }

    public Element getElementAt(int index)
    {
        return getRowElementAt(index);
    }

    public Element removeElementAt(int index)
    {
        return removeRowAt(index);
    }

    public int indexOfElement(Element elem)
    {
        return indexOfRowElement(elem);
    }

    public int lastIndexOfElement(Element elem)
    {
        return lastIndexOfRowElement(elem);
    }

    public Element[] getElements()
    {
        return getElementRows();
    }

    public Element getFirstElement()
    {
        return getFirstRowElement();
    }

    public Element getLastElement()
    {
        return getLastRowElement();
    }

    public Element getParentElement()
    {
        return rows.getParentElement();
    }

    public void removeAllElements()
    {
        removeAllRows();
    }

    public void removeElementRange(int fromIndex, int toIndex)
    {
        removeRowRange(fromIndex,toIndex);
    }

    public void moveElement(int start,int end,int to)
    {
        moveRow(start,end,to);
    }

    protected Element[] createCellElementArray(int size)
    {
        if (isHTMLTable)
            return new HTMLTableCellElement[size];
        else
            return new Element[size];
    }

}
