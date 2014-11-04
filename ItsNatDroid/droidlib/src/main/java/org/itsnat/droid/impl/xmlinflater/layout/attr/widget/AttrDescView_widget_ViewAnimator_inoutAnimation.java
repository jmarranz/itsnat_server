package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.view.View;
import android.widget.ViewAnimator;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_ViewAnimator_inoutAnimation extends AttrDescView
{
    public AttrDescView_widget_ViewAnimator_inoutAnimation(ClassDescViewBased parent, String name)
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
