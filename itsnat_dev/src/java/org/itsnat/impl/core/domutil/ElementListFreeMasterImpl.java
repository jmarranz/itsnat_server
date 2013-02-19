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
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.w3c.dom.Element;

/**
 * Manda sobre el DOM, NO debería modificarse el DOM directamente sin la lista
 * pues ésta quedaría desincronizada. Es más rápida que la versión "slave"
 *
 * @author jmarranz
 */
public class ElementListFreeMasterImpl extends ElementListFreeImpl
{
    protected ArrayList<ListElementInfoMasterImpl> elements = new ArrayList<ListElementInfoMasterImpl>();

    /**
     * Creates a new instance of ElementListDOMMasterImpl
     */
    public ElementListFreeMasterImpl(Element parentElement,ItsNatDocumentImpl itsNatDoc)
    {
        super(parentElement,itsNatDoc);

        initialSync();
    }

    public boolean isMaster()
    {
        return true;
    }

    public void initialSync()
    {
        elements.clear(); // Por si acaso

        Element child = ItsNatTreeWalker.getFirstChildElement(parentElement);
        while(child != null)
        {
            addListElementInfo(child);

            child = ItsNatTreeWalker.getNextSiblingElement(child);
        }

        // A partir de ahora NO debería modificarse el DOM directamente,
        // usar esta lista
    }

    public ListElementInfoImpl getListElementInfo(int index,Element elem)
    {
        return getListElementInfo(index);
    }

    public ListElementInfoImpl getListElementInfo(int index)
    {
        return elements.get(index);
    }

    public ListElementInfoImpl addListElementInfo(Element elem)
    {
        ListElementInfoMasterImpl elemInfo = new ListElementInfoMasterImpl(elements.size(),elem,this);
        elements.add(elemInfo);
        return elemInfo;
    }

    public ListElementInfoImpl insertListElementInfo(int index,Element elem)
    {
        ListElementInfoMasterImpl elemInfo = new ListElementInfoMasterImpl(index,elem,this);
        elements.add(index,elemInfo);
        for(int i = index + 1; i < elements.size(); i++)
        {
            ListElementInfoMasterImpl currElemInfo = elements.get(i);
            currElemInfo.setIndex(currElemInfo.getIndex() + 1);
        }
        return elemInfo;
    }

    public void removeListElementInfo(int index)
    {
        elements.remove(index);
        for(int i = index; i < elements.size(); i++)
        {
            ListElementInfoMasterImpl currElemInfo = elements.get(i);
            currElemInfo.setIndex(currElemInfo.getIndex() - 1);
        }
    }

    public void removeListElementInfoRange(int fromIndex,int toIndex)
    {
        int count = toIndex - fromIndex + 1;
        for(int i = 1; i <= count; i++)
            elements.remove(fromIndex);

        for(int i = fromIndex; i < elements.size(); i++)
        {
            ListElementInfoMasterImpl currElemInfo = elements.get(i);
            currElemInfo.setIndex(currElemInfo.getIndex() - count);
        }
    }


    public ListElementInfoImpl getListElementInfo(Element elem)
    {
        if (elem == null) return null;

        int len = getLength();
        for(int i = 0; i < len; i++)
        {
            ListElementInfoImpl elemInfo = getListElementInfo(i);
            Element currElem = elemInfo.getElement();
            if (currElem == elem)
                return elemInfo;
        }
        return null; // El nodo no forma parte de la lista
    }

    public boolean isEmpty()
    {
        return elements.isEmpty();
    }

    public int getLength()
    {
        return elements.size();
    }

    public boolean isOutOfRange(int index)
    {
        return (index < 0) || (index >= elements.size());
    }

    public Element getElementAt(int index)
    {
        if (isOutOfRange(index)) return null;

        ListElementInfoImpl elemInfo = getListElementInfo(index);
        return elemInfo.getElement();
    }

    public Element getFirstElement()
    {
        return getElementAt(0);
    }

    public Element getLastElement()
    {
        return getElementAt(getLength() - 1);
    }

    public int indexOfElement(Element node)
    {
        if (node == null) return -1;

        int len = getLength();
        for(int i = 0; i < len; i++)
        {
            Element child = getElementAt(i);
            if (child == node)
                return i;
        }
        return -1; // El nodo no forma parte de la lista
    }

    public int lastIndexOfElement(Element node)
    {
        if (node == null) return -1;

        int len = getLength();
        for(int i = len - 1; i >= 0; i--)
        {
            Element child = getElementAt(i);
            if (child == node)
                return i;
        }
        return -1; // El nodo no forma parte de la lista
    }

    public void fillElements(Element[] elemList)
    {
        for(int i = 0; i < elemList.length; i++)
        {
            Element elem = getElementAt(i);
            elemList[i] = elem;
        }
    }

    @Override
    protected void addElementInternal(Element newNode)
    {
        super.addElementInternal(newNode);

        addListElementInfo(newNode);
    }

    @Override
    protected void insertBeforeElementInternal(int index,Element newNode,Element refNode)
    {
        super.insertBeforeElementInternal(index,newNode,refNode); // Si refNode es nulo se inserta al final

        insertListElementInfo(index,newNode);
    }

    @Override
    protected void insertElementAtInternal(int index,Element newNode)
    {
        super.insertElementAtInternal(index,newNode);

        insertListElementInfo(index,newNode);
    }

    @Override
    protected Element setElementAtInternal(int index,Element currNode,Element newNode)
    {
        Element res = super.setElementAtInternal(index,currNode,newNode);

        updateListElementInfo(index,newNode);

        return res;
    }

    public ListElementInfoImpl updateListElementInfo(int index,Element elem)
    {
        ListElementInfoMasterImpl elemInfo = (ListElementInfoMasterImpl)getListElementInfo(index);
        elemInfo.setElement(elem);
        return elemInfo;
    }

    @Override
    public void removeElement(int index,Element node)
    {
        super.removeElement(index,node);

        removeListElementInfo(index);
    }

    @Override
    public Element removeElementAt(int index)
    {
        Element child = super.removeElementAt(index);
        if (child == null) return null; // No existe

        removeListElementInfo(index);

        return child;
    }

    @Override
    public void removeElementRange(int fromIndex, int toIndex)
    {
        super.removeElementRange(fromIndex,toIndex);

        removeListElementInfoRange(fromIndex,toIndex);
    }

    public Element getNextSiblingElement(int index,Element ref)
    {
        return getElementAt(index + 1);
    }

    public Element getPreviousSiblingElement(int index,Element ref)
    {
        return getElementAt(index - 1);
    }
}
