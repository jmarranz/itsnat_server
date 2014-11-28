package org.itsnat.droid.impl.xmlinflater.drawable.attr;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.dom.DOMAttrRemote;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescDrawable;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescDrawableReflecMethodDrawable<Tdrawable> extends AttrDescDrawableReflecMethod<Tdrawable>
{
    public AttrDescDrawableReflecMethodDrawable(ClassDescDrawable parent, String name, String methodName)
    {
        super(parent,name,methodName,getClassParam());
    }

    public AttrDescDrawableReflecMethodDrawable(ClassDescDrawable parent, String name)
    {
        super(parent,name,getClassParam());
    }

    protected static Class<?> getClassParam()
    {
        return Drawable.class;
    }

    @Override
    public void setAttribute(final Tdrawable draw,final DOMAttr attr,final XMLInflaterDrawable xmlInflaterDrawable,final Context ctx)
    {
        Runnable task = new Runnable()
        {
            @Override
            public void run()
            {
                Drawable convValue = getDrawable(attr,ctx,xmlInflaterDrawable);
                callMethod(draw, convValue);
            }
        };
        if (DOMAttrRemote.isPendingToDownload(attr))
            processDownloadTask((DOMAttrRemote)attr,task,xmlInflaterDrawable);
        else
            task.run();
    }

}
