package org.itsnat.droid.impl.xmlinflater.attr;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by jmarranz on 23/09/14.
 */
public class MethodContainer<T>
{
    protected final Class<?> clasz;
    protected final String methodName;
    protected final Class[] paramClasses;
    protected Method method;

    public MethodContainer(ClassDescViewBased parent,String methodName,Class[] paramClasses)
    {
        this(parent.getViewClass(),methodName,paramClasses);
    }

    public MethodContainer(Class<?> clasz, String methodName,Class[] paramClasses)
    {
        this.clasz = clasz;
        this.methodName = methodName;
        this.paramClasses = paramClasses;
    }

    public String getMethodName()
    {
        return methodName;
    }

    public Class[] getParamClasses()
    {
        return paramClasses;
    }

    public Method getMethod()
    {
        try
        {
            if (method == null)
            {
                this.method = clasz.getDeclaredMethod(methodName, paramClasses);
                method.setAccessible(true); // Pues normalmente serán atributos ocultos
            }
            return method;
        }
        catch (NoSuchMethodException ex) { throw new ItsNatDroidException(ex); }
    }

    public T invoke(Object obj, Object... params)
    {
        try
        {
            Method method = getMethod();
            return (T)method.invoke(obj, params);
        }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
        catch (InvocationTargetException ex) { throw new ItsNatDroidException(ex); }
    }
}
