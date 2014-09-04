package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingAttrTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_layout_height extends AttrDesc_view_View_layout_widthheight_Base
{
    public AttrDesc_view_View_layout_height(ClassDescViewBased parent)
    {
        super(parent,"layout_height");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingAttrTasks pending)
    {
        int height = getDimension(view,value);

        ViewGroup.LayoutParams params = view.getLayoutParams();

        params.height = height;

        if (oneTimeAttrProcess != null) oneTimeAttrProcess.neededSetLayoutParams = true;
        else view.setLayoutParams(view.getLayoutParams());
    }

    public void removeAttribute(View view)
    {
        ViewGroup.LayoutParams params = view.getLayoutParams();

        params.height = ViewGroup.LayoutParams.MATCH_PARENT;

        view.setLayoutParams(params);
    }
}
