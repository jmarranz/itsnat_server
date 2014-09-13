package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.lang.reflect.Field;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_Spinner_dropDownWidth extends AttrDesc
{
    protected Field field;
    protected String fieldName = "mDropDownWidth";

    public AttrDesc_widget_Spinner_dropDownWidth(ClassDescViewBased parent)
    {
        super(parent,"dropDownWidth");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int convertedValue = getDimensionWithName(view, value);

        setField(view,convertedValue);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"wrap_content",null,null);
    }


    protected void setField(View view,Object convertedValue)
    {
        try
        {
            if (field == null)
            {
                this.field = parent.getViewClass().getDeclaredField(fieldName);
                field.setAccessible(true); // Pues normalmente serán atributos ocultos
            }

            field.set(view,convertedValue);
        }
        catch (NoSuchFieldException ex) { throw new ItsNatDroidException(ex); }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
    }
}
