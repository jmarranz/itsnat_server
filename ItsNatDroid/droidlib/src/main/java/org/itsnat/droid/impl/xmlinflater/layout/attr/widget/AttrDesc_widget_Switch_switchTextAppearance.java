package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.view.View;
import android.widget.Switch;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_Switch_switchTextAppearance extends AttrDesc
{
    public AttrDesc_widget_Switch_switchTextAppearance(ClassDescViewBased parent)
    {
        super(parent,"switchTextAppearance");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        Context ctx = view.getContext();
        int resId = getIdentifier(value,ctx);

        ((Switch)view).setSwitchTextAppearance(ctx, resId);
    }

    public void removeAttribute(View view)
    {
        // Android tiene un estilo por defecto
    }
}
