/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    
    public int getRawX()
    {
        return getParameterInt("rawX");
    }

    public int getRawY()
    {
        return getParameterInt("rawY");
    }

    public int getX()
    {
        return getParameterInt("getX");
    }

    public int getY()
    {
        return getParameterInt("getY");
    }  

    public void setRawX(int value)
    {
       throw new ItsNatException("Not implemented",this);
    }

    public void setRawY(int value)
    {
       throw new ItsNatException("Not implemented",this);
    }

    public void setX(int value)
    {
       throw new ItsNatException("Not implemented",this);
    }

    public void setY(int value)
    {
       throw new ItsNatException("Not implemented",this);
    }

    
}
