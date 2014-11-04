package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_ViewAnimator_inoutAnimation;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_ViewAnimator extends ClassDescViewBased
{
    public ClassDescView_widget_ViewAnimator(ClassDescViewMgr classMgr,ClassDescView_widget_FrameLayout parentClass)
    {
        super(classMgr,"android.widget.ViewAnimator",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"animateFirstView",true));
        addAttrDesc(new AttrDescView_widget_ViewAnimator_inoutAnimation(this,"inAnimation"));
        addAttrDesc(new AttrDescView_widget_ViewAnimator_inoutAnimation(this,"outAnimation"));

    }
}

