package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.view.View;

import org.itsnat.droid.impl.dom.AttrParsed;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 17/09/14.
 */
public class AttrDescView_widget_ProgressBar_indeterminate extends AttrDescViewReflecMethodBoolean
{
    public AttrDescView_widget_ProgressBar_indeterminate(ClassDescViewBased parent, String name, boolean defaultValue)
    {
        super(parent,name,defaultValue);
    }

    public void setAttribute(final View view, final AttrParsed attr,final XMLInflaterLayout xmlInflaterLayout, final Context ctx, final OneTimeAttrProcess oneTimeAttrProcess, final PendingPostInsertChildrenTasks pending)
    {
        if (oneTimeAttrProcess != null)
        {
            // setIndeterminate depende de indeterminateOnly que debe definirse antes, si el usuario lo pone después
            // setIndeterminate funcionará mal
            oneTimeAttrProcess.addLastTask(new Runnable()
            {
                @Override
                public void run()
                {
                    AttrDescView_widget_ProgressBar_indeterminate.super.setAttribute(view, attr,xmlInflaterLayout, ctx, oneTimeAttrProcess, pending);
                }
            });
        }
        else
        {
            super.setAttribute(view,attr, xmlInflaterLayout, ctx, oneTimeAttrProcess,pending);
        }
    }
}
