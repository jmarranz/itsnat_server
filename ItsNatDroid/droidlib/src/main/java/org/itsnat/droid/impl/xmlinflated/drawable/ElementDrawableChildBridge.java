package org.itsnat.droid.impl.xmlinflated.drawable;

import android.graphics.drawable.Drawable;

import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescRootElementDrawable;

/**
 * Created by jmarranz on 30/11/14.
 */
public class ElementDrawableChildBridge extends ElementDrawableChild
{
    protected ClassDescRootElementDrawable classDescBridge;

    public ElementDrawableChildBridge(ClassDescRootElementDrawable classDescBridge,ElementDrawable parentChildDrawable,Drawable drawable)
    {
        this.classDescBridge = classDescBridge;
        setParentElementDrawable(parentChildDrawable);

        ((ElementDrawableContainer)getParentElementDrawable()).setDrawable(drawable);
    }

    public ClassDescRootElementDrawable getClassDescRootDrawableBridge()
    {
        return classDescBridge;
    }

    public ElementDrawableContainer getElementDrawableContainer()
    {
        return (ElementDrawableContainer)getParentElementDrawable();
    }

    public Drawable getDrawable()
    {
        return getElementDrawableContainer().getDrawable();
    }
}
