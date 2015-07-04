/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2014 Jose Maria Arranz Santamaria, Spanish citizen

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

package org.itsnat.impl.core.event.client.droid;

import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.droid.DroidMotionEvent;
import org.itsnat.impl.core.listener.droid.ItsNatDroidEventListenerWrapperImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;

/**
 *
 * @author jmarranz
 */
public class ClientItsNatDroidMotionEventImpl extends ClientItsNatDroidInputEventImpl implements DroidMotionEvent
{

    public ClientItsNatDroidMotionEventImpl(ItsNatDroidEventListenerWrapperImpl listenerWrapper, RequestNormalEventImpl request)
    {
        super(listenerWrapper, request);
    }
    
    public float getRawX()
    {
        return getParameterFloat("rawX");
    }

    public float getRawY()
    {
        return getParameterFloat("rawY");
    }

    public float getX()
    {
        return getParameterFloat("x");
    }

    public float getY()
    {
        return getParameterFloat("y");
    }  

    public void setRawX(float value)
    {
       throw new ItsNatException("Not implemented",this);
    }

    public void setRawY(float value)
    {
       throw new ItsNatException("Not implemented",this);
    }

    public void setX(float value)
    {
       throw new ItsNatException("Not implemented",this);
    }

    public void setY(float value)
    {
       throw new ItsNatException("Not implemented",this);
    }

    
}
