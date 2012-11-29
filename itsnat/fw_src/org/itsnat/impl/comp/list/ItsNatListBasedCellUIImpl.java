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

package org.itsnat.impl.comp.list;

import java.io.Serializable;
import org.itsnat.impl.comp.ItsNatElementComponentUIImpl;
import org.itsnat.impl.core.domutil.ListElementInfoMasterImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatListBasedCellUIImpl implements Serializable
{
    protected ListElementInfoMasterImpl elementInfo;
    protected ItsNatElementComponentUIImpl listUI;

    /**
     * Creates a new instance of ItsNatListBasedCellUIImpl
     */
    protected ItsNatListBasedCellUIImpl(ListElementInfoMasterImpl elementInfo,ItsNatElementComponentUIImpl listUI)
    {
        this.elementInfo = elementInfo;
        this.listUI = listUI;
    }

    public Element getElement()
    {
        return elementInfo.getElement();
    }

    public int getIndex()
    {
        return elementInfo.getIndex();
    }

    public boolean containsUserValueName(String name)
    {
        return elementInfo.containsUserValueName(name);
    }

    public Object getUserValue(String name)
    {
        return elementInfo.getUserValue(name);
    }

    public Object setUserValue(String name, Object value)
    {
        return elementInfo.setUserValue(name,value);
    }

    public Object removeUserValue(String name)
    {
        return elementInfo.removeUserValue(name);
    }

    public String[] getUserValueNames()
    {
        return elementInfo.getUserValueNames();
    }
}
