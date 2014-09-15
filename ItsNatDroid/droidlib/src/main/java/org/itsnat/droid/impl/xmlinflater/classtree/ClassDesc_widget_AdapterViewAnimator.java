package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_widget_AdapterViewAnimator_inAnimation;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_widget_AdapterViewAnimator_loopViews;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_widget_AdapterViewAnimator_outAnimation;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_AdapterViewAnimator extends ClassDescViewBased
{
    public ClassDesc_widget_AdapterViewAnimator(ClassDescViewBased parentClass)
    {
        super("android.widget.AdapterViewAnimator",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecBoolean(this,"animateFirstView",true));
        addAttrDesc(new AttrDesc_widget_AdapterViewAnimator_inAnimation(this));
        addAttrDesc(new AttrDesc_widget_AdapterViewAnimator_loopViews(this));
        addAttrDesc(new AttrDesc_widget_AdapterViewAnimator_outAnimation(this));
    }
}

