package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodMultipleName;
import org.itsnat.droid.impl.xmlinflater.layout.attr.GravityUtil;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_FrameLayout extends ClassDescViewBased
{
    public ClassDesc_widget_FrameLayout(ClassDescViewMgr classMgr,ClassDesc_view_ViewGroup parentClass)
    {
        super(classMgr,"android.widget.FrameLayout",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecMethodDrawable(this,"foreground","@null"));
        addAttrDesc(new AttrDescReflecMethodMultipleName(this,"foregroundGravity", GravityUtil.valueMap,"fill"));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"measureAllChildren",false));

    }
}

