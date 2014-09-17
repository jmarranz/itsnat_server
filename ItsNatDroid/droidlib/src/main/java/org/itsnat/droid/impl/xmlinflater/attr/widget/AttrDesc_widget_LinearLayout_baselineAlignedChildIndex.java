package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecInt;
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

    public void setAttribute(final View view,final String value,final OneTimeAttrProcess oneTimeAttrProcess,final PendingPostInsertChildrenTasks pending)
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
            pending.addTask(task);
        }
        else super.setAttribute(view,value,oneTimeAttrProcess,pending);
    }

}
