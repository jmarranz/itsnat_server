package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;
import android.widget.RelativeLayout;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_layout_alignWithParentIfMissing extends AttrDesc
{
    public AttrDesc_view_View_layout_alignWithParentIfMissing(ClassDescViewBased parent)
    {
        super(parent,"layout_alignWithParentIfMissing");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
        boolean convValue = getBoolean(value, view.getContext());

        params.alignWithParent = convValue;

        if (oneTimeAttrProcess != null) oneTimeAttrProcess.setNeededSetLayoutParams();
        else view.setLayoutParams(view.getLayoutParams());
    }

    public void removeAttribute(View view)
    {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();

        params.alignWithParent = false;

        view.setLayoutParams(view.getLayoutParams());
    }
}
