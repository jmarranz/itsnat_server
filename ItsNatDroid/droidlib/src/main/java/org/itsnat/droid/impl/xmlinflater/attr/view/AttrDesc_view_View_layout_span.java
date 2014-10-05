package org.itsnat.droid.impl.xmlinflater.attr.view;

import android.view.View;
import android.widget.TableRow;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_layout_span extends AttrDesc
{
    public AttrDesc_view_View_layout_span(ClassDescViewBased parent)
    {
        super(parent,"layout_span");
    }

    public void setAttribute(final View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        final int convValue = getInteger(value, view.getContext());

        Runnable task = new Runnable(){
            @Override
            public void run()
            {
                TableRow.LayoutParams params = (TableRow.LayoutParams)view.getLayoutParams();
                params.span = convValue;
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
        TableRow.LayoutParams params = (TableRow.LayoutParams)view.getLayoutParams();

        params.span = 1;

        view.setLayoutParams(view.getLayoutParams());
    }
}
