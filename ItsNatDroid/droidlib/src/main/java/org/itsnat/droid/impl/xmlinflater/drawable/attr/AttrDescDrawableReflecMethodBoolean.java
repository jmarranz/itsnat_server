package org.itsnat.droid.impl.xmlinflater.drawable.attr;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.itsnat.droid.impl.model.AttrParsed;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;
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
    public void setAttribute(Drawable draw, AttrParsed attr,XMLInflaterDrawable xmlInflaterDrawable,Context ctx)
    {
        boolean convValue = getBoolean(attr.getValue(),ctx);
        callMethod(draw, convValue);
    }

    @Override
    public void removeAttribute(Drawable draw,XMLInflaterDrawable xmlInflaterDrawable, Context ctx)
    {
        callMethod(draw, defaultValue);
    }

}
