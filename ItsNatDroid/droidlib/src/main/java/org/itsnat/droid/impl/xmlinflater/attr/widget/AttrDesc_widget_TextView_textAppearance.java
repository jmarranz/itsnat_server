package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_TextView_textAppearance extends AttrDesc
{
    public AttrDesc_widget_TextView_textAppearance(ClassDescViewBased parent)
    {
        super(parent,"textAppearance");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        Context ctx = view.getContext();
        int resId = getIdentifier(value,ctx);

        ((TextView)view).setTextAppearance(ctx,resId);
    }

    public void removeAttribute(View view)
    {
        // No se que hacer
    }
}
