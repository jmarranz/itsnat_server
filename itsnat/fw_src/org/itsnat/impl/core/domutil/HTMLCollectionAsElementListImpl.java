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

import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;

/**
 * Permite que un HTMLCollection pueda ser visto como un NodeList
 * Es una lista "esclava" pues en teoría el HTMLCollection
 * se actualizará automáticamente cuando directamente añadamos o quitemos
 * via DOM elementos.
 * 
 * No esperamos que sea más rápida que usando ElementListFreeSlaveImpl pero
 * por si acaso una implementación DOM consigue un rendimiento mejor que por
 * la fuerza bruta.
 *
 * @author jmarranz
 */
public class HTMLCollectionAsElementListImpl extends ElementListFreeSlaveImpl
{
    protected HTMLCollection collection;

    /**
     * Creates a new instance of ItsNatHTMLElementCollectionImpl
     */
    public HTMLCollectionAsElementListImpl(HTMLElement parentElement,HTMLCollection collection,ItsNatDocumentImpl itsNatDoc)
    {
        super(parentElement,itsNatDoc);

        // Se supone que parentElement es el padre de los elementos de la colección
        this.collection = collection;
    }

    public int getLength()
    {
        return getHTMLCollection().getLength();
    }

    public Element getElementAt(int index)
    {
        return (Element)getHTMLCollection().item(index);
    }

    public boolean isEmpty()
    {
        return (getHTMLCollection().getLength() == 0);
    }

    public Element getFirstElement()
    {
        return (Element)getHTMLCollection().item(0);
    }

    public Element getLastElement()
    {
        return (Element)getHTMLCollection().item(getLength() -1);
    }

    public int indexOfElement(Element node)
    {
        return indexOfElement(node,getHTMLCollection());
    }

    public int lastIndexOfElement(Element node)
    {
        return lastIndexOfElement(node,getHTMLCollection());
    }

    public static int indexOfElement(Element node,HTMLCollection col)
    {
        if (node == null) return -1;

        int len = col.getLength();
        for(int i = 0; i < len; i++)
        {
            Element child = (Element)col.item(i);
            if (child == node)
                return i;
        }
        return -1; // El nodo no forma parte de la lista
    }

    public static int lastIndexOfElement(Element node,HTMLCollection col)
    {
        if (node == null) return -1;

        int len = col.getLength();
        for(int i = len - 1; i >= 0; i--)
        {
            Element child = (Element)col.item(i);
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

    public Element getNextSiblingElement(int index,Element ref)
    {
        return getElementAt(index + 1);
    }

    public Element getPreviousSiblingElement(int index,Element ref)
    {
        return getElementAt(index - 1);
    }

    public HTMLCollection getHTMLCollection()
    {
        return collection;
    }
}
