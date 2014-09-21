package org.itsnat.droid.impl.util;

import org.itsnat.droid.ItsNatDroidException;

/**
 * Created by jmarranz on 15/09/14.
 */
public class MiscUtil
{
    public static Class resolveClass(String className)
    {
        try { return Class.forName(className); } // Es absurdo hacer una caché, el Class ya tiene una caché
        catch (ClassNotFoundException ex) { throw new ItsNatDroidException(ex); }
    }
}
