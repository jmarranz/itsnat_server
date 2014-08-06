package org.itsnat.droid.event;

/**
 * Created by jmarranz on 29/07/14.
 */
public interface DroidTextChangeEvent extends DroidEvent
{
    public CharSequence getNewText();
}
