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

import org.itsnat.impl.comp.list.ItsNatListBasedCellUIImpl;
import org.itsnat.impl.comp.list.ItsNatListBasedUIImpl;
import org.itsnat.comp.table.ItsNatTable;
import org.itsnat.comp.table.ItsNatTableHeader;
import org.itsnat.comp.table.ItsNatTableHeaderCellRenderer;
import org.itsnat.comp.table.ItsNatTableStructure;
import org.itsnat.comp.table.ItsNatTableHeaderCellUI;
import org.itsnat.comp.table.ItsNatTableHeaderUI;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.domutil.ListElementInfoMasterImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatTableHeaderUIImpl extends ItsNatListBasedUIImpl implements ItsNatTableHeaderUI
{
    protected boolean enabled = true;

    /**
     * Creates a new instance of ItsNatTableHeaderUIImpl
     */
    public ItsNatTableHeaderUIImpl(ItsNatTableHeaderImpl parentComp,Element rowElement)
    {
        super(parentComp);

        ItsNatTableImpl tableComp = (ItsNatTableImpl)parentComp.getItsNatTable();
        ItsNatTableStructure structure = tableComp.getItsNatTableStructure();
        ItsNatTableStructureCoreAdapterImpl structAdapter;
        structAdapter = new ItsNatTableStructureCoreAdapterImpl(structure,null,parentComp);

        ItsNatDocumentImpl itsNatDoc = getItsNatDocumentImpl();
        this.elementList = itsNatDoc.getElementGroupManagerImpl().createElementListInternal(rowElement,true,structAdapter,null);
    }

    public ItsNatTableHeader getItsNatTableHeader()
    {
        return (ItsNatTableHeader)parentComp;
    }

    public ItsNatTable getItsNatTable()
    {
        return getItsNatTableHeader().getItsNatTable();
    }

    public ItsNatTableStructure getItsNatTableStructure()
    {
        return getItsNatTable().getItsNatTableStructure();
    }

    public ItsNatTableHeaderCellRenderer getItsNatTableHeaderCellRenderer()
    {
        return getItsNatTableHeader().getItsNatTableHeaderCellRenderer();
    }

    public void setElementValueAt(int column,Object columnValue,boolean isSelected,boolean cellHasFocus,Element element,boolean isNew)
    {
        ItsNatTableHeader tableHeader = getItsNatTableHeader();
        Element contentElem = getContentElementAt(column,element);

        elementList.prepareRendering(contentElem,isNew);

        ItsNatTableHeaderCellRenderer renderer = getItsNatTableHeaderCellRenderer();
        if (renderer != null)
            renderer.renderTableHeaderCell(tableHeader,column,columnValue,isSelected,cellHasFocus,contentElem,isNew);
    }

    public Element getContentElementAt(int index,Element elem)
    {
        return getItsNatTableStructure().getHeaderColumnContentElement(getItsNatTableHeader(),index,elem);
    }

    public ItsNatListBasedCellUIImpl createItsNatListCellUI(ListElementInfoMasterImpl elementInfo)
    {
        return new ItsNatTableHeaderCellUIImpl(elementInfo,this);
    }

    public ItsNatTableHeaderCellUI getItsNatTableHeaderCellUIAt(int index)
    {
        return (ItsNatTableHeaderCellUI)getItsNatListBasedCellUIAt(index);
    }

    public ItsNatTableHeaderCellUI getItsNatTableHeaderCellUIFromNode(Node node)
    {
        return (ItsNatTableHeaderCellUI)getItsNatListBasedCellUIFromNode(node);
    }

    public Element getContentElementAt(int index)
    {
        return super.getContentElementAtBase(index);
    }

    /**
     * ESTE METODO FUE PUBLICO ANTES
     *
     * Renders the specified value into the header column element with the given position.
     *
     * @param index index of the element.
     * @param value the value to render.
     * @param isSelected if this header column is selected.
     * @param hasFocus if this header has the focus. Current ItsNat implementation ever passes false.
     * @see org.itsnat.core.domutil.ElementList#setElementValueAt(int,Object)
     */
    public void setElementValueAt(int index, Object anObject, boolean isSelected, boolean cellHasFocus)
    {
        super.setElementValueAtBase(index,anObject,isSelected,cellHasFocus);
    }

    public boolean isEmpty()
    {
        return super.isEmptyBase();
    }

    public int getLength()
    {
        return super.getLengthBase();
    }

    /**
     * ESTE METODO FUE PUBLICO ANTES
     *
     * Sets the number of header columns.
     *
     * @param len the new number of header columns.
     * @see org.itsnat.core.domutil.ElementList#setLength(int)
     */
    public void setLength(int len)
    {
        super.setLengthBase(len);
    }

    public Element getElementAt(int index)
    {
        return super.getElementAtBase(index);
    }

    /**
     * ESTE METODO FUE PUBLICO ANTES
     *
     * Inserts a new header column element at the specified position rendering
     * the specified column value.
     *
     * @param index index of the new header column.
     * @param value the column value to render as markup.
     * @return the new element.
     * @see org.itsnat.core.domutil.ElementList#insertElementAt(int,Object)
     */
    public Element insertElementAt(int index,Object anObject)
    {
        return super.insertElementAtBase(index,anObject);
    }

    public void unrenderTableHeaderCell(int column)
    {
        ItsNatTableHeaderCellRenderer renderer = getItsNatTableHeaderCellRenderer();
        if (renderer == null) return;

        Element cellElem = getElementAt(column);
        if (cellElem == null) return;

        Element contentElem = getContentElementAt(column,cellElem);
        renderer.unrenderTableHeaderCell(getItsNatTableHeader(),column,contentElem);
    }

    /**
     * ESTE METODO FUE PUBLICO ANTES
     *
     * Removes the specified header column.
     *
     * @param index index of the header column to remove.
     * @return the removed element or null if index is out of bounds.
     * @see org.itsnat.core.domutil.ElementListBase#removeElementAt(int)
     */
    public Element removeElementAt(int index)
    {
        return super.removeElementAtBase(index);
    }

    /**
     * ESTE METODO FUE PUBLICO ANTES
     *
     * Removes the header column elements between the specified indexes.
     *
     * @param fromIndex low index (inclusive).
     * @param toIndex high index (inclusive).
     * @see org.itsnat.core.domutil.ElementListBase#removeElementRange(int,int)
     */
    public void removeElementRange(int fromIndex,int toIndex)
    {
        ItsNatTableHeaderCellRenderer renderer = getItsNatTableHeaderCellRenderer();
        if (renderer != null)
        {
            for(int i = fromIndex; i <= toIndex; i++)
                unrenderTableHeaderCell(i);
        }

        super.removeElementRangeBase(fromIndex,toIndex);
    }


    /**
     * ESTE METODO FUE PUBLICO ANTES
     * 
     * Removes all header columns. The header is now empty.
     * @see org.itsnat.core.domutil.ElementListBase#removeAllElements()
     */
    public void removeAllElements()
    {
        ItsNatTableHeaderCellRenderer renderer = getItsNatTableHeaderCellRenderer();
        if (renderer != null)
        {
            int len = getLength();
            for(int i = 0; i < len; i++)
                unrenderTableHeaderCell(i);
        }

        super.removeAllElementsBase();
    }

    public boolean isUsePatternMarkupToRender()
    {
        return super.isUsePatternMarkupToRenderBase();
    }

    public void setUsePatternMarkupToRender(boolean value)
    {
        super.setUsePatternMarkupToRenderBase(value);
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
