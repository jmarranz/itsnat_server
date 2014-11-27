package org.itsnat.droid.impl.xmlinflated.drawable;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by jmarranz on 27/11/14.
 */
public abstract class ChildElementDrawable
{
    protected Drawable parentDrawable;
    protected ArrayList<ChildElementDrawable> childDrawableList;

    protected ChildElementDrawable()
    {
    }

    public Drawable getParentDrawable()
    {
        return parentDrawable;
    }

    public void setParentDrawable(Drawable parentDrawable)
    {
        this.parentDrawable = parentDrawable;
    }

    public ArrayList<ChildElementDrawable> getChildElementDrawableList()
    {
        return childDrawableList;
    }

    public void initChildElementDrawableList(int size)
    {
        this.childDrawableList = new ArrayList<ChildElementDrawable>(size);
    }

    public void addChildElementDrawable(ChildElementDrawable child)
    {
        childDrawableList.add(child);
    }
}
