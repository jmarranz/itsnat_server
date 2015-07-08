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

package org.itsnat.impl.core.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jmarranz
 */
public class UserDataMonoThreadImpl implements UserData,Serializable
{
    protected Map<String,Object> userData;

    /**
     * Creates a new instance of UserDataMonoThreadImpl
     */
    public UserDataMonoThreadImpl()
    {
    }

    public Map<String,Object> getInternalMap()
    {
        if (userData == null) userData = new HashMap<String,Object>();
        return userData;
    }

    public String[] getUserDataNames()
    {
        Map<String,Object> userData = getInternalMap();
        String[] names = new String[userData.size()];
        return userData.keySet().toArray(names);
    }

    public boolean containsName(String name)
    {
        Map<String,Object> userData = getInternalMap();
        return userData.containsKey(name);
    }

    public Object getUserData(String name)
    {
        Map<String,Object> userData = getInternalMap();
        return userData.get(name);
    }

    public Object setUserData(String name,Object value)
    {
        Map<String,Object> userData = getInternalMap();
        return userData.put(name,value);
    }

    public Object removeUserData(String name)
    {
        Map<String,Object> userData = getInternalMap();
        return userData.remove(name);
    }

}
