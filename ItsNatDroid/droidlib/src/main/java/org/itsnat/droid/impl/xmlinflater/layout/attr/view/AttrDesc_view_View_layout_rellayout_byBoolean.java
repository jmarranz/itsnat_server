package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.view.View;
import android.widget.RelativeLayout;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

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

    public void setAttribute(final View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        final boolean convValue = getBoolean(value, view.getContext());

        Runnable task = new Runnable(){
            @Override
            public void run()
            {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
                params.addRule(selector, convValue ? RelativeLayout.TRUE : 0);
            }};

        if (oneTimeAttrProcess != null)
        {
            oneTimeAttrProcess.addLayoutParamsTask(task);
        }
        else
        {
            task.run();
            view.setLayoutParams(view.getLayoutParams());
        }
    }

    public void removeAttribute(View view)
    {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
        params.addRule(selector, 0); // ver el caso LayoutBelow

        view.setLayoutParams(view.getLayoutParams());
    }
}
