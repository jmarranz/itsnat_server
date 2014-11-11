package org.itsnat.droid.impl.xmlinflater;

import org.itsnat.droid.ItsNatDroidException;

import java.lang.reflect.Field;

/**
 * Created by jmarranz on 23/09/14.
 */
public class FieldContainer<T>
{
    protected final Class<?> clasz;
    protected final String fieldName;
    protected Field field;

    public FieldContainer(ClassDesc parent,String fieldName)
    {
        this(parent.getDeclaredClass(),fieldName);
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
