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
public interface MotionEvent extends InputEvent
{
    public int getRawX();
    public void setRawX(int value);    
    public int getRawY();
    public void setRawY(int value);    
    public int getX();
    public void setX(int value);    
    public int getY();
    public void setY(int value); 
    
}
