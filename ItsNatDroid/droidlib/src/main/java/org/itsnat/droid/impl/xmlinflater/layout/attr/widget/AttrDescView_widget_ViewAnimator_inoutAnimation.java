package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.view.View;
import android.widget.ViewAnimator;

import org.itsnat.droid.impl.dom.AttrParsed;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
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

    public void setAttribute(View view, AttrParsed attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int id = getIdentifier(attr.getValue(),ctx);

        if ("inAnimation".equals(name))
            ((ViewAnimator)view).setInAnimation(ctx,id);
        else if ("outAnimation".equals(name))
            ((ViewAnimator)view).setOutAnimation(ctx,id);
    }

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        setAttribute(view,"0",xmlInflaterLayout,ctx,null,null);
    }

}
