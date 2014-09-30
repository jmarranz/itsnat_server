package org.itsnat.droid.impl.util;

import org.itsnat.droid.ItsNatDroidException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 15/09/14.
 */
public class MiscUtil
{
    private static final Map<String,Class> mapClasses = new HashMap<String,Class>();

    public static Class resolveClass(String className)
    {
        // El propio Class.forName tiene obviamente un caché de clases ya cargadas (ojo de t_odo el espacio de clases Android),
        // sin embargo con nuestro Map se consiguen
        // mayores velocidades en clases ya cargadas si el map no se llena de muchas clases (menos de 50 por ejemplo)
        // Por supuesto NO utilizar multihilo
        Class clasz = mapClasses.get(className);
        if (clasz != null)
            return clasz;

        try { clasz = Class.forName(className); }
        catch (ClassNotFoundException ex) { throw new ItsNatDroidException(ex); }
        mapClasses.put(className,clasz);
        return clasz;
    }

}
