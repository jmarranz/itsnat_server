package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecGravity;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_widget_LinearLayout_orientation;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_LinearLayout extends ClassDescViewBased
{
    public ClassDesc_widget_LinearLayout(ClassDesc_widget_ViewGroup parentClass)
    {
        super("android.widget.LinearLayout",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecGravity(this,"gravity")); // "orientation"
        addAttrDesc(new AttrDesc_widget_LinearLayout_orientation(this)); // "orientation"
    }
}

