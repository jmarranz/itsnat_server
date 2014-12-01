package org.itsnat.droid.impl.xmlinflated.drawable;

import android.graphics.drawable.Drawable;

import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescRootElementDrawable;

/**
 * Created by jmarranz on 27/11/14.
 */
public class LayerDrawableItemBridge extends ElementDrawableChildBridge
{
    public LayerDrawableItemBridge(ClassDescRootElementDrawable classDesc, LayerDrawableItem parentChildDrawable, Drawable drawable)
    {
        super(classDesc);
        setParentElementDrawable(parentChildDrawable);

        parentChildDrawable.setDrawable(drawable);
    }

    public LayerDrawableItem getLayerDrawableItem()
    {
        return (LayerDrawableItem)getParentElementDrawable();
    }

    public Drawable getDrawable()
    {
        return getLayerDrawableItem().getDrawable();
    }

}
