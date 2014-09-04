package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingAttrTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescReflecBoolean extends AttrDescReflection
{
    protected boolean defaultValue;

    public AttrDescReflecBoolean(ClassDescViewBased parent, String name, String methodName,boolean defaultValue)
    {
        super(parent,name,methodName);
        this.defaultValue = defaultValue;
    }

    public AttrDescReflecBoolean(ClassDescViewBased parent, String name,boolean defaultValue)
    {
        super(parent,name);
        this.defaultValue = defaultValue;
    }

    protected Class<?> getClassParam()
    {
        return boolean.class;
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingAttrTasks pending)
    {
        boolean convValue = getBoolean(value, view.getContext());
        callMethod(view, convValue);
    }

    public void removeAttribute(View view)
    {
        callMethod(view, defaultValue);
    }
}
