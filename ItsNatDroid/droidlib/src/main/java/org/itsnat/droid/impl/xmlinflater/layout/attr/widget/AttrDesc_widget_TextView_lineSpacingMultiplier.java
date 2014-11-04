package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.layout.attr.FieldContainer;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_TextView_lineSpacingMultiplier extends AttrDesc
{
    protected FieldContainer<Float> field;

    public AttrDesc_widget_TextView_lineSpacingMultiplier(ClassDescViewBased parent)
    {
        super(parent,"lineSpacingMultiplier");
        this.field = new FieldContainer<Float>(parent,"mSpacingAdd");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        float convertedValue = getFloat(value, view.getContext());

        TextView textView = (TextView)view;
        textView.setLineSpacing(getLineSpacingExtra(textView),convertedValue);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view, "1.0", null,null);
    }

    protected float getLineSpacingExtra(TextView view)
    {
        return field.get(view);
    }
}
