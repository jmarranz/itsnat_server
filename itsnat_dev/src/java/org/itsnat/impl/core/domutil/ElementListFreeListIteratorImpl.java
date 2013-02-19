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

import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ElementListFreeListIteratorImpl extends ElementListFreeIteratorImpl implements ListIterator<Element>
{
    protected Element prevElem;

    /**
     * Creates a new instance of ElementListFreeListIteratorImpl
     */
    public ElementListFreeListIteratorImpl(ElementListFreeInternal list,int nextIndex)
    {
        super(list,nextIndex);

        this.prevElem = list.getElementAt(nextIndex - 1);
    }

    public boolean hasPrevious()
    {
        return (prevElem != null);
    }

    public Element previous()
    {
        if (prevElem == null) throw new NoSuchElementException();

        this.nextIndex--;
        this.currIndex = nextIndex;

        this.nextElem = currElem;
        this.currElem = prevElem;
        this.prevElem = list.getPreviousSiblingElement(currIndex,currElem); // Devuelve null si no hay más

        return currElem;
    }

    public Element next()
    {
        Element res = super.next();

        this.prevElem = currElem;

        return res;
    }

    public int nextIndex()
    {
        return nextIndex;
    }

    public int previousIndex()
    {
        return nextIndex - 1;
    }

    public void set(Element o)
    {
        if (currIndex == -1) throw new IllegalStateException(); // Pues es el que queremos cambiar y lo mismo nos lo cargamos antes o bien no hemos llamado a next() nunca
        ElementPair res = list.setElementAt2(currIndex,o);
        this.currElem = res.getNewElem();
    }

    public void add(Element o)
    {
        Element currElem = o;
        this.currElem = list.insertBeforeElement(nextIndex,currElem,nextElem);
        this.prevElem = this.currElem;
        nextIndex++;
        currIndex++;
    }

}
