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
public interface DroidKeyEvent extends DroidInputEvent
{
    public int getKeyCode();
    public void setKeyCode(int keyCode);    
}
