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

package org.itsnat.impl.core.domimpl.html;

import java.io.Serializable;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class NodeListBaseImpl implements Serializable
{
    protected Node topNode;

    public NodeListBaseImpl(Node topNode)
    {
        if (topNode == null) throw new DOMException(DOMException.NOT_FOUND_ERR,"The parent node cannot be null"); // Es el caso por ejemplo de que el documentElement no esté añadido todavía
        this.topNode = topNode;
    }

    protected Element searchChild(Node parent,NumberCounter counter,String name)
    {
        Element child = ItsNatTreeWalker.getFirstChildElement(parent);
        while(child != null)
        {
            if (machElement(child,name))
            {
                if (counter.isFinished())
                    return child;
                counter.inc();
            }
            else if (isRecursive())
            {
                Element found = searchChild(child,counter,name);
                if (found != null)
                    return found;
            }
            child = ItsNatTreeWalker.getNextSiblingElement(child);
        }
        return null;
    }

    public int getLength(String name)
    {
        // Como debe ser tolerante a cambios, tenemos que recalcular el valor
        // cada vez.
        NumberCounter counter = new NumberCounter(Integer.MAX_VALUE);
        searchChild(topNode,counter,name);
        return counter.getCount();
    }

    public Node item(int index,String name)
    {
        // Como debe ser tolerante a cambios, tenemos que recalcular el valor
        // cada vez.
        if (index < 0) return null;
        return searchChild(topNode,new NumberCounter(index),name);
    }

    public abstract boolean isRecursive();

    public abstract boolean machElement(Element elem,String name);

    public static class NumberCounter
    {
        private int index;
        private int count;

        public NumberCounter(int index)
        {
            this.index = index;
        }

        public boolean isFinished()
        {
            return index == count;
        }

        public int inc()
        {
            count++;
            return count;
        }

        public int getCount()
        {
            return count;
        }
    }
}
