package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class AttrDescReflecFieldMethod extends AttrDesc
{
    protected String methodName;
    protected Class methodClass;
    protected Class paramClass;
    protected FieldContainer field;
    protected Method method;

    public AttrDescReflecFieldMethod(ClassDescViewBased parent, String name, String fieldName, String methodName, Class methodClass, Class paramClass)
    {
        super(parent,name);
        this.field = new FieldContainer(parent,fieldName);
        this.methodName = methodName;
        this.methodClass = methodClass;
        this.paramClass = paramClass;
    }

    protected void callFieldMethod(View view, Object convertedValue)
    {
        try
        {
            Object fieldValue = field.getValue(view);

            if (method == null)
            {
                this.method = methodClass.getDeclaredMethod(methodName, paramClass);
                method.setAccessible(true); // Pues normalmente serán atributos ocultos
            }
            method.invoke(fieldValue,convertedValue);
        }
        catch (NoSuchMethodException ex) { throw new ItsNatDroidException(ex); }
        catch (InvocationTargetException ex) { throw new ItsNatDroidException(ex); }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
    }



}
