package org.itsnat.droid;

import android.app.Application;

import org.itsnat.droid.impl.ItsNatDroidImpl;

/**
 * Created by jmarranz on 3/05/14.
 */
public class ItsNatDroidRoot
{
    public static void init(Application app) { ItsNatDroidImpl.init(app); }
    public static ItsNatDroid get()
    {
        return ItsNatDroidImpl.DEFAULT;
    }
}
