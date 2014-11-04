package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.layout.attr.FieldContainer;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

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

    protected FieldContainer fieldDrawables;
    protected FieldContainer<Drawable>[] fieldMemberDrawables = new FieldContainer[4];

    public AttrDesc_widget_TextView_compoundDrawables(ClassDescViewBased parent, String name)
    {
        super(parent,name);
        this.fieldDrawables = new FieldContainer(parent,"mDrawables");

        String[] fieldMemberNames = new String[] { "mDrawableLeft","mDrawableTop","mDrawableRight","mDrawableBottom" };
        this.fieldMemberDrawables = new FieldContainer[fieldMemberNames.length];
        Class clasz = fieldDrawables.getField().getType();
        for(int i = 0; i < fieldMemberNames.length; i++)
        {
            fieldMemberDrawables[i] = new FieldContainer(clasz,fieldMemberNames[i]);
        }
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
        Object fieldValue = fieldDrawables.get(view);
        if (fieldValue == null)
            return null; // Esto es normal, y es cuando todavía no se ha definido ningún Drawable, setCompoundDrawablesWithIntrinsicBounds lo creará en la primera llamada

        return fieldMemberDrawables[index].get(fieldValue);
    }
}
