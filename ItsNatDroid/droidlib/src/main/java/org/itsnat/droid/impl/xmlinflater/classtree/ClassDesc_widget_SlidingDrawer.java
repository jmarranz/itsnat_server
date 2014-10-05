package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_SlidingDrawer extends ClassDescViewBased
{
    public ClassDesc_widget_SlidingDrawer(ClassDescViewMgr classMgr, ClassDesc_widget_ViewGroup parentClass)
    {
        super(classMgr,"android.widget.SlidingDrawer",parentClass);
    }

    protected void init()
    {
        super.init();

        /*
        addAttrDesc(new AttrDescReflecMethodMultipleName(this,"gravity", GravityUtil.valueMap,"left|top"));
        addAttrDesc(new AttrDescReflecMethodId(this,"ignoreGravity"));
        */
    }
}

