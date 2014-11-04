package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDesc_widget_ViewAnimator_inoutAnimation;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_ViewAnimator extends ClassDescViewBased
{
    public ClassDesc_widget_ViewAnimator(ClassDescViewMgr classMgr,ClassDesc_widget_FrameLayout parentClass)
    {
        super(classMgr,"android.widget.ViewAnimator",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecMethodBoolean(this,"animateFirstView",true));
        addAttrDesc(new AttrDesc_widget_ViewAnimator_inoutAnimation(this,"inAnimation"));
        addAttrDesc(new AttrDesc_widget_ViewAnimator_inoutAnimation(this,"outAnimation"));

    }
}

