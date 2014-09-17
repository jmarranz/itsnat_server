package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecBoolean;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 17/09/14.
 */
public class AttrDesc_widget_ProgressBar_indeterminate extends AttrDescReflecBoolean
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
