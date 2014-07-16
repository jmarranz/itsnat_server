package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_widget_LinearLayout_orientation;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_LinearLayout extends ClassDescViewBased
{
    public ClassDesc_widget_LinearLayout(ClassDescViewBased parent)
    {
        super("android.widget.LinearLayout",parent);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDesc_widget_LinearLayout_orientation(this)); // "orientation"
    }
}

