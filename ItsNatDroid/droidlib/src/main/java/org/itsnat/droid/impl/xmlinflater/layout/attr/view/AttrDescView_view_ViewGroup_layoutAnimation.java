package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_view_ViewGroup_layoutAnimation extends AttrDescView
{
    public AttrDescView_view_ViewGroup_layoutAnimation(ClassDescViewBased parent)
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
        setAttribute(view,"-1",null,null);
    }
}
