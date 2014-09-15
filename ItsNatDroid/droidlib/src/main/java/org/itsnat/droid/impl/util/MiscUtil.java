package org.itsnat.droid.impl.util;

import org.itsnat.droid.ItsNatDroidException;

/**
 * Created by jmarranz on 15/09/14.
 */
public class MiscUtil
{
    public static Class resolveClass(String viewName)
    {
        try { return Class.forName(viewName); }
        catch (ClassNotFoundException ex) { throw new ItsNatDroidException(ex); }
    }
}
