/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.core.event.droid;

/**
 *
 * @author jmarranz
 */
public interface DroidMotionEvent extends DroidInputEvent
{
    public float getRawX();
    public void setRawX(float value);    
    public float getRawY();
    public void setRawY(float value);    
    public float getX();
    public void setX(float value);    
    public float getY();
    public void setY(float value); 
    
}
