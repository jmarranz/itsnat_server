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

import org.itsnat.impl.comp.ItsNatElementComponentUIImpl;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.comp.ItsNatElementComponentImpl;
import org.itsnat.impl.core.domutil.ElementListImpl;
import org.itsnat.impl.core.domutil.ListElementInfoMasterImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatListBasedUIImpl extends ItsNatElementComponentUIImpl
{
    protected ElementListImpl elementList;

    /**
     * Creates a new instance of ItsNatListBasedUIImpl
     */
    public ItsNatListBasedUIImpl(ItsNatElementComponentImpl parentComp)
    {
        super(parentComp);
    }

    public void setNewElementValueAt(int index,Object anObject,Element optionElem)
    {
        // Está siendo creado el objeto, por tanto no está seleccionado por defecto (y del foco no nos ocupamos: false)
        setElementValueAt(index,anObject,false,false,optionElem,true);
    }

    public abstract Element getContentElementAt(int index);

    protected Element getContentElementAtBase(int index)
    {
        Element optionElem = getElementAt(index);
        if (optionElem == null) return null;
        return getContentElementAt(index,optionElem);
    }

    public abstract Element getContentElementAt(int index,Element optionElem);

    public abstract void setElementValueAt(int index,Object anObject,boolean isSelected,boolean cellHasFocus,Element optionElem,boolean isNew);

    //public abstract void setElementValueAt(int index,Object anObject,boolean isSelected,boolean cellHasFocus);

    protected void setElementValueAtBase(int index,Object anObject,boolean isSelected,boolean cellHasFocus)
    {
        Element optionElem = getElementAt(index);
        if (optionElem == null) throw new ItsNatException("Index out of bounds: " + index,this);
        setElementValueAt(index,anObject,isSelected,cellHasFocus,optionElem,false);
    }

    public abstract boolean isEmpty();

    protected boolean isEmptyBase()
    {
        return elementList.isEmpty();
    }

    public abstract int getLength();

    protected int getLengthBase()
    {
        return elementList.getLength();
    }

    public abstract void setLength(int len);

    protected void setLengthBase(int len)
    {
        elementList.setLength(len);
    }

    public abstract Element getElementAt(int index);

    protected Element getElementAtBase(int index)
    {
        return elementList.getElementAt(index);
    }

    public abstract ItsNatListBasedCellUIImpl createItsNatListCellUI(ListElementInfoMasterImpl elementInfo);

    public ItsNatListBasedCellUIImpl getItsNatListBasedCellUI(ListElementInfoMasterImpl elementInfo)
    {
        if (elementInfo == null) return null;

        ItsNatListBasedCellUIImpl elementUI = (ItsNatListBasedCellUIImpl)elementInfo.getAuxObject();
        if (elementUI == null)
        {
            elementUI = createItsNatListCellUI(elementInfo);
            elementInfo.setAuxObject(elementUI);
        }
        return elementUI;
    }

    public ItsNatListBasedCellUIImpl getItsNatListBasedCellUIAt(int index)
    {
        ListElementInfoMasterImpl elemInfo = (ListElementInfoMasterImpl)elementList.getListElementInfoAt(index);
        return getItsNatListBasedCellUI(elemInfo);
    }

    public ItsNatListBasedCellUIImpl getItsNatListBasedCellUIFromNode(Node node)
    {
        ListElementInfoMasterImpl elemInfo = (ListElementInfoMasterImpl)elementList.getListElementInfoFromNode(node);
        return getItsNatListBasedCellUI(elemInfo);
    }

//    public abstract Element insertElementAt(int index,Object anObject);

    protected Element insertElementAtBase(int index,Object anObject)
    {
        Element newElem = elementList.insertElementAt(index);
        setNewElementValueAt(index,anObject,newElem);
        return newElem;
    }

    public abstract Element removeElementAt(int index);

    protected Element removeElementAtBase(int index)
    {
        return elementList.removeElementAt(index);
    }

    public abstract void removeElementRange(int fromIndex,int toIndex);

    protected void removeElementRangeBase(int fromIndex,int toIndex)
    {
        elementList.removeElementRange(fromIndex,toIndex);
    }

    public abstract void removeAllElements();

    protected void removeAllElementsBase()
    {
        elementList.removeAllElements();
    }

    public abstract boolean isUsePatternMarkupToRender();

    protected boolean isUsePatternMarkupToRenderBase()
    {
        return elementList.isUsePatternMarkupToRender();
    }

    public abstract void setUsePatternMarkupToRender(boolean value);

    protected void setUsePatternMarkupToRenderBase(boolean value)
    {
        elementList.setUsePatternMarkupToRender(value);
    }
}
