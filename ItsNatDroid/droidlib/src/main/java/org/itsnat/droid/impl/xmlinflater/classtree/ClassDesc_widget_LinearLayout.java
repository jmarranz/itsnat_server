package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_widget_LinearLayout_gravity;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_widget_LinearLayout_orientation;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_LinearLayout extends ClassDescViewBased
{
    public ClassDesc_widget_LinearLayout(ClassDescViewBased parentClass)
    {
        super("android.widget.LinearLayout",parentClass);
    }

    protected void init()
    {
        super.init();


        addAttrDesc(new AttrDesc_widget_LinearLayout_gravity(this)); // "gravity"
        addAttrDesc(new AttrDesc_widget_LinearLayout_orientation(this)); // "orientation"
    }
}

