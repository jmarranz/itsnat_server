package org.itsnat.droid.impl.xmlinflated.drawable;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by jmarranz on 27/11/14.
 */
public class ElementDrawableRoot extends ElementDrawable implements ElementDrawableContainer
{
    protected Drawable drawable;

    public ElementDrawableRoot(Drawable drawable,ArrayList<ElementDrawable> itemList)
    {
        this.drawable = drawable;
        setChildElementDrawableList(itemList);
    }

    public ElementDrawableRoot()
    {
    }

    public void setChildElementDrawableList(ArrayList<ElementDrawable> childDrawableList)
    {
        this.childDrawableList = childDrawableList;
        if (childDrawableList != null)
        {
            for(int i = 0; i < childDrawableList.size(); i++)
            {
                childDrawableList.get(i).setParentElementDrawable(this);
            }
        }
    }

    public void setParentElementDrawable(ElementDrawable parentElementDrawable)
    {
        // No es error que se llame, es el caso por ej del drawable hijo de <item> o similar, es un Root de drawable pero tiene padre XML
        // Lo ignoramos
    }

    public Drawable getDrawable()
    {
        return drawable;
    }

    public void setDrawable(Drawable drawable)
    {
        this.drawable = drawable;
    }

    public ElementDrawableRoot getElementDrawableRoot()
    {
        return this;
    }
}
