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

package org.itsnat.impl.comp.list;

import org.itsnat.comp.list.ItsNatList;
import org.itsnat.comp.list.ItsNatListCellRenderer;
import org.itsnat.comp.list.ItsNatListCellUI;
import org.itsnat.impl.comp.ItsNatElementComponentImpl;
import org.itsnat.impl.core.domutil.ListElementInfoMasterImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatListUIImpl extends ItsNatListBasedUIImpl implements ItsNatListUIInternal
{

    /**
     * Creates a new instance of ItsNatListUIImpl
     */
    public ItsNatListUIImpl(ItsNatListInternal parentComp)
    {
        super((ItsNatElementComponentImpl)parentComp);
    }

    public ItsNatList getItsNatList()
    {
        return (ItsNatList)parentComp;
    }

    public ItsNatListInternal getItsNatListInternal()
    {
        return (ItsNatListInternal)parentComp;
    }

    public ItsNatListCellRenderer getItsNatListCellRenderer()
    {
        return getItsNatList().getItsNatListCellRenderer();
    }

    public void setElementValueAt(int index,Object anObject,boolean isSelected,boolean cellHasFocus,Element optionElem,boolean isNew)
    {
        Element contentElem = getContentElementAt(index,optionElem);
        elementList.prepareRendering(contentElem,isNew);

        ItsNatListCellRenderer renderer = getItsNatListCellRenderer();
        if (renderer != null)
            renderer.renderListCell(getItsNatList(),index,anObject,isSelected,cellHasFocus,contentElem,isNew);
    }

    public ItsNatListBasedCellUIImpl createItsNatListCellUI(ListElementInfoMasterImpl elementInfo)
    {
        return new ItsNatListCellUIImpl(elementInfo,this);
    }

    public ItsNatListCellUI getItsNatListCellUIAt(int index)
    {
        return (ItsNatListCellUI)getItsNatListBasedCellUIAt(index);
    }

    public ItsNatListCellUI getItsNatListCellUIFromNode(Node node)
    {
        return (ItsNatListCellUI)getItsNatListBasedCellUIFromNode(node);
    }

    public Element getContentElementAt(int index)
    {
        return super.getContentElementAtBase(index);
    }

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
     * Increases or shrinks the list to fit the new size.
     *
     * <p>If the new size is bigger new elements are added at the end, if the size
     * is lower tail elements are removed.</p>
     *
     * @param len the new number of child elements.
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

    public Element insertElementAt(int index,Object anObject)
    {
        return super.insertElementAtBase(index,anObject);
    }

    public void unrenderList(int index)
    {
        ItsNatListCellRenderer renderer = getItsNatListCellRenderer();
        if (renderer == null) return;

        Element optionElem = getElementAt(index);
        if (optionElem == null) return;

        Element contentElem = getContentElementAt(index,optionElem);
        renderer.unrenderListCell(getItsNatList(),index,contentElem);
    }

    /**
     * ESTE METODO FUE PUBLICO ANTES
     * 
     * Removes the specified child element.
     *
     * @param index index of the element to remove.
     * @return the removed element or null if index is out of bounds.
     * @see org.itsnat.core.domutil.ElementListBase#removeElementAt(int)
     */
    public Element removeElementAt(int index)
    {
        unrenderList(index);

        return super.removeElementAtBase(index);
    }

    /**
     * ESTE METODO FUE PUBLICO ANTES
     * 
     * Removes the child elements between the specified indexes.
     *
     * @param fromIndex low index (inclusive).
     * @param toIndex high index (inclusive).
     * @see org.itsnat.core.domutil.ElementListBase#removeElementRange(int,int)
     */
    public void removeElementRange(int fromIndex,int toIndex)
    {
        ItsNatListCellRenderer renderer = getItsNatListCellRenderer();
        if (renderer != null)
        {
            for(int i = fromIndex; i <= toIndex; i++)
                unrenderList(i);
        }

        super.removeElementRangeBase(fromIndex,toIndex);
    }

    public void removeAllElements()
    {
        ItsNatListCellRenderer renderer = getItsNatListCellRenderer();
        if (renderer != null)
        {
            int len = getLength();
            for(int i = 0; i < len; i++)
                unrenderList(i);
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
}
