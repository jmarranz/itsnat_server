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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author jmarranz
 */
public class NodeListByNameImpl extends NodeListBaseImpl implements NodeList
{
    protected String elemName;

    public NodeListByNameImpl(Document doc,String elemName)
    {
        super(doc);
        this.elemName = elemName;
    }

    public boolean isRecursive()
    {
        return true;
    }

    public boolean machElement(Element elem,String name)
    {
        return elem.getAttribute("name").equals(name);
    }

    public int getLength()
    {
        return getLength(elemName);
    }

    public Node item(int index)
    {
        return item(index,elemName);
    }
}
