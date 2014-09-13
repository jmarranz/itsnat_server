package org.itsnat.droid.impl.xmlinflater.attr;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ListPopupWindow;
import android.widget.Spinner;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_Spinner_popupBackground extends AttrDesc
{
    protected Field field;
    protected Method method;

    public AttrDesc_widget_Spinner_popupBackground(ClassDescViewBased parent)
    {
        super(parent,"popupBackground");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        Drawable convertedValue = getDrawable(value, view.getContext());

        callMethodField(view,Spinner.class,ListPopupWindow.class,Drawable.class,"mPopup","setBackgroundDrawable",convertedValue);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"wrap_content",null,null);
    }


    protected void callMethodField(View view,Class fieldClass,Class methodClass,Class paramClass,
                                   String fieldName,String methodName,Object convertedValue)
    {
        try
        {
            if (field == null)
            {
                this.field = fieldClass.getDeclaredField(fieldName);
                field.setAccessible(true); // Pues normalmente serán atributos ocultos
            }
            Object fieldValue = field.get(view);

            if (method == null)
            {
                this.method = methodClass.getDeclaredMethod(methodName, paramClass);
                method.setAccessible(true); // Pues normalmente serán atributos ocultos
            }
            method.invoke(fieldValue,convertedValue);
        }
        catch (NoSuchFieldException ex) { throw new ItsNatDroidException(ex); }
        catch (NoSuchMethodException ex) { throw new ItsNatDroidException(ex); }
        catch (InvocationTargetException ex) { throw new ItsNatDroidException(ex); }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
    }
}
