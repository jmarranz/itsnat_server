package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.text.InputFilter;
import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_TextView_maxLength extends AttrDesc
{
    public AttrDesc_widget_TextView_maxLength(ClassDescViewBased parent)
    {
        super(parent,"maxLength");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int convertedValue = getInteger(value,view.getContext());

        TextView textView = (TextView)view;
        if (convertedValue >= 0)
        {
            textView.setFilters(new InputFilter[] { new InputFilter.LengthFilter(convertedValue) });
        }
        else
        {
            InputFilter[] NO_FILTERS = new InputFilter[0];
            textView.setFilters(NO_FILTERS);
        }
    }

    public void removeAttribute(View view)
    {
        setAttribute(view, "-1", null,null);
    }

}
