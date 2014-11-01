package org.itsnat.droid.impl.model.layout;

import org.itsnat.droid.impl.model.ElementParsed;

/**
 * Created by jmarranz on 27/10/14.
 */
public class ViewParsed extends ElementParsed
{
    protected String styleAttr;

    public ViewParsed(String name,ViewParsed parentElement)
    {
        super(name,parentElement);
    }

    public ViewParsed getParentViewParsed()
    {
        return (ViewParsed)getParentElement();
    }

    public String getStyleAttr()
    {
        return styleAttr;
    }

    public void setStyleAttr(String styleAttr)
    {
        this.styleAttr = styleAttr;
    }

    public void addChildView(ViewParsed viewParsed)
    {
        super.addChild(viewParsed);
    }
}
