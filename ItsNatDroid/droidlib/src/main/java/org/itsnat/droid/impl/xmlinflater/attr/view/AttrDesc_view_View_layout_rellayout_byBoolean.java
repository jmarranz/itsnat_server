package org.itsnat.droid.impl.xmlinflater.attr.view;

import android.view.View;
import android.widget.RelativeLayout;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_layout_rellayout_byBoolean extends AttrDesc
{
    protected int selector;

    public AttrDesc_view_View_layout_rellayout_byBoolean(ClassDescViewBased parent, String name,int selector)
    {
        super(parent,name);
        this.selector = selector;
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
        boolean convValue = getBoolean(value, view.getContext());

        params.addRule(selector, convValue ? RelativeLayout.TRUE : 0);

        if (oneTimeAttrProcess != null) oneTimeAttrProcess.setNeededSetLayoutParams();
        else view.setLayoutParams(view.getLayoutParams());
    }

    public void removeAttribute(View view)
    {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
        params.addRule(selector, 0); // ver el caso LayoutBelow

        view.setLayoutParams(view.getLayoutParams());
    }
}
