package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescReflecFloat extends AttrDescReflection
{
    protected float defaultValue;

    public AttrDescReflecFloat(ClassDescViewBased parent, String name, String methodName, float defaultValue)
    {
        super(parent,name,methodName);
        this.defaultValue = defaultValue;
    }

    public AttrDescReflecFloat(ClassDescViewBased parent, String name, float defaultValue)
    {
        super(parent,name);
        this.defaultValue = defaultValue;
    }

    protected Class<?> getClassParam()
    {
        return float.class;
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess)
    {
        float convValue = getFloat(value, view.getContext());
        setAttribute(view,convValue);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,getDefaultValue());
    }

    public float getDefaultValue() { return defaultValue; };
}
