package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodMultipleName;
import org.itsnat.droid.impl.xmlinflater.layout.attr.GravityUtil;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_FrameLayout extends ClassDescViewBased
{
    public ClassDescView_widget_FrameLayout(ClassDescViewMgr classMgr,ClassDescView_view_ViewGroup parentClass)
    {
        super(classMgr,"android.widget.FrameLayout",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescViewReflecMethodDrawable(this,"foreground","@null"));
        addAttrDesc(new AttrDescViewReflecMethodMultipleName(this,"foregroundGravity", GravityUtil.valueMap,"fill"));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"measureAllChildren",false));

    }
}

