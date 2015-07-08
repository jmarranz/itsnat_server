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

import org.itsnat.core.ItsNatException;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ListElementInfoSlaveImpl extends ListElementInfoImpl
{

    /** Creates a new instance of ListElementInfoMasterImpl */
    public ListElementInfoSlaveImpl(int index,Element elem,ElementListFreeSlaveImpl list)
    {
        super(index,elem,list);
    }

    public boolean containsUserValueName(String name)
    {
        throw new ItsNatException("As this object must not be saved, user data would be lost",this);
    }

    public Object getUserValue(String name)
    {
        throw new ItsNatException("As this object must not be saved, user data would be lost",this);
    }

    public Object setUserValue(String name, Object value)
    {
        throw new ItsNatException("As this object must not be saved, user data would be lost",this);
    }

    public Object removeUserValue(String name)
    {
        throw new ItsNatException("As this object must not be saved, user data would be lost",this);
    }

    public String[] getUserValueNames()
    {
        throw new ItsNatException("As this object must not be saved, user data would be lost",this);
    }

}
