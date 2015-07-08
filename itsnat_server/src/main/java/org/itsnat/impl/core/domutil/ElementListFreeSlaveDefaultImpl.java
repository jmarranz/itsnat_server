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

import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.w3c.dom.Element;

/**
 * El DOM es el que manda, puede modificarse añadiendo nuevos elementos
 * directamente via DOM, la lista "se entera" de los cambios, el coste: más lento.
 *
 * @author jmarranz
 */
public class ElementListFreeSlaveDefaultImpl extends ElementListFreeSlaveImpl
{
    /**
     * Creates a new instance of ElementListFreeSlaveDefaultImpl
     */
    public ElementListFreeSlaveDefaultImpl(Element parentElement,ItsNatDocumentImpl itsNatDoc)
    {
        super(parentElement,itsNatDoc);
    }

    public static boolean isEmpty(Element parentElement)
    {
        // Este algoritmo es mucho más rápido que (getLength() != 0)
        // pues no necesita contar todos
        // Filtramos los nodos de texto
        Element child = ItsNatTreeWalker.getFirstChildElement(parentElement);
        return (child == null);
    }

    public boolean isEmpty()
    {
        return isEmpty(parentElement);
    }

    public int getLength()
    {
        // Filtramos los nodos de texto
        return ItsNatTreeWalker.getChildElementCount(parentElement);
    }

    public static Element getElementAt(Element parentElement,int index)
    {
        if (index < 0) return null;

        // Filtramos los nodos de texto
        int count = 0;
        Element child = ItsNatTreeWalker.getFirstChildElement(parentElement);
        while(child != null)
        {
            if (count == index)
                return child;
            count++;
            child = ItsNatTreeWalker.getNextSiblingElement(child);
        }
        return null; // Fuera de rango
    }

    public Element getElementAt(int index)
    {
        return getElementAt(parentElement,index);
    }

    public Element getFirstElement()
    {
        // Filtramos los nodos de texto
        return ItsNatTreeWalker.getFirstChildElement(parentElement); // Puede ser null
    }

    public Element getLastElement()
    {
        // Filtramos los nodos de texto
        return ItsNatTreeWalker.getLastChildElement(parentElement);
    }

    public int indexOfElement(Element node)
    {
        return indexOfElement(node,parentElement);
    }

    public int lastIndexOfElement(Element node)
    {
        return lastIndexOfElement(node,parentElement);
    }

    public static int indexOfElement(Element node,Element parentElement)
    {
        if (node == null) return -1;

        // Filtramos los nodos de texto etc
        int count = 0;
        Element child = ItsNatTreeWalker.getFirstChildElement(parentElement);
        while(child != null)
        {
            if (child == node)
                return count;
            count++;
            child = ItsNatTreeWalker.getNextSiblingElement(child);
        }
        return -1; // El nodo no forma parte de la lista
    }

    public static int lastIndexOfElement(Element node,Element parentElement)
    {
        if (node == null) return -1;

        // Buscamos hacia adelante pues si buscamos desde el final necesitaremos
        // el getLength() que de todas formas recorrerá todos los elementos

        // Filtramos los nodos de texto etc
        int count = 0;
        int index = -1;
        Element child = ItsNatTreeWalker.getFirstChildElement(parentElement);
        while(child != null)
        {
            if (child == node)
                index = count;
            count++;
            child = ItsNatTreeWalker.getNextSiblingElement(child);
        }
        return index; // Recordará el último
    }

    public void fillElements(Element[] elemList)
    {
        if (elemList.length > 0)
        {
            int i = 0;
            Element child = ItsNatTreeWalker.getFirstChildElement(parentElement);
            while(child != null)
            {
                elemList[i] = child;

                i++;
                child = ItsNatTreeWalker.getNextSiblingElement(child);
            }
        }
    }

    public Element getNextSiblingElement(int index,Element ref)
    {
        return getNextSiblingElement(ref);
    }

    public Element getPreviousSiblingElement(int index,Element ref)
    {
        return getPreviousSiblingElement(ref);
    }
}
