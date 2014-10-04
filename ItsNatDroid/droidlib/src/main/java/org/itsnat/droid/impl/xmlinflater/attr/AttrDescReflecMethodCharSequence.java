package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescReflecMethodCharSequence extends AttrDescReflecMethod
{
    protected String defaultValue;

    public AttrDescReflecMethodCharSequence(ClassDescViewBased parent, String name, String methodName,String defaultValue)
    {
        super(parent,name,methodName,getClassParam());
        this.defaultValue = defaultValue;
    }

    public AttrDescReflecMethodCharSequence(ClassDescViewBased parent, String name,String defaultValue)
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
