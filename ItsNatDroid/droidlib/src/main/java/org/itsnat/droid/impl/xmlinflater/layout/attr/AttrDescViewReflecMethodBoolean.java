package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.content.Context;
import android.view.View;

import org.itsnat.droid.impl.dom.AttrParsed;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescViewReflecMethodBoolean extends AttrDescViewReflecMethod
{
    protected boolean defaultValue;

    public AttrDescViewReflecMethodBoolean(ClassDescViewBased parent, String name, String methodName, boolean defaultValue)
    {
        super(parent,name,methodName,getClassParam());
        this.defaultValue = defaultValue;
    }

    public AttrDescViewReflecMethodBoolean(ClassDescViewBased parent, String name, boolean defaultValue)
    {
        super(parent,name,getClassParam());
        this.defaultValue = defaultValue;
    }

    protected static Class<?> getClassParam()
    {
        return boolean.class;
    }

    public void setAttribute(View view, AttrParsed attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        boolean convValue = getBoolean(attr.getValue(),ctx);
        callMethod(view, convValue);
    }

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        callMethod(view, defaultValue);
    }
}
