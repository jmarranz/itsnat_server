package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class AttrDescReflecFieldFieldMethod extends AttrDesc
{
    protected String fieldName1;
    protected String fieldName2;
    protected String methodName;
    protected Class field2Class;
    protected Class methodClass;
    protected Class paramClass;
    protected Field field1;
    protected Field field2;
    protected Method method;

    public AttrDescReflecFieldFieldMethod(ClassDescViewBased parent, String name, String fieldName1, String fieldName2, String methodName, Class field2Class, Class methodClass, Class paramClass)
    {
        super(parent,name);
        this.fieldName1 = fieldName1;
        this.fieldName2 = fieldName2;
        this.methodName = methodName;
        this.field2Class = field2Class;
        this.methodClass = methodClass;
        this.paramClass = paramClass;
    }

    protected void callFieldFieldMethod(View view,Object convertedValue)
    {
        try
        {
            if (field1 == null)
            {
                this.field1 = parent.getViewClass().getDeclaredField(fieldName1);
                field1.setAccessible(true); // Pues normalmente serán atributos ocultos
            }
            Object fieldValue1 = field1.get(view);

            if (field2 == null)
            {
                this.field2 = field2Class.getDeclaredField(fieldName2);
                field2.setAccessible(true); // Pues normalmente serán atributos ocultos
            }
            Object fieldValue2 = field2.get(fieldValue1);

            if (method == null)
            {
                this.method = methodClass.getDeclaredMethod(methodName, paramClass);
                method.setAccessible(true); // Pues normalmente serán atributos ocultos
            }
            method.invoke(fieldValue2,convertedValue);
        }
        catch (NoSuchFieldException ex) { throw new ItsNatDroidException(ex); }
        catch (NoSuchMethodException ex) { throw new ItsNatDroidException(ex); }
        catch (InvocationTargetException ex) { throw new ItsNatDroidException(ex); }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
    }



}
