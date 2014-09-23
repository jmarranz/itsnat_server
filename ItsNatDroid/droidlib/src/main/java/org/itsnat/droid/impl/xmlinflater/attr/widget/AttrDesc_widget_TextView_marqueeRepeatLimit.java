package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodInt;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_TextView_marqueeRepeatLimit extends AttrDescReflecMethodInt
{
    public AttrDesc_widget_TextView_marqueeRepeatLimit(ClassDescViewBased parent)
    {
        super(parent,"marqueeRepeatLimit",3);
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        if ("marquee_forever".equals(value))
            value = "-1";
        super.setAttribute(view,value,oneTimeAttrProcess,pending);
    }
}
