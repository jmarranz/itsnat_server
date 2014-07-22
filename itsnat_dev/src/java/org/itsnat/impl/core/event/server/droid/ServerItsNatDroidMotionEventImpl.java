/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.event.server.droid;

import org.itsnat.core.event.droid.DroidMotionEvent;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;

/**
 *
 * @author jmarranz
 */
public class ServerItsNatDroidMotionEventImpl extends ServerItsNatDroidEventImpl implements DroidMotionEvent
{
    protected float rawX;
    protected float rawY;    
    protected float x;    
    protected float y;    
    
    public ServerItsNatDroidMotionEventImpl(ItsNatStfulDocumentImpl itsNatDoc)
    {
        super(itsNatDoc);
    }

    public float getRawX()
    {
        return rawX;
    }

    public void setRawX(float value)
    {
        this.rawX = value;
    }

    public float getRawY()
    {
        return rawY;
    }

    public void setRawY(float value)
    {
        this.rawY = value;
    }

    public float getX()
    {
        return x;
    }

    public void setX(float value)
    {
        this.x = value;
    }

    public float getY()
    {
        return y;
    }

    public void setY(float value)
    {
        this.y = value;
    }
    
}
