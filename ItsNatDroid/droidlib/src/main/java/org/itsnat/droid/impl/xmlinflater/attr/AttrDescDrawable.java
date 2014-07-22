package org.itsnat.droid.impl.xmlinflater.attr;

import android.graphics.drawable.Drawable;
import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 1/05/14.
 */
public class AttrDescDrawable extends AttrDescReflection
{
    public AttrDescDrawable(ClassDescViewBased parent, String name, String methodName)
    {
        super(parent,name,methodName);
    }

    public AttrDescDrawable(ClassDescViewBased parent, String name)
    {
        super(parent, name);
    }

    protected Class<?> getClassParam()
    {
        return Drawable.class;
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess)
    {
        Drawable convValue = getDrawable(value,view.getContext());
        setAttribute(view,convValue);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,null);
    }
}
