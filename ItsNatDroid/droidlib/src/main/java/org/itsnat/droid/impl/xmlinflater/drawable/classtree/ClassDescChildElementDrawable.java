package org.itsnat.droid.impl.xmlinflater.drawable.classtree;

import android.content.Context;

import org.itsnat.droid.impl.dom.DOMElement;
import org.itsnat.droid.impl.xmlinflated.drawable.ChildElementDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.ClassDescDrawableMgr;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;

/**
 * Created by jmarranz on 27/11/14.
 */
public abstract class ClassDescChildElementDrawable<Tdrawable extends ChildElementDrawable> extends ClassDescDrawable<Tdrawable>
{
    public ClassDescChildElementDrawable(ClassDescDrawableMgr classMgr, String elemName)
    {
        super(classMgr, elemName, null);
    }

    public abstract ChildElementDrawable createChildElementDrawable(DOMElement domElement,XMLInflaterDrawable inflaterDrawable,ChildElementDrawable parentChildDrawable,Context ctx);
}
