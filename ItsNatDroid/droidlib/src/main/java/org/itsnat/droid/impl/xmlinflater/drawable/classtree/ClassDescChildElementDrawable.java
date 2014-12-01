package org.itsnat.droid.impl.xmlinflater.drawable.classtree;

import android.content.Context;

import org.itsnat.droid.impl.dom.DOMElement;
import org.itsnat.droid.impl.xmlinflated.drawable.ElementDrawableChild;
import org.itsnat.droid.impl.xmlinflater.drawable.ClassDescDrawableMgr;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;

/**
 * Created by jmarranz on 27/11/14.
 */
public abstract class ClassDescChildElementDrawable<Tdrawable extends ElementDrawableChild> extends ClassDescDrawable<Tdrawable>
{
    public ClassDescChildElementDrawable(ClassDescDrawableMgr classMgr, String elemName)
    {
        super(classMgr, elemName, null);
    }

    public abstract ElementDrawableChild createChildElementDrawable(DOMElement domElement,XMLInflaterDrawable inflaterDrawable,ElementDrawableChild parentChildDrawable,Context ctx);
}
