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

package org.itsnat.impl.core;

import java.io.Serializable;
import org.itsnat.core.ItsNatUserData;
import org.itsnat.impl.core.util.UserData;
import org.itsnat.impl.core.util.UserDataMonoThreadImpl;
import org.itsnat.impl.core.util.UserDataMultiThreadImpl;

/**
 *
 * @author jmarranz
 */
public class ItsNatUserDataImpl implements ItsNatUserData,Serializable
{
    protected UserData userData;

    /**
     * Creates a new instance of ItsNatObjectNoSyncImpl
     */
    public ItsNatUserDataImpl(boolean sync)
    {
        if (sync)
            this.userData = new UserDataMultiThreadImpl();
        else
            this.userData = null; // Se crea cuando se necesite.
    }

    public UserData getUserData()
    {
        if (userData == null)
            this.userData = new UserDataMonoThreadImpl(); // Para ahorrar memoria si no se usa, no está sincronizada
        return userData;
    }

    public boolean containsUserValueName(String name)
    {
        return getUserData().containsName(name);
    }

    public String[] getUserValueNames()
    {
        return getUserData().getUserDataNames();
    }

    public Object getUserValue(String name)
    {
        return getUserData().getUserData(name);
    }

    public Object setUserValue(String name,Object value)
    {
        return getUserData().setUserData(name,value);
    }

    public Object removeUserValue(String name)
    {
        return getUserData().removeUserData(name);
    }

}
