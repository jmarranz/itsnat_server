package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 1/05/14.
 */
public class AttrDescReflecGravity extends AttrDescReflection
{
    public AttrDescReflecGravity(ClassDescViewBased parent, String name, String methodName)
    {
        super(parent,name,methodName);
    }

    public AttrDescReflecGravity(ClassDescViewBased parent, String name)
    {
        super(parent, name);
    }

    protected Class<?> getClassParam()
    {
        return int.class;
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess)
    {
        int valueInt = parseMultipleName(value, AttrDescGravityUtil.valueMap);
        callMethod(view, valueInt);
    }

    public void removeAttribute(View view)
    {
        callMethod(view, 0);
    }
}
