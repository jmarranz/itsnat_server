package org.itsnat.droid;

import org.itsnat.droid.impl.ItsNatDroidImpl;

/**
 * Created by jmarranz on 3/05/14.
 */
public class ItsNatDroidRoot
{
    public static ItsNatDroid get()
    {
        return ItsNatDroidImpl.DEFAULT;
    }
}
