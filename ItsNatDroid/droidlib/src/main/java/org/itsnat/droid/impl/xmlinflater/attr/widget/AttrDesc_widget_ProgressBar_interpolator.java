package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_ProgressBar_interpolator extends AttrDesc
{
    public AttrDesc_widget_ProgressBar_interpolator(ClassDescViewBased parent)
    {
        super(parent,"interpolator");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        Context ctx = view.getContext();
        int resId = getIdentifier(value,ctx);

        ((ProgressBar)view).setInterpolator(ctx, resId);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"@android:anim/linear_interpolator",null,null); // Yo creo que es el que usa por defecto Android en este caso
    }
}
