package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.View;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.util.MiscUtil;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethod;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_ImageView_tint extends AttrDescViewReflecMethod
{
    protected String defaultValue;

    public AttrDescView_widget_ImageView_tint(ClassDescViewBased parent)
    {
        super(parent,"tint",getMethodName(),getClassParam());
        this.defaultValue = getDefaultValue();
    }

    protected static String getMethodName()
    {
        // A partir de Lollipop (level 21) ya no se usa setColorFilter, se usa setImageTintList, en teoría el resultado
        // visual debería ser el mismo pero a nivel de estado interno no, lo cual nos entorpece el testing
        return (Build.VERSION.SDK_INT < MiscUtil.LOLLIPOP) ? "setColorFilter" : "setImageTintList";
    }

    protected static Class<?> getClassParam()
    {
        return (Build.VERSION.SDK_INT < MiscUtil.LOLLIPOP) ? int.class : ColorStateList.class;
    }

    protected static String getDefaultValue()
    {
        return "#000000";
    }

    public void setAttribute(View view, DOMAttr attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        final int convValue = getColor(attr.getValue(), ctx);
        if (Build.VERSION.SDK_INT < MiscUtil.LOLLIPOP)
        {
            callMethod(view, convValue);
        }
        else
        {
            callMethod(view,new ColorStateList(new int[1][0],new int[]{convValue}));
        }
    }

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        if (defaultValue != null)
            setAttribute(view,defaultValue, xmlInflaterLayout,ctx,null,null);
    }
}
