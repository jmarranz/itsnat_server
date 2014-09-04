package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingAttrTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_LinearLayout_baselineAlignedChildIndex extends AttrDescReflecInt
{
    public AttrDesc_widget_LinearLayout_baselineAlignedChildIndex(ClassDescViewBased parent)
    {
        super(parent,"baselineAlignedChildIndex",-1);
    }

    public void setAttribute(final View view,final String value,final OneTimeAttrProcess oneTimeAttrProcess,final PendingAttrTasks pending)
    {
        if (pending != null)
        {
            Runnable task = new Runnable()
            {
                @Override
                public void run()
                {
                    AttrDesc_widget_LinearLayout_baselineAlignedChildIndex.super.setAttribute(view,value,oneTimeAttrProcess,pending);
                }
            };
            pending.addPostInsertChildrenTask(task);
        }
        else super.setAttribute(view,value,oneTimeAttrProcess,pending);
    }

}
