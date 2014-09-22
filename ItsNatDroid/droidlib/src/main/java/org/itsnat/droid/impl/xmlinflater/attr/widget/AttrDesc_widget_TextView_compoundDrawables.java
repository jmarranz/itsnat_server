package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_TextView_compoundDrawables extends AttrDesc
{
    private static final int LEFT   = 0;
    private static final int TOP    = 1;
    private static final int RIGHT  = 2;
    private static final int BOTTOM = 3;

    private static Map<String,Integer> drawableMap = new HashMap<String,Integer>( 4  );
    static
    {
        drawableMap.put("drawableLeft",LEFT);
        drawableMap.put("drawableTop",TOP);
        drawableMap.put("drawableRight",RIGHT);
        drawableMap.put("drawableBottom",BOTTOM);
    }

    protected Field fieldDrawables;
    protected Field[] fieldMemberDrawables = new Field[4];
    protected String[] fieldMemberNames = new String[] { "mDrawableLeft","mDrawableTop","mDrawableRight","mDrawableBottom" };

    public AttrDesc_widget_TextView_compoundDrawables(ClassDescViewBased parent, String name)
    {
        super(parent,name);
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        Drawable convValue = getDrawable(value, view.getContext());

        int index = drawableMap.get(name);

        Drawable drawableLeft   = index == LEFT ? convValue : getDrawable(view,LEFT);
        Drawable drawableTop    = index == TOP ? convValue : getDrawable(view,TOP);
        Drawable drawableRight  = index == RIGHT ? convValue : getDrawable(view,RIGHT);
        Drawable drawableBottom = index == BOTTOM ? convValue : getDrawable(view,BOTTOM);

        ((TextView)view).setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"@null",null,null);
    }

    protected Drawable getDrawable(View view,int index)
    {
        try
        {
            if (fieldDrawables == null)
            {
                this.fieldDrawables = parent.getViewClass().getDeclaredField("mDrawables");
                fieldDrawables.setAccessible(true); // Pues normalmente serán atributos ocultos
            }
            Object fieldValue = fieldDrawables.get(view);
            if (fieldValue == null)
                return null; // Esto es normal, y es cuando todavía no se ha definido ningún Drawable, setCompoundDrawablesWithIntrinsicBounds lo creará en la primera llamada

            Field fieldMember = fieldMemberDrawables[index];
            if (fieldMember == null)
            {
                fieldMember = fieldDrawables.getType().getDeclaredField(fieldMemberNames[index]);
                fieldMember.setAccessible(true); // Pues normalmente serán atributos ocultos
                fieldMemberDrawables[index] = fieldMember;
            }
            return (Drawable)fieldMember.get(fieldValue);
        }
        catch (NoSuchFieldException ex) { throw new ItsNatDroidException(ex); }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
    }
}
