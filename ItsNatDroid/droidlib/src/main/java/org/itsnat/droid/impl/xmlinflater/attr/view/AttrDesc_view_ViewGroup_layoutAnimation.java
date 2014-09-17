package org.itsnat.droid.impl.xmlinflater.attr.view;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_ViewGroup_layoutAnimation extends AttrDesc
{
    public AttrDesc_view_ViewGroup_layoutAnimation(ClassDescViewBased parent)
    {
        super(parent,"layoutAnimation");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int id = getIdentifier(value, view.getContext());

        LayoutAnimationController controller = id > 0 ?  AnimationUtils.loadLayoutAnimation(view.getContext(), id) : null;

        ((ViewGroup)view).setLayoutAnimation(controller);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"0",null,null);
    }
}
