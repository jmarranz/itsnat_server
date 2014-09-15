package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.lang.reflect.Field;

/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class AttrDescReflecFieldGet extends AttrDesc
{
    protected Field field;
    protected String fieldName;

    public AttrDescReflecFieldGet(ClassDescViewBased parent, String name, String fieldName)
    {
        super(parent,name);
        this.fieldName = fieldName;
    }

    protected Object getField(View view)
    {
        try
        {
            if (field == null)
            {
                this.field = parent.getViewClass().getDeclaredField(fieldName);
                field.setAccessible(true); // Pues normalmente serán atributos ocultos
            }

            return field.get(view);
        }
        catch (NoSuchFieldException ex) { throw new ItsNatDroidException(ex); }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
    }
}
