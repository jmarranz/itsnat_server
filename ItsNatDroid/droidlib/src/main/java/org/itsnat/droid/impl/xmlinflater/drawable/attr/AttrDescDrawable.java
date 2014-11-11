package org.itsnat.droid.impl.xmlinflater.drawable.attr;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.itsnat.droid.impl.model.AttrParsed;
import org.itsnat.droid.impl.xmlinflater.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescDrawable;

/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class AttrDescDrawable extends AttrDesc
{
    public AttrDescDrawable(ClassDescDrawable parent, String name)
    {
        super(parent,name);
    }

    public void setAttribute(Drawable obj, AttrParsed attr,Context ctx)
    {
        setAttribute(obj,attr.getValue(),ctx);
    }

    protected abstract void setAttribute(Drawable draw, String value,Context ctx);

    public abstract void removeAttribute(Drawable draw);
}


