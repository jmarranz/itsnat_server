package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodMultipleName;
import org.itsnat.droid.impl.xmlinflater.attr.GravityUtil;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_FrameLayout extends ClassDescViewBased
{
    public ClassDesc_widget_FrameLayout(ClassDesc_widget_ViewGroup parentClass)
    {
        super("android.widget.FrameLayout",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecMethodDrawable(this,"foreground","@null"));
        addAttrDesc(new AttrDescReflecMethodMultipleName(this,"foregroundGravity", GravityUtil.valueMap,"fill"));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"measureAllChildren",false));

    }
}

