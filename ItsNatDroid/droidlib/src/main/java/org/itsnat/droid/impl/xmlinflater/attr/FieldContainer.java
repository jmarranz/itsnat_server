package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.lang.reflect.Field;

/**
 * Created by jmarranz on 23/09/14.
 */
public class FieldContainer<T>
{
    protected final ClassDescViewBased parent;
    protected final String fieldName;
    protected Field field;

    public FieldContainer(ClassDescViewBased parent,String fieldName)
    {
        this.parent = parent;
        this.fieldName = fieldName;
    }

    public Field getField()
    {
        try
        {
            if (field == null)
            {
                this.field = parent.getViewClass().getDeclaredField(fieldName);
                field.setAccessible(true); // Pues normalmente serán atributos ocultos
            }
            return field;
        }
        catch (NoSuchFieldException ex) { throw new ItsNatDroidException(ex); }
    }

    public T getValue(View view)
    {
        try
        {
            Field field = getField();
            return (T)field.get(view);
        }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
    }

    public void setValue(View view,T value)
    {
        try
        {
            Field field = getField();
            field.set(view, value);
        }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
    }
}
