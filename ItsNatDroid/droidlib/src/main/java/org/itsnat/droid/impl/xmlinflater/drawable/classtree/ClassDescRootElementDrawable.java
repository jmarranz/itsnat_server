package org.itsnat.droid.impl.xmlinflater.drawable.classtree;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.itsnat.droid.impl.dom.DOMElement;
import org.itsnat.droid.impl.xmlinflated.drawable.ElementDrawableRoot;
import org.itsnat.droid.impl.xmlinflater.drawable.ClassDescDrawableMgr;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;

/**
 * Created by jmarranz on 27/11/14.
 */
public abstract class ClassDescRootElementDrawable<Tdrawable extends Drawable> extends ClassDescDrawable<Tdrawable>
{
    public ClassDescRootElementDrawable(ClassDescDrawableMgr classMgr, String className)
    {
        super(classMgr, className, null);
    }

    public abstract ElementDrawableRoot createRootElementDrawable(DOMElement rootElem, XMLInflaterDrawable inflaterDrawable, Context ctx);
}
