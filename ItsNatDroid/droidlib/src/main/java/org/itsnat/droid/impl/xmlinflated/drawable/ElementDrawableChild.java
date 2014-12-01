package org.itsnat.droid.impl.xmlinflated.drawable;

/**
 * Created by jmarranz on 27/11/14.
 */
public abstract class ElementDrawableChild extends ElementDrawable
{
    protected ElementDrawable parentElement;

    protected ElementDrawableChild()
    {
    }

    public ElementDrawable getParentElementDrawable()
    {
        return parentElement;
    }

    public void setParentElementDrawable(ElementDrawable parentElement)
    {
        this.parentElement = parentElement;
    }

    public ElementDrawableRoot getElementDrawableRoot()
    {
        return parentElement.getElementDrawableRoot();
    }


}
