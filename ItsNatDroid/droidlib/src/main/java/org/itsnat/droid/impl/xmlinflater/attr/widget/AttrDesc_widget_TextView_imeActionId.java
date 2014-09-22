package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_TextView_imeActionId extends AttrDesc
{
    public AttrDesc_widget_TextView_imeActionId(ClassDescViewBased parent)
    {
        super(parent,"imeActionId");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int convertedValue = getInteger(value,view.getContext());

        TextView textView = (TextView)view;
        textView.setImeActionLabel(textView.getImeActionLabel(),convertedValue);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view, "0", null,null);
    }

}
