package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class AttrDescReflection extends AttrDesc
{
    protected String methodName;
    protected Method method;

    public AttrDescReflection(ClassDescViewBased parent, String name, String methodName)
    {
        super(parent,name);
        this.methodName = methodName;
    }

    public AttrDescReflection(ClassDescViewBased parent, String name)
    {
        this(parent,name,genMethodName(name));
    }

    public static String genMethodName(String name)
    {
        return "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    protected abstract Class<?> getClassParam();

    protected void setAttribute(View view, Object convertedValue)
    {
        try
        {
            if (method == null)
                this.method = parent.getViewClass().getMethod(methodName,getClassParam());

            method.invoke(view,convertedValue);
        }
        catch (NoSuchMethodException ex) { throw new ItsNatDroidException(ex); }
        catch (InvocationTargetException ex) { throw new ItsNatDroidException(ex); }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
    }

}
