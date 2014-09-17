package org.itsnat.droid.impl.xmlinflater.attr.view;

import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_layout_height extends AttrDesc
{
    public AttrDesc_view_View_layout_height(ClassDescViewBased parent)
    {
        super(parent,"layout_height");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int height = getDimensionWithName(view, value);

        ViewGroup.LayoutParams params = view.getLayoutParams();

        params.height = height;

        if (oneTimeAttrProcess != null) oneTimeAttrProcess.setNeededSetLayoutParams();
        else view.setLayoutParams(view.getLayoutParams());
    }

    public void removeAttribute(View view)
    {
        ViewGroup.LayoutParams params = view.getLayoutParams();

        params.height = ViewGroup.LayoutParams.MATCH_PARENT; // Por poner algo, no lo tengo claro y yo creo que puede cambiar según el ViewGroup padre

        view.setLayoutParams(params);
    }
}
