package org.itsnat.droid.impl.xmlinflated.drawable;

import android.graphics.drawable.BitmapDrawable;

/**
 * Created by jmarranz on 27/11/14.
 */
public class LayerDrawableItemBitmap extends ChildElementDrawable
{
    protected LayerDrawableItem parentChildDrawable;

    public LayerDrawableItemBitmap(LayerDrawableItem parentChildDrawable,BitmapDrawable bitmapDrawable)
    {
        this.parentChildDrawable = parentChildDrawable;
        parentChildDrawable.setDrawable(bitmapDrawable);
    }

    public BitmapDrawable getBitmapDrawable()
    {
        return (BitmapDrawable)parentChildDrawable.getDrawable();
    }

}
