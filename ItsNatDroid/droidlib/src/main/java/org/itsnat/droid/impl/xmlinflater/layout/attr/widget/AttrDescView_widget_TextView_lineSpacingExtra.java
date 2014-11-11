package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.model.AttrParsed;
import org.itsnat.droid.impl.xmlinflater.FieldContainer;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_TextView_lineSpacingExtra extends AttrDescView
{
    protected FieldContainer<Float> field;

    public AttrDescView_widget_TextView_lineSpacingExtra(ClassDescViewBased parent)
    {
        super(parent,"lineSpacingExtra");
        this.field = new FieldContainer<Float>(parent.getDeclaredClass(),"mSpacingMult");
    }

    public void setAttribute(View view, AttrParsed attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        float convertedValue = getDimensionFloat(attr.getValue(),ctx);

        TextView textView = (TextView)view;
        textView.setLineSpacing(convertedValue, getMultiplier(textView));
    }

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        setAttribute(view, "0dp",xmlInflaterLayout,ctx, null,null);
    }

    protected float getMultiplier(TextView view)
    {
        return field.get(view);
    }
}
