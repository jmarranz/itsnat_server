package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.GravityUtil;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecDrawable;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMultipleName;

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

        addAttrDesc(new AttrDescReflecDrawable(this,"foreground","@null"));
        addAttrDesc(new AttrDescReflecMultipleName(this,"foregroundGravity", GravityUtil.valueMap,"fill"));
        addAttrDesc(new AttrDescReflecBoolean(this,"measureAllChildren",false));

    }
}

