package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
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

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess)
    {
        int id = getIdentifier(value, view.getContext());

        if (id > 0)
            ((ViewGroup)view).setLayoutAnimation(AnimationUtils.loadLayoutAnimation(view.getContext(), id));
        else
            ((ViewGroup)view).setLayoutAnimation(null);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"0",null);
    }
}
