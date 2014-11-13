package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import org.itsnat.droid.impl.model.AttrParsed;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 1/05/14.
 */
public class AttrDescViewReflecMethodDrawable extends AttrDescViewReflecMethod
{
    protected String defaultValue;

    public AttrDescViewReflecMethodDrawable(ClassDescViewBased parent, String name, String methodName, String defaultValue)
    {
        super(parent,name,methodName,getClassParam());
        this.defaultValue = defaultValue;
    }

    public AttrDescViewReflecMethodDrawable(ClassDescViewBased parent, String name, String defaultValue)
    {
        super(parent, name,getClassParam());
        this.defaultValue = defaultValue;
    }

    protected static Class<?> getClassParam()
    {
        return Drawable.class;
    }

    public void setAttribute(final View view,final AttrParsed attr,final XMLInflaterLayout xmlInflaterLayout,final Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        Runnable task = new Runnable()
        {
            @Override
            public void run()
            {
                Drawable convValue = getDrawable(attr,ctx,xmlInflaterLayout);
                callMethod(view, convValue);
            }
        };
        processDrawableTask(attr,task,xmlInflaterLayout);
    }

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        if (defaultValue != null) // Para especificar null se ha de usar "@null"
            setAttribute(view,defaultValue,xmlInflaterLayout,ctx,null,null); // defaultValue puede ser null (ej attr background), también valdría "@null" en el atributo
    }
}
