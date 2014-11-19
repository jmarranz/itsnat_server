package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import org.itsnat.droid.impl.dom.AttrParsed;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecFieldFieldMethod;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class AttrDescViewReflecFieldFieldMethodDrawable extends AttrDescViewReflecFieldFieldMethod
{
    public AttrDescViewReflecFieldFieldMethodDrawable(ClassDescViewBased parent, String name, String fieldName1, String fieldName2, String methodName, Class field2Class, Class methodClass, Class paramClass)
    {
        super(parent,name,fieldName1,fieldName2,methodName,field2Class,methodClass,paramClass);
    }

    public void setAttribute(final View view,final AttrParsed attr,final XMLInflaterLayout xmlInflaterLayout,final Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        Runnable task = new Runnable()
        {
            @Override
            public void run()
            {
                Drawable convValue = getDrawable(attr,ctx,xmlInflaterLayout);
                callFieldFieldMethod(view, convValue);
            }
        };
        processDrawableTask(attr,task,xmlInflaterLayout);
    }

}
