package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.content.Context;
import android.view.View;
import android.widget.ViewAnimator;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_ViewAnimator_inoutAnimation extends AttrDesc
{
    public AttrDesc_widget_ViewAnimator_inoutAnimation(ClassDescViewBased parent, String name)
    {
        super(parent,name);
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        Context ctx = view.getContext();
        int id = getIdentifier(value,ctx);

        if ("inAnimation".equals(name))
            ((ViewAnimator)view).setInAnimation(ctx,id);
        else if ("outAnimation".equals(name))
            ((ViewAnimator)view).setOutAnimation(ctx,id);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"0",null,null);
    }

}
