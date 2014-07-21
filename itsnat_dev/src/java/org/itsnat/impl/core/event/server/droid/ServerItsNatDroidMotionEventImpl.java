/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.event.server.droid;

import org.itsnat.core.event.droid.MotionEvent;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;

/**
 *
 * @author jmarranz
 */
public class ServerItsNatDroidMotionEventImpl extends ServerItsNatDroidEventImpl implements MotionEvent
{
    protected int rawX;
    protected int rawY;    
    protected int x;    
    protected int y;    
    
    public ServerItsNatDroidMotionEventImpl(ItsNatStfulDocumentImpl itsNatDoc)
    {
        super(itsNatDoc);
    }

    public int getRawX()
    {
        return rawX;
    }

    public void setRawX(int value)
    {
        this.rawX = value;
    }

    public int getRawY()
    {
        return rawY;
    }

    public void setRawY(int value)
    {
        this.rawY = value;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int value)
    {
        this.x = value;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int value)
    {
        this.y = value;
    }
    
}
