package org.itsnat.droid.impl.xmlinflated.drawable;

import android.graphics.drawable.Drawable;

import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescRootElementDrawable;

/**
 * Created by jmarranz on 27/11/14.
 */
public class LayerDrawableItemBridge extends ChildElementDrawableBridge
{
    protected LayerDrawableItem parentChildDrawable;

    public LayerDrawableItemBridge(ClassDescRootElementDrawable classDesc, LayerDrawableItem parentChildDrawable, Drawable drawable)
    {
        super(classDesc);
        this.parentChildDrawable = parentChildDrawable;

        parentChildDrawable.setDrawable(drawable);
    }

    public Drawable getDrawable()
    {
        return parentChildDrawable.getDrawable();
    }

}
