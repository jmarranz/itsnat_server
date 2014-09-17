package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecFieldSet;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_AdapterViewAnimator_loopViews extends AttrDescReflecFieldSet
{
    public AttrDesc_widget_AdapterViewAnimator_loopViews(ClassDescViewBased parent)
    {
        super(parent,"loopViews","mLoopViews");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        boolean convertedValue = getBoolean(value,view.getContext());

        setField(view,convertedValue);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"false",null,null);
    }

}
