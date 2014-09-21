package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.graphics.drawable.Drawable;
import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by jmarranz on 17/09/14.
 */
public class AttrDesc_widget_ProgressBar_progressDrawable extends AttrDescReflecMethodDrawable
{
    protected Method methodTileify;

    public AttrDesc_widget_ProgressBar_progressDrawable(ClassDescViewBased parent)
    {
        super(parent,"progressDrawable",null); // Valor por default: Android tiene un Drawable por defecto
    }

    protected void callMethod(View view, Object convertedValue)
    {
        Drawable drawable = (Drawable)convertedValue;

        // En el código fuente del constructor se ve que hay un proceso previo a llamar setProgressDrawable()

        try
        {
            if (methodTileify == null)
            {
                this.methodTileify = parent.getViewClass().getDeclaredMethod("tileify", new Class[]{Drawable.class, boolean.class});
                methodTileify.setAccessible(true);
            }

            drawable = (Drawable)methodTileify.invoke(view,convertedValue,false);
        }
        catch (NoSuchMethodException ex) { throw new ItsNatDroidException(ex); }
        catch (InvocationTargetException ex) { throw new ItsNatDroidException(ex); }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }

        super.callMethod(view,drawable);
    }

}
