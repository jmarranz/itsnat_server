package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 17/09/14.
 */
public class AttrDesc_widget_ProgressBar_indeterminate extends AttrDescReflecMethodBoolean
{
    public AttrDesc_widget_ProgressBar_indeterminate(ClassDescViewBased parent, String name,boolean defaultValue)
    {
        super(parent,name,defaultValue);
    }

    public void setAttribute(final View view,final String value,final OneTimeAttrProcess oneTimeAttrProcess,final PendingPostInsertChildrenTasks pending)
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
                    AttrDesc_widget_ProgressBar_indeterminate.super.setAttribute(view, value, oneTimeAttrProcess, pending);
                }
            });
        }
        else
        {
            super.setAttribute(view,value,oneTimeAttrProcess,pending);
        }
    }
}
