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
public class AttrDesc_widget_TextView_imeActionLabel extends AttrDesc
{
    public AttrDesc_widget_TextView_imeActionLabel(ClassDescViewBased parent)
    {
        super(parent,"imeActionLabel");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        String convertedValue = getString(value,view.getContext());

        TextView textView = (TextView)view;
        textView.setImeActionLabel(convertedValue,textView.getImeActionId());
    }

    public void removeAttribute(View view)
    {
        setAttribute(view, "", null,null); // No estoy seguro del valor por defecto
    }

}
