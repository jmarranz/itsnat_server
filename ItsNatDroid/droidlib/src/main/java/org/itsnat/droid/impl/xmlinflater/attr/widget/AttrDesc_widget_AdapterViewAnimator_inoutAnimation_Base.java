package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.AdapterViewAnimator;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class AttrDesc_widget_AdapterViewAnimator_inoutAnimation_Base extends AttrDesc
{
    protected static final int DEFAULT_ANIMATION_DURATION = 200;

    public AttrDesc_widget_AdapterViewAnimator_inoutAnimation_Base(ClassDescViewBased parent,String name)
    {
        super(parent,name);
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int id = getIdentifier(value, view.getContext());

        ObjectAnimator animator = id > 0 ? (ObjectAnimator)AnimatorInflater.loadAnimator(view.getContext(), id) : getDefaultAnimation();

        setAnimation((AdapterViewAnimator)view,animator);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"0",null,null);
    }

    protected abstract void setAnimation(AdapterViewAnimator view,ObjectAnimator animator);
    protected abstract ObjectAnimator getDefaultAnimation();
}
