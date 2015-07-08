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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.domutil.ListElementInfo;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

/**
 *
 * @author jmarranz
 */
public abstract class ElementListFreeImpl extends ElementListBaseImpl implements ElementListFreeInternal
{
    protected Element parentElement;

    /**
     * Creates a new instance of ElementListFreeImpl
     */
    public ElementListFreeImpl(Element parentElement,ItsNatDocumentImpl itsNatDoc)
    {
        super(itsNatDoc);

        this.parentElement = parentElement;
    }

    public Element getParentElement()
    {
        return parentElement;
    }
/*
    public Element getNextSiblingElement(int index,Element ref)
    {
        return getNextSiblingElement(ref);
    }
*/
    public Element getNextSiblingElement(Element ref)
    {
        // ref debe pertenecer a la lista
        // Filtramos los nodos de texto
        return ItsNatTreeWalker.getNextSiblingElement(ref);
    }

    public Element getPreviousSiblingElement(Element ref)
    {
        // ref debe pertenecer a la lista
        // Filtramos los nodos de texto
        return ItsNatTreeWalker.getPreviousSiblingElement(ref);
    }

    protected Element cloneNewElementIfNeeded(Element newNode)
    {
        if (newNode.getParentNode() == getParentElement())
        {
            // Pertenece a la propia lista por tanto al añadirse
            // implícitamente se remueve primero de su posición actual, esto
            // altera totalmente el sistema de índices en el caso
            // master y es una acción "colateral" no deseada que rompe la unicidad de las operaciones.
            // Por ello lo clonamos y por tanto será "nuevo", así permitimos que funcionen
            // algoritmos de java.util.Collection tal y como sort, shuffle etc
            return (Element)newNode.cloneNode(true);
        }
        else
            return newNode;
    }

    public void addElement(Element newNode)
    {
        addElement2(newNode);
    }

    public Element addElement2(Element newNode)
    {
        newNode = cloneNewElementIfNeeded(newNode);

        addElementInternal(newNode);

        return newNode;
    }

    protected void addElementInternal(Element newNode)
    {
        // Puede derivarse
        parentElement.appendChild(newNode);
    }

    public void insertElementAt(int index,Element newNode)
    {
        insertElementAt2(index,newNode);
    }

    public Element insertElementAt2(int index,Element newNode)
    {
        newNode = cloneNewElementIfNeeded(newNode);

        insertElementAtInternal(index,newNode);

        return newNode;
    }

    protected void insertElementAtInternal(int index,Element newNode)
    {
        // Puede derivarse
        Element childRef = getElementAt(index); // Puede ser null (caso append)
        parentElement.insertBefore(newNode,childRef);
    }

    public Element setElementAt(int index,Element newNode)
    {
        ElementPair res = setElementAt2(index,newNode);
        return res.getOldElem();
    }

    public ElementPair setElementAt2(int index,Element newNode)
    {
        Element currNode = getElementAt(index);
        if (currNode == null) throw new ItsNatException("No Element found at " + index,this);
        if (currNode == newNode)
            return new ElementPair(currNode,newNode); // Nada que hacer

        newNode = cloneNewElementIfNeeded(newNode);

        Element oldNode = setElementAtInternal(index,currNode,newNode);
        return new ElementPair(oldNode,newNode);
    }

    protected Element setElementAtInternal(int index,Element currNode,Element newNode)
    {
        // Puede derivarse
        parentElement.insertBefore(newNode,currNode);
        parentElement.removeChild(currNode);
        return currNode;
    }

    public Element insertBeforeElement(int index,Element newNode,Element refNode)
    {
        newNode = cloneNewElementIfNeeded(newNode);

        insertBeforeElementInternal(index,newNode,refNode);

        return newNode;
    }

    protected void insertBeforeElementInternal(int index,Element newNode,Element refNode)
    {
        // Puede derivarse
        // Usar index en el caso master (derivada)
        parentElement.insertBefore(newNode,refNode); // Si refNode es nulo se inserta al final
    }

    public void removeElement(int index,Element node)
    {
        // Usar index en el caso master (derivada)
        parentElement.removeChild(node);
    }

    public Element removeElementAt(int index)
    {
        Element child = getElementAt(index);
        if (child == null) return null; // No existe, fuera de rango
        parentElement.removeChild(child);
        return child;
    }

    public void removeElementRange(int fromIndex, int toIndex)
    {
        int count = toIndex - fromIndex + 1;
        if (count <= 0) return;

        // Toleramos que toIndex por ejemplo esté fuera de rango
        Element last = null;
        int i = toIndex;
        for( ; i >= fromIndex; i--)
        {
            last = getElementAt(i);
            if (last != null) break;
        }
        if (last == null) return; // Nada que hacer
        toIndex = i;
        count = toIndex - fromIndex + 1;
        if (count <= 0) return;

        // Técnica más rápida de borrado que usar índices:
        for(i = 1; i <= count; i++)
        {
            Element prev = last;
            last = getPreviousSiblingElement(last);
            parentElement.removeChild(prev);
        }
    }

