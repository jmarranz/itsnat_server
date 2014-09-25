package org.itsnat.droid.impl.xmlinflater.attr;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.lang.reflect.Field;

/**
 * Created by jmarranz on 23/09/14.
 */
public class FieldContainer<T>
{
    protected final Class<?> clasz;
    protected final String fieldName;
    protected Field field;

    public FieldContainer(ClassDescViewBased parent,String fieldName)
    {
        this(parent.getViewClass(),fieldName);
    }

    public FieldContainer(Class<?> clasz,String fieldName)
    {
        this.clasz = clasz;
        this.fieldName = fieldName;
    }

    public Field getField()
    {
        try
        {
            if (field == null)
            {
                this.field = clasz.getDeclaredField(fieldName);
                field.setAccessible(true); // Pues normalmente serán atributos ocultos
            }
            return field;
        }
        catch (NoSuchFieldException ex) { throw new ItsNatDroidException(ex); }
    }

    public T get(Object obj)
    {
        try
        {
            Field field = getField();
            return (T)field.get(obj);
        }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
    }

    public void set(Object obj, T value)
    {
        try
        {
            Field field = getField();
            field.set(obj, value);
        }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
    }
}
