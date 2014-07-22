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
