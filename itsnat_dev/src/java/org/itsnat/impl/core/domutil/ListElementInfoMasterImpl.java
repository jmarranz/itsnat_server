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

import org.itsnat.impl.core.ItsNatUserDataImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ListElementInfoMasterImpl extends ListElementInfoImpl
{
    protected ItsNatUserDataImpl userData;
    protected Object auxObject;

    /** Creates a new instance of ListElementInfoMasterImpl */
    public ListElementInfoMasterImpl(int index,Element elem,ElementListFreeMasterImpl list)
    {
        super(index,elem,list);
    }

    public void setIndex(int index)
    {
        // NO hacer público
        this.index = index;
    }

    public void setElement(Element elem)
    {
        // NO hacer público
        this.elem = elem;
    }

    public Object getAuxObject()
    {
        return auxObject;
    }

    public void setAuxObject(Object auxObject)
    {
        this.auxObject = auxObject;
    }

    public ItsNatUserDataImpl getItsNatUserData()
    {
        if (userData == null)
            this.userData = new ItsNatUserDataImpl(false);
        return userData;
    }

    public boolean containsUserValueName(String name)
    {
        return getItsNatUserData().containsUserValueName(name);
    }

    public Object getUserValue(String name)
    {
        return getItsNatUserData().getUserValue(name);
    }

    public Object setUserValue(String name, Object value)
    {
        return getItsNatUserData().setUserValue(name,value);
    }

    public Object removeUserValue(String name)
    {
        return getItsNatUserData().removeUserValue(name);
    }

    public String[] getUserValueNames()
    {
        return getItsNatUserData().getUserValueNames();
    }


}
