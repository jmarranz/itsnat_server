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

import java.io.Serializable;
import org.itsnat.core.domutil.ElementListBase;
import org.itsnat.core.domutil.ListElementInfo;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class ListElementInfoImpl implements ListElementInfo,Serializable
{
    protected int index;
    protected Element elem;
    protected ElementListBaseImpl list;

    /** Creates a new instance of ListElementInfoImpl */
    public ListElementInfoImpl(int index,Element elem,ElementListBaseImpl list)
    {
        this.index = index;
        this.elem = elem;
        this.list = list;
    }

    public Element getElement()
    {
        return elem;
    }

    public int getIndex()
    {
        return index;
    }

    public ElementListBase getParentList()
    {
        return list;
    }

}
