package org.itsnat.droid.impl.xmlinflater.attr;

import android.animation.ObjectAnimator;
import android.widget.AdapterViewAnimator;

import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_AdapterViewAnimator_outAnimation extends AttrDesc_widget_AdapterViewAnimator_inoutAnimation_Base
{
    public AttrDesc_widget_AdapterViewAnimator_outAnimation(ClassDescViewBased parent)
    {
        super(parent,"outAnimation");
    }

    @Override
    protected void setAnimation(AdapterViewAnimator view, ObjectAnimator animator)
    {
        view.setOutAnimation(animator);
    }

    @Override
    protected ObjectAnimator getDefaultAnimation()
    {
        return getDefaultOutAnimation();
    }

    protected static ObjectAnimator getDefaultOutAnimation()
    {
        // Copiado del código fuente
        ObjectAnimator anim = ObjectAnimator.ofFloat(null, "alpha", 1.0f, 0.0f);
        anim.setDuration(DEFAULT_ANIMATION_DURATION);
        return anim;
    }
}
