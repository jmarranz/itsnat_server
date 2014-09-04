package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingAttrTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 1/05/14.
 */
public class AttrDescReflecColor extends AttrDescReflection
{
    protected String defaultValue;

    public AttrDescReflecColor(ClassDescViewBased parent, String name, String methodName,String defaultValue)
    {
        super(parent,name,methodName);
        this.defaultValue = defaultValue;
    }

    public AttrDescReflecColor(ClassDescViewBased parent, String name,String defaultValue)
    {
        super(parent, name);
        this.defaultValue = defaultValue;
    }

    protected Class<?> getClassParam()
    {
        return int.class;
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingAttrTasks pending)
    {
        int convValue = getColor(value,view.getContext());
        callMethod(view, convValue);
    }

    public void removeAttribute(View view)
    {
        if (defaultValue != null)
            callMethod(view, defaultValue);
    }
}
