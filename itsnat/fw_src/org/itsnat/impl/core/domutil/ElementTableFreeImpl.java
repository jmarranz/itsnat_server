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
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.itsnat.core.domutil.ElementListFree;
import org.itsnat.core.domutil.ElementTableFree;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class ElementTableFreeImpl extends ElementTableBaseImpl implements ElementTableFree,ElementListFreeInternal
{

    /**
     * Creates a new instance of ElementTableBaseImpl
     */
    public ElementTableFreeImpl(ItsNatDocumentImpl itsNatDoc,boolean master,Element parentElement)
    {
        super(itsNatDoc,parentElement);

        this.rows = (ElementListFreeImpl)itsNatDoc.getElementGroupManagerImpl().createElementListFree(parentElement,master);
    }

    public ElementListFreeImpl getRowsAsElementListFree()
    {
        return (ElementListFreeImpl)rows;
    }

    public ElementListFreeImpl getRows()
    {
        return (ElementListFreeImpl)rows;
    }

    public void addRow(Element rowElem)
    {
        addRow2(rowElem);
    }

    public Element addRow2(Element rowElem)
    {
        // Se deriva
        rowElem = getRowsAsElementListFree().addElement2(rowElem);

        return rowElem;
    }

    public void insertRowAt(int row,Element rowElem)
    {
        insertRowAt2(row,rowElem);
    }

    public Element insertRowAt2(int row,Element rowElem)
    {
        // Se deriva
        rowElem = getRowsAsElementListFree().insertElementAt2(row,rowElem);

        return rowElem;
    }

    public Element setRowAt(int row,Element rowElem)
    {
        return setRowAt2(row,rowElem).getOldElem();
    }

    public ElementPair setRowAt2(int row,Element rowElem)
    {
        // Se deriva
        ElementPair res = getRowsAsElementListFree().setElementAt2(row,rowElem);

        return res;
    }

    public Element[] setCellElementsOfRow(int row,Element[] cells)
    {
        Element rowElem = getRowElementAt(row); // Si devuelve null es que está fuera del rango
        if (rowElem == null) throw new ItsNatException("Row is out of range: " + row,this);
        ElementListFreeImpl columns = (ElementListFreeImpl)getColumnsOfRowElementList(row,rowElem);
        return columns.setElements(cells);
    }

    public ElementListBaseImpl newColumnsOfRowElementList(int row,Element rowElem)
    {
        // row no se necesita
        return (ElementListFreeImpl)getItsNatDocumentImpl().getElementGroupManagerImpl().createElementListFree(rowElem,isMaster());
    }

    public void addColumn(Element[] cells)
    {
        ElementListFreeImpl rows = getRowsAsElementListFree();
        if (rows.isEmpty()) return;  // Nada que hacer

        // Si la longitud de cells es menor que el número de filas dará error
        int row = 0;
        for(Iterator it = rows.iterator(); it.hasNext(); )
        {
            Element rowElem = (Element)it.next();

            ElementListFreeImpl columns = (ElementListFreeImpl)getColumnsOfRowElementList(row,rowElem);
            cells[row] = columns.addElement2(cells[row]);

            row++;
        }
    }

    public void insertColumnAt(int column,Element[] cells)
    {
        ElementListFreeImpl rows = getRowsAsElementListFree();
        if (rows.isEmpty()) return;  // Nada que hacer

        // Si la longitud de cells es menor que el número de filas dará error
        int row = 0;
        for(Iterator it = rows.iterator(); it.hasNext(); )
        {
            Element rowElem = (Element)it.next();

            ElementListFreeImpl columns = (ElementListFreeImpl)getColumnsOfRowElementList(row,rowElem);
            cells[row] = columns.insertElementAt2(column,cells[row]);

            row++;
        }
    }

    public Element[] setCellElementsOfColumn(int column,Element[] cells)
    {
        ElementListFreeImpl rows = getRowsAsElementListFree();
        if (rows.isEmpty()) return new Element[0];  // Nada que hacer

        Element[] replaced = new Element[cells.length];

        // Si la longitud de cells es menor que el número de filas dará error
        int row = 0;
        for(Iterator it = rows.iterator(); it.hasNext(); )
        {
            Element rowElem = (Element)it.next();

            ElementListFreeImpl columns = (ElementListFreeImpl)getColumnsOfRowElementList(row,rowElem);
            ElementPair res = columns.setElementAt2(column,cells[row]);
            cells[row] = res.getNewElem();
            replaced[row] = res.getOldElem();

            row++;
        }
        return replaced;
    }

    public Element setCellElementAt(int row, int column,Element elem)
    {
        Element rowElem = getRowElementAt(row); // Si devuelve null es que está fuera del rango
        if (rowElem == null) throw new ItsNatException("Row is out of range: " + row,this);
        ElementListFreeImpl columns = (ElementListFreeImpl)getColumnsOfRowElementList(row,rowElem);
        ElementPair res = columns.setElementAt2(column,elem);
        return res.getOldElem();
    }

    public ElementListFree getCellElementListOfRow(int row)
    {
        Element rowElem = getRowElementAt(row); // Si devuelve null es que está fuera del rango
        if (rowElem == null) throw new ItsNatException("Row is out of range: " + row,this);
        ElementListFreeImpl columns = (ElementListFreeImpl)getColumnsOfRowElementList(row,rowElem);
        return columns;
    }

    public void moveColumn(int columnIndex, int newIndex)
    {
        if (columnIndex == newIndex) return;

        Element[] elements = getCellElementsOfColumn(columnIndex);
        removeColumnAt(columnIndex);
        insertColumnAt(newIndex,elements);
    }

    // java.util.List

    public int indexOf(Object o)
    {
        return ElementListFreeUtil.indexOf(this,o);
    }

    public boolean contains(Object o)
    {
        return ElementListFreeUtil.contains(this,o);
    }

    public boolean add(Object o)
    {
        return ElementListFreeUtil.add(this,o);
    }

    public int lastIndexOf(Object o)
    {
        return ElementListFreeUtil.lastIndexOf(this,o);
    }

    public boolean remove(Object o)
    {
        return ElementListFreeUtil.remove(this,o);
    }

    public Object[] toArray(Object[] a)
    {
        return ElementListFreeUtil.toArray(this,a);
    }

    public Object get(int index)
    {
        return ElementListFreeUtil.get(this,index);
    }

    public Iterator iterator()
    {
        return ElementListFreeUtil.iterator(this);
    }

    public ListIterator listIterator(int index)
    {
        return ElementListFreeUtil.listIterator(this,index);
    }

    public ListIterator listIterator()
    {
        return ElementListFreeUtil.listIterator(this);
    }

    public Object remove(int index)
    {
        return ElementListFreeUtil.remove(this,index);
    }

    public boolean addAll(int index, Collection c)
    {
        return ElementListFreeUtil.addAll(this,index,c);
    }

    public boolean containsAll(Collection c)
    {
        return ElementListFreeUtil.containsAll(this,c);
    }

    public boolean addAll(Collection c)
    {
        return ElementListFreeUtil.addAll(this,c);
    }

    public boolean removeAll(Collection c)
    {
        return ElementListFreeUtil.removeAll(this,c);
    }

    public boolean retainAll(Collection c)
    {
        return ElementListFreeUtil.retainAll(this,c);
    }

    public void add(int index, Object element)
    {
        ElementListFreeUtil.add(this,index,element);
    }

    public Object set(int index, Object element)
    {
        return ElementListFreeUtil.set(this,index,element);
    }

    public Object[] toArray()
    {
        return ElementListFreeUtil.toArray(this);
    }

    public List subList(int fromIndex, int toIndex)
    {
        return ElementListFreeUtil.subList(this,fromIndex,toIndex);
    }

    public int size()
    {
        return ElementListFreeUtil.size(this);
    }

    public void clear()
    {
        ElementListFreeUtil.clear(this);
    }

    // ElementListFreeInternal

    public void removeElement(int index,Element node)
    {
        removeRowAt(index);
    }

    public Element insertBeforeElement(int index,Element newNode,Element refNode)
    {
        return insertElementAt2(index,newNode);
    }

    public Element getPreviousSiblingElement(int index,Element ref)
    {
        return getRowsAsElementListFree().getPreviousSiblingElement(index,ref);
    }

    public Element getNextSiblingElement(int index,Element ref)
    {
        return getRowsAsElementListFree().getNextSiblingElement(index,ref);
    }

    public void addElement(Element elem)
    {
        addRow(elem);
    }

    public Element addElement2(Element elem)
    {
        return addRow2(elem);
    }

    public Element setElementAt(int index, Element elem)
    {
        return setRowAt(index,elem);
    }

    public ElementPair setElementAt2(int index, Element elem)
    {
        return setRowAt2(index,elem);
    }

    public void insertElementAt(int index, Element elem)
    {
        insertRowAt(index,elem);
    }

    public Element insertElementAt2(int index, Element elem)
    {
        return insertRowAt2(index,elem);
    }

    public boolean isMaster()
    {
        return getRowsAsElementListFree().isMaster();
    }

    public Element[] setElements(Element[] newElems)
    {
        return getRowsAsElementListFree().setElements(newElems);
    }

}
