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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.itsnat.core.ItsNatException;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ElementListFreeUtil
{

    public static int indexOf(ElementListFreeInternal list,Object o)
    {
        return list.indexOfElement((Element)o);
    }

    public static boolean contains(ElementListFreeInternal list,Object o)
    {
        return indexOf(list,o) != -1;
    }

    public static boolean add(ElementListFreeInternal list,Object o)
    {
        list.addElement((Element)o);
        return true; // Pues si falla se lanzaría una excepción
    }

    public static int lastIndexOf(ElementListFreeInternal list,Object o)
    {
        return list.lastIndexOfElement((Element)o);
    }

    public static boolean remove(ElementListFreeInternal list,Object o)
    {
        Element elem = (Element)o;
        int index = list.indexOfElement(elem);
        list.removeElement(index,elem);
        return true; // Porque si no contiene el nodo se lanza una excepción
    }
 
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(ElementListFreeInternal list,T[] a)        
    {
        Element[] elems = list.getElements();
        if (a.length < elems.length)
            a = (T[])java.lang.reflect.Array.newInstance(
                                a.getClass().getComponentType(), elems.length);
        System.arraycopy(elems, 0, a, 0, elems.length);

        return a;
    }

    public static Element get(ElementListFreeInternal list,int index)
    {
        return list.getElementAt(index);
    }

    public static Iterator<Element> iterator(ElementListFreeInternal list)
    {
        return new ElementListFreeIteratorImpl(list);
    }

    public static ListIterator<Element> listIterator(ElementListFreeInternal list,int index)
    {
        return new ElementListFreeListIteratorImpl(list,index);
    }

    public static ListIterator<Element> listIterator(ElementListFreeInternal list)
    {
        return new ElementListFreeListIteratorImpl(list,0);
    }

    public static Element remove(ElementListFreeInternal list,int index)
    {
        return list.removeElementAt(index);
    }

    public static boolean addAll(ElementListFreeInternal list,int index, Collection<? extends Element> c)
    {
        for(Element elem : c)
        {
            list.insertElementAt(index,elem);
            index++;
        }
        return true;
    }

    public static boolean containsAll(ElementListFreeInternal list,Collection<?> c)
    {
	for(Iterator<?> e = c.iterator(); e.hasNext(); )
        {
	    if(!list.contains(e.next()))
		return false;
        }
	return true;
    }

    public static boolean addAll(ElementListFreeInternal list,Collection<? extends Element> c)
    {
        for(Element elem : c)
        {
            list.addElement(elem);
        }
        return true;
    }

    public static boolean removeAll(ElementListFreeInternal list,Collection<?> c)
    {
	boolean modified = false;
	for(Iterator<Element> it = list.iterator(); it.hasNext(); )
        {
	    if(c.contains(it.next()))
            {
		it.remove();
		modified = true;
	    }
	}
	return modified;
    }

    public static boolean retainAll(ElementListFreeInternal list,Collection<?> c)
    {
	boolean modified = false;
	for(Iterator<Element> it = list.iterator(); it.hasNext(); )
        {
	    if(!c.contains(it.next()))
            {
		it.remove();
		modified = true;
	    }
	}
	return modified;
    }

    public static void add(ElementListFreeInternal list,int index, Object element)
    {
        list.insertElementAt(index,(Element)element);
    }

    public static Element set(ElementListFreeInternal list,int index, Element element)
    {
        return list.setElementAt(index,element);
    }

    public static Element[] toArray(ElementListFreeInternal list)
    {
        return list.getElements();
    }

    public static List<Element> subList(ElementListFreeInternal list,int fromIndex, int toIndex)
    {
        throw new ItsNatException("Not implemented",list);
    }

    public static int size(ElementListFreeInternal list)
    {
        return list.getLength();
    }

    public static void clear(ElementListFreeInternal list)
    {
        list.removeAllElements();
    }
}
