package org.itsnat.droid.impl.xmlinflater.drawable.attr;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescDrawable;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescDrawableReflecMethodBoolean extends AttrDescDrawableReflecMethod
{
    protected boolean defaultValue;

    public AttrDescDrawableReflecMethodBoolean(ClassDescDrawable parent, String name, String methodName, boolean defaultValue)
    {
        super(parent,name,methodName,getClassParam());
        this.defaultValue = defaultValue;
    }

    public AttrDescDrawableReflecMethodBoolean(ClassDescDrawable parent, String name, boolean defaultValue)
    {
        super(parent,name,getClassParam());
        this.defaultValue = defaultValue;
    }

    protected static Class<?> getClassParam()
    {
        return boolean.class;
    }

    @Override
    protected void setAttribute(Drawable draw, String value,Context ctx)
    {
        boolean convValue = getBoolean(value,ctx);
        callMethod(draw, convValue);
    }

    @Override
    public void removeAttribute(Drawable draw)
    {
        callMethod(draw, defaultValue);
    }

}
