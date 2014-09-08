package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;

import org.itsnat.droid.ItsNatDroidException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by jmarranz on 7/09/14.
 */
public abstract class GridLayout_columnAndRowSpec
{
    public static Method method_getAlignment;

    public int layout_gravity = Gravity.NO_GRAVITY;

    public GridLayout_columnAndRowSpec() {}

    public void setAttributes(View view)
    {
        GridLayout viewParent = (GridLayout)view.getParent();
        GridLayout.Spec spec = GridLayout.spec(getStart(), getSpan(), getAlignment(viewParent, layout_gravity, isHorizontal()));

        GridLayout.LayoutParams params = (GridLayout.LayoutParams)view.getLayoutParams();
        setSpec(params,spec);
    }

    public abstract void setSpec(GridLayout.LayoutParams params,GridLayout.Spec spec);
    public abstract boolean isHorizontal();
    public abstract int getStart();
    public abstract int getSpan();

    public static GridLayout.Alignment getAlignment(GridLayout view,int gravity,boolean horizontal)
    {
        try
        {
            if (method_getAlignment == null)
            {
                method_getAlignment = GridLayout.class.getDeclaredMethod("getAlignment", new Class[]{int.class, boolean.class});
                method_getAlignment.setAccessible(true); // No es público
            }

            return (GridLayout.Alignment)method_getAlignment.invoke(view,gravity,horizontal);
        }
        catch (NoSuchMethodException ex)
        {
            throw new ItsNatDroidException(ex);
        }
        catch (IllegalAccessException ex)
        {
            throw new ItsNatDroidException(ex);
        }
        catch (InvocationTargetException ex)
        {
            throw new ItsNatDroidException(ex);
        }
    }
}
