package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_view_View_layout_width extends AttrDescView
{
    public AttrDescView_view_View_layout_width(ClassDescViewBased parent)
    {
        super(parent,"layout_width");
    }

    public void setAttribute(final View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        final int width = getDimensionWithNameInt(value,view.getContext());

        Runnable task = new Runnable(){
            @Override
            public void run()
            {
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.width = width;
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
        ViewGroup.LayoutParams params = view.getLayoutParams();

        params.width = ViewGroup.LayoutParams.MATCH_PARENT;

        view.setLayoutParams(params);
    }
}
