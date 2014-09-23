package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by jmarranz on 23/09/14.
 */
public class MethodContainer<T>
{
    protected final ClassDescViewBased parent;
    protected final String methodName;
    protected final Class[] paramClasses;
    protected Method method;

    public MethodContainer(ClassDescViewBased parent, String methodName,Class[] paramClasses)
    {
        this.parent = parent;
        this.methodName = methodName;
        this.paramClasses = paramClasses;
if (paramClasses == null)
{
    System.out.println("PARAR");
}
    }

    public Method getMethod()
    {
        try
        {
            if (method == null)
            {
                this.method = parent.getViewClass().getDeclaredMethod(methodName, paramClasses);
                method.setAccessible(true); // Pues normalmente serán atributos ocultos
            }
            return method;
        }
        catch (NoSuchMethodException ex) { throw new ItsNatDroidException(ex); }
    }

    public T call(View view,Object...params)
    {
        try
        {
            Method method = getMethod();
            return (T)method.invoke(view, params);
        }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
        catch (InvocationTargetException ex) { throw new ItsNatDroidException(ex); }
    }
}
