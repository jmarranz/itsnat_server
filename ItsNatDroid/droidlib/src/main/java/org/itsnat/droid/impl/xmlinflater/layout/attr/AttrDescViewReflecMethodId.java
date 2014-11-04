package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 *
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescViewReflecMethodId extends AttrDescViewReflecMethod
{
    protected Integer defaultValue;

    public AttrDescViewReflecMethodId(ClassDescViewBased parent, String name, String methodName, Integer defaultValue)
    {
        super(parent,name,methodName,getClassParam());
        this.defaultValue = defaultValue;
    }

    public AttrDescViewReflecMethodId(ClassDescViewBased parent, String name, Integer defaultValue)
    {
        super(parent,name,getClassParam());
        this.defaultValue = defaultValue;
    }

    protected static Class<?> getClassParam()
    {
        return int.class;
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int id = getIdentifierAddIfNecessary(value, view.getContext());

        callMethod(view, id);
    }

    public void removeAttribute(View view)
    {
        if (defaultValue != null)
            callMethod(view, defaultValue);
    }

}
