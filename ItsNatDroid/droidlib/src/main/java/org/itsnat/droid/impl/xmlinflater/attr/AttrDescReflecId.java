package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescReflecId extends AttrDescReflection
{
    public AttrDescReflecId(ClassDescViewBased parent, String name, String methodName)
    {
        super(parent,name,methodName);
    }

    public AttrDescReflecId(ClassDescViewBased parent, String name)
    {
        super(parent,name);
    }

    protected Class<?> getClassParam()
    {
        return int.class;
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess)
    {
        int convValue = getIdentifier(value, view.getContext());
        setAttribute(view,convValue);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,0);
    }

}
