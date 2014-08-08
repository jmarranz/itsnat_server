package org.itsnat.droid.impl.xmlinflater.attr;

import android.graphics.drawable.Drawable;
import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 1/05/14.
 */
public class AttrDescReflecColor extends AttrDescReflection
{
    public AttrDescReflecColor(ClassDescViewBased parent, String name, String methodName)
    {
        super(parent,name,methodName);
    }

    public AttrDescReflecColor(ClassDescViewBased parent, String name)
    {
        super(parent, name);
    }

    protected Class<?> getClassParam()
    {
        return int.class;
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess)
    {
        int convValue = getColor(value,view.getContext());
        setAttribute(view,convValue);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"#000000");
    }
}
