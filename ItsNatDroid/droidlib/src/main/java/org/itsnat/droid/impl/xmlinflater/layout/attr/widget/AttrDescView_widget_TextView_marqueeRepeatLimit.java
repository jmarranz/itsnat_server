package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodInt;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_TextView_marqueeRepeatLimit extends AttrDescViewReflecMethodInt
{
    public AttrDescView_widget_TextView_marqueeRepeatLimit(ClassDescViewBased parent)
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
