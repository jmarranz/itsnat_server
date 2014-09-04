package org.itsnat.droid.impl.xmlinflater.attr;

import android.graphics.drawable.Drawable;
import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingAttrTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 1/05/14.
 */
public class AttrDescReflecDrawable extends AttrDescReflection
{
    public AttrDescReflecDrawable(ClassDescViewBased parent, String name, String methodName)
    {
        super(parent,name,methodName);
    }

    public AttrDescReflecDrawable(ClassDescViewBased parent, String name)
    {
        super(parent, name);
    }

    protected Class<?> getClassParam()
    {
        return Drawable.class;
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingAttrTasks pending)
    {
        Drawable convValue = getDrawable(value,view.getContext());
        callMethod(view, convValue);
    }

    public void removeAttribute(View view)
    {
        callMethod(view, null);
    }
}
