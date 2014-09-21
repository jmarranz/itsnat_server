package org.itsnat.droid.impl.xmlinflater.attr.view;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_fadeScrollbars extends AttrDescReflecMethodBoolean
{
    public AttrDesc_view_View_fadeScrollbars(ClassDescViewBased parent)
    {
        super(parent,"fadeScrollbars","setScrollbarFadingEnabled",true);
    }

    public void setAttribute(final View view,final String value, final OneTimeAttrProcess oneTimeAttrProcess,final PendingPostInsertChildrenTasks pending)
    {
        if (oneTimeAttrProcess != null)
        {
            // Delegamos al final para que esté totalmente claro si hay o no scrollbars
            oneTimeAttrProcess.addLastTask(new Runnable()
            {
                @Override
                public void run()
                {
                    AttrDesc_view_View_fadeScrollbars.super.setAttribute(view,value,oneTimeAttrProcess,pending);
                }
            });
        }
        else
        {
            super.setAttribute(view,value,oneTimeAttrProcess,pending);
        }
    }

}
