/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.comp.android.widget;

/**
 *
 * @author jmarranz
 */
public interface Checkable
{
    /**
     * @TODO
     * @return 
     */
    public boolean isChecked();

    /**
     * @TODO
     * @param checked 
     */
    public void setChecked(boolean checked);    
    
    /**
     * @TODO
     */
    public void toggle();
}
