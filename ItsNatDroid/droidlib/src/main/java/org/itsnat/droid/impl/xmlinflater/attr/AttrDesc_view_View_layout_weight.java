package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;
import android.widget.LinearLayout;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_layout_weight extends AttrDesc
{
    public AttrDesc_view_View_layout_weight(ClassDescViewBased parent)
    {
        super(parent,"layout_weight");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        float weight = getFloat(value,view.getContext());

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)view.getLayoutParams();

        params.weight = weight;

        if (oneTimeAttrProcess != null) oneTimeAttrProcess.setNeededSetLayoutParams();
        else view.setLayoutParams(view.getLayoutParams());
    }

    public void removeAttribute(View view)
    {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)view.getLayoutParams();

        params.weight = 0;

        view.setLayoutParams(view.getLayoutParams());
    }
}
