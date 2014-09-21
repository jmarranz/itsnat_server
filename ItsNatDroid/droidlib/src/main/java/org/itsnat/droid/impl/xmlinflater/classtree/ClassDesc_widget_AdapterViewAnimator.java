package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecFieldSetBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_AdapterViewAnimator_inAnimation;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_AdapterViewAnimator_outAnimation;

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

        addAttrDesc(new AttrDescReflecMethodBoolean(this,"animateFirstView",true));
        addAttrDesc(new AttrDesc_widget_AdapterViewAnimator_inAnimation(this));
        addAttrDesc(new AttrDescReflecFieldSetBoolean(this,"loopViews","mLoopViews",false));
        addAttrDesc(new AttrDesc_widget_AdapterViewAnimator_outAnimation(this));
    }
}

