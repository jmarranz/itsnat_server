package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

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
