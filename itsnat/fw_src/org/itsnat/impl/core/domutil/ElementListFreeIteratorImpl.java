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

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.w3c.dom.Element;

/**
 * Esta clase es debida a que a través de un iterador es más
 * eficaz recorrer la lista que a través de índices enteros
 *
 * @author jmarranz
 */
public class ElementListFreeIteratorImpl implements Iterator<Element>
{
    protected ElementListFreeInternal list;
    protected Element currElem;
    protected Element nextElem;
    protected int nextIndex;
    protected int currIndex;

    /**
     * Creates a new instance of ElementListFreeIteratorImpl
     */
    public ElementListFreeIteratorImpl(ElementListFreeInternal list,int nextIndex)
    {
        this.list = list;
        this.currElem = null;
        this.nextElem = list.getElementAt(nextIndex); // Si la lista es nula no da error, devuelve null

        this.nextIndex = nextIndex;
        this.currIndex = -1; // Se necesita un next() o previous() para definirse
    }

    public ElementListFreeIteratorImpl(ElementListFreeInternal list)
    {
        this(list,0);
    }

    public boolean hasNext()
    {
        return (nextElem != null);
    }

    public Element next()
    {
        if (nextElem == null) throw new NoSuchElementException();

        this.nextIndex++;
        this.currIndex = nextIndex - 1;

        this.currElem = nextElem;
        this.nextElem = list.getNextSiblingElement(currIndex,currElem); // Devuelve null si no hay más

        return currElem;
    }

    public void remove()
    {
        if (currIndex == -1) throw new IllegalStateException(); // No se puede borrar dos veces o bien no se ha llamado nunca a next() o previous()
        list.removeElement(currIndex,currElem);
        this.currElem = nextElem; // Para el next() da igual pero para el previous() permite que nextElem se defina correctamente

        nextIndex--;
        currIndex = -1; // Necesita un next() o previous() para actualizarse
    }
}
