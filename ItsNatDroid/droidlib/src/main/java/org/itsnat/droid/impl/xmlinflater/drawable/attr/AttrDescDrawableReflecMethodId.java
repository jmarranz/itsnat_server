package org.itsnat.droid.impl.xmlinflater.drawable.attr;

import android.content.Context;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescDrawable;

/**
 *
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescDrawableReflecMethodId<Tdrawable> extends AttrDescDrawableReflecMethod<Tdrawable>
{
    public AttrDescDrawableReflecMethodId(ClassDescDrawable parent, String name, String methodName)
    {
        super(parent,name,methodName,getClassParam());
    }

    public AttrDescDrawableReflecMethodId(ClassDescDrawable parent, String name)
    {
        super(parent,name,getClassParam());
    }

    protected static Class<?> getClassParam()
    {
        return int.class;
    }

    @Override
    public void setAttribute(final Tdrawable draw,final DOMAttr attr,final XMLInflaterDrawable xmlInflaterDrawable,final Context ctx)
    {
        int id = getIdentifierAddIfNecessary(attr.getValue(),ctx);

        callMethod(draw, id);
    }

}
