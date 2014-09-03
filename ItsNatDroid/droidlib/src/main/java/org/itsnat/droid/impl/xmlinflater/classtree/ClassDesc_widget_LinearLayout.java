package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecGravity;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecInt;
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

        addAttrDesc(new AttrDescReflecBoolean(this,"baselineAligned",true));
        addAttrDesc(new AttrDescReflecInt(this,"baselineAlignedChildIndex",-1));


        addAttrDesc(new AttrDescReflecGravity(this,"gravity"));
        addAttrDesc(new AttrDesc_widget_LinearLayout_orientation(this)); // "orientation"
    }
}