    public void removeAllElements()
    {
        while(!isEmpty())
            removeElementAt(0);
    }


    public Element getElementFromNode(Node node)
    {
        return getChildElementFromNode(node,null);
    }

    public Element getChildElementFromNode(Node node,Element limitElem)
    {
        // node es un nodo que forma parte de un elemento de la lista
        // obtenemos el nodo elemento de la lista, dicho elemento
        // es que tiene como padre el padre de la lista

        Node child = DOMUtilInternal.getChildTopMostContainingNode(node,getParentElement(),limitElem);
        return (Element)child; // Si null es que no forma parte de la lista
    }

    public Element[] getElements()
    {
        Element[] elemList = createElementArray(getLength());
        fillElements(elemList);
        return elemList;
    }

    public ListElementInfo getListElementInfoFromNode(Node node)
    {
        return getListElementInfoFromNode(node,null);
    }

    public ListElementInfo getListElementInfoAt(int index)
    {
        // La versión pública
        return getListElementInfo(index);
    }

    public ListElementInfoImpl getListElementInfoFromNode(Node node,Element limitElem)
    {
        // Uso interno
        Element elem = getChildElementFromNode(node,limitElem);
        if (elem == null) return null; // node no forma parte de la lista
        return getListElementInfo(elem);
    }

    public void moveElement(int start,int end,int to)
    {
        Element elemCurrent = getElementAt(start);
        int count = end - start + 1;
        List<Element> movedElems = new ArrayList<Element>(count);

        for(int i = start; i <= end; i++)
        {
             // Usamos start y no i porque al borrar se mueven los siguientes
            movedElems.add(elemCurrent);
            Element elemTmp = elemCurrent;
            elemCurrent = getNextSiblingElement(start,elemCurrent);
            removeElement(start,elemTmp);
        }
        Element refElem = getElementAt(to); // puede ser null (añadir al final)
        for(int i = 0; i < count; i++)
        {
            elemCurrent = movedElems.get(i);
            int pos = to + i;
            insertBeforeElement(pos,elemCurrent,refElem);
        }
    }

    public Element[] setElements(Element[] newElems)
    {
        Element[] replaced = getElements();

        removeAllElements();

        for(int i = 0; i < newElems.length; i++)
            addElement(newElems[i]);

        return replaced;
    }

    public abstract void fillElements(Element[] elemList);


    public Element[] createElementArray(int len)
    {
        Element parentElement = getParentElement();
        if (parentElement instanceof HTMLTableSectionElement)
            return new HTMLTableRowElement[len];
        else if (parentElement instanceof HTMLTableRowElement)
            return new HTMLTableCellElement[len];
        else
            return new Element[len];
    }

    public abstract ListElementInfoImpl getListElementInfo(int index,Element elem);

    public abstract ListElementInfoImpl getListElementInfo(Element elem);

    public abstract ListElementInfoImpl getListElementInfo(int index);

    // java.util.List methods

    public int indexOf(Object o)
    {
        return ElementListFreeUtil.indexOf(this,o);
    }

    public boolean contains(Object o)
    {
        return ElementListFreeUtil.contains(this,o);
    }

    public boolean add(Element o)
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

    public <T> T[] toArray(T[] a)
    {
        return ElementListFreeUtil.toArray(this,a);
    }

    public Element get(int index)
    {
        return ElementListFreeUtil.get(this,index);
    }

    public Iterator<Element> iterator()
    {
        return ElementListFreeUtil.iterator(this);
    }

    public ListIterator<Element> listIterator(int index)
    {
        return ElementListFreeUtil.listIterator(this,index);
    }

    public ListIterator<Element> listIterator()
    {
        return ElementListFreeUtil.listIterator(this);
    }

    public Element remove(int index)
    {
        return ElementListFreeUtil.remove(this,index);
    }

    public boolean addAll(int index, Collection<? extends Element> c)
    {
        return ElementListFreeUtil.addAll(this,index,c);
    }

    public boolean containsAll(Collection<?> c)
    {
        return ElementListFreeUtil.containsAll(this,c);
    }

    public boolean addAll(Collection<? extends Element> c)
    {
        return ElementListFreeUtil.addAll(this,c);
    }

    public boolean removeAll(Collection<?> c)
    {
        return ElementListFreeUtil.removeAll(this,c);
    }

    public boolean retainAll(Collection<?> c)
    {
        return ElementListFreeUtil.retainAll(this,c);
    }

    public void add(int index, Element element)
    {
        ElementListFreeUtil.add(this,index,element);
    }

    public Element set(int index, Element element)
    {
        return ElementListFreeUtil.set(this,index,element);
    }

    public Object[] toArray()
    {
        return ElementListFreeUtil.toArray(this);
    }

    public List<Element> subList(int fromIndex, int toIndex)
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

}
