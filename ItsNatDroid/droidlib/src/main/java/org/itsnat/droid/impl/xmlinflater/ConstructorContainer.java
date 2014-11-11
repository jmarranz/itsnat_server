package org.itsnat.droid.impl.xmlinflater;

import org.itsnat.droid.ItsNatDroidException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by jmarranz on 23/09/14.
 */
public class ConstructorContainer<T>
{
    protected final Class<?> clasz;
    protected final Class[] paramClasses;
    protected Constructor constructor;

    public ConstructorContainer(Class<?> clasz, Class[] paramClasses)
    {
        this.clasz = clasz;
        this.paramClasses = paramClasses;
    }

    public Class[] getParamClasses()
    {
        return paramClasses;
    }

    public Constructor getConstructor()
    {
        try
        {
            if (constructor == null)
            {
                this.constructor = clasz.getDeclaredConstructor(paramClasses);
                constructor.setAccessible(true); // Pues normalmente serán atributos ocultos
            }
            return constructor;
        }
        catch (NoSuchMethodException ex) { throw new ItsNatDroidException(ex); }
    }

    public T invoke(Object... params)
    {
        try
        {
            Constructor constructor = getConstructor();
            return (T)constructor.newInstance(params);
        }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
        catch (InvocationTargetException ex) { throw new ItsNatDroidException(ex); }
        catch (InstantiationException ex) { throw new ItsNatDroidException(ex); }
    }
}
