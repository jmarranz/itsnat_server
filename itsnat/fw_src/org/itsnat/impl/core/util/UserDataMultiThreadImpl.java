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

/**
 *
 * @author jmarranz
 */
public class UserDataMultiThreadImpl implements UserData,Serializable
{
    protected UserDataMonoThreadImpl userData;

    /** Creates a new instance of UserDataMultiThreadImpl */
    public UserDataMultiThreadImpl()
    {
    }

    private UserDataMonoThreadImpl getInternalUserData()
    {
        // Es llamado siempre por métodos sincronizados, no hay problema de hilos
        if (userData == null)
            this.userData = new UserDataMonoThreadImpl(); // Para ahorrar memoria si no se usa
        return userData;
    }

    public synchronized String[] getUserDataNames()
    {
        UserDataMonoThreadImpl userData = getInternalUserData();
        return userData.getUserDataNames();
    }

    public synchronized boolean containsName(String name)
    {
        UserDataMonoThreadImpl userData = getInternalUserData();
        return userData.containsName(name);
    }

    public synchronized Object getUserData(String name)
    {
        UserDataMonoThreadImpl userData = getInternalUserData();
        return userData.getUserData(name);
    }

    public synchronized Object setUserData(String name, Object value)
    {
        UserDataMonoThreadImpl userData = getInternalUserData();
        return userData.setUserData(name,value);
    }

    public synchronized Object removeUserData(String name)
    {
        UserDataMonoThreadImpl userData = getInternalUserData();
        return userData.removeUserData(name);
    }

}
