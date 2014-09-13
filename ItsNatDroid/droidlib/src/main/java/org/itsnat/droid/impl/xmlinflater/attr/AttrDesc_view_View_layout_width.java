package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_layout_width extends AttrDesc
{
    public AttrDesc_view_View_layout_width(ClassDescViewBased parent)
    {
        super(parent,"layout_width");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int width = getDimensionWithName(view, value);

        ViewGroup.LayoutParams params = view.getLayoutParams();

        params.width = width;

        if (oneTimeAttrProcess != null) oneTimeAttrProcess.setNeededSetLayoutParams();
        else view.setLayoutParams(view.getLayoutParams());
    }

    public void removeAttribute(View view)
    {
        ViewGroup.LayoutParams params = view.getLayoutParams();

        params.width = ViewGroup.LayoutParams.MATCH_PARENT;

        view.setLayoutParams(params);
    }
}
