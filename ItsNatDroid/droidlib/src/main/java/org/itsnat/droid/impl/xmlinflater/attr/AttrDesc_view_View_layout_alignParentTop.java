package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;
import android.widget.RelativeLayout;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_layout_alignParentTop extends AttrDesc
{
    public AttrDesc_view_View_layout_alignParentTop(ClassDescViewBased parent)
    {
        super(parent,"layout_alignParentTop");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
        boolean convValue = getBoolean(value, view.getContext());

        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, convValue ? RelativeLayout.TRUE : 0);

        if (oneTimeAttrProcess != null) oneTimeAttrProcess.setNeededSetLayoutParams();
        else view.setLayoutParams(view.getLayoutParams());
    }

    public void removeAttribute(View view)
    {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0); // ver el caso LayoutBelow

        view.setLayoutParams(view.getLayoutParams());
    }
}
