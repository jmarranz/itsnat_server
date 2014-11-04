package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescViewReflecMethodCharSequence extends AttrDescViewReflecMethod
{
    protected String defaultValue;

    public AttrDescViewReflecMethodCharSequence(ClassDescViewBased parent, String name, String methodName, String defaultValue)
    {
        super(parent,name,methodName,getClassParam());
        this.defaultValue = defaultValue;
    }

    public AttrDescViewReflecMethodCharSequence(ClassDescViewBased parent, String name, String defaultValue)
    {
        super(parent,name,getClassParam());
        this.defaultValue = defaultValue;
    }

    protected static Class<?> getClassParam()
    {
        return CharSequence.class;
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        CharSequence convValue = getText(value, view.getContext());
        callMethod(view, convValue);
    }

    public void removeAttribute(View view)
    {
        if (defaultValue != null)
            callMethod(view,defaultValue);
    }

}
