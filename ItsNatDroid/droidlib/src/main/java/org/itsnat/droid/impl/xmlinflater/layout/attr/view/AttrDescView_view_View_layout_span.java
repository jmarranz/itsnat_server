package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.content.Context;
import android.view.View;
import android.widget.TableRow;

import org.itsnat.droid.impl.dom.AttrParsed;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_view_View_layout_span extends AttrDescView
{
    public AttrDescView_view_View_layout_span(ClassDescViewBased parent)
    {
        super(parent,"layout_span");
    }

    public void setAttribute(final View view, AttrParsed attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        final int convValue = getInteger(attr.getValue(),ctx);

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

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        TableRow.LayoutParams params = (TableRow.LayoutParams)view.getLayoutParams();

        params.span = 1;

        view.setLayoutParams(view.getLayoutParams());
    }
}
