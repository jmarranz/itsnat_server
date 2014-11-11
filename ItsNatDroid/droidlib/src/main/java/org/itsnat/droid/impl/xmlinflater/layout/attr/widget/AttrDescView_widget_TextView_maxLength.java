package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.text.InputFilter;
import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.model.AttrParsed;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_TextView_maxLength extends AttrDescView
{
    public AttrDescView_widget_TextView_maxLength(ClassDescViewBased parent)
    {
        super(parent,"maxLength");
    }

    public void setAttribute(View view, AttrParsed attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int convertedValue = getInteger(attr.getValue(),ctx);

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

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        setAttribute(view, "-1",xmlInflaterLayout,ctx, null,null);
    }

}
