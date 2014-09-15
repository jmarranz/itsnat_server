package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.impl.util.MiscUtil;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_scrollbarAlwaysDrawVerticalTrack extends AttrDescReflecFieldFieldMethod
{
    public AttrDesc_view_View_scrollbarAlwaysDrawVerticalTrack(ClassDescViewBased parent)
    {
        super(parent,"scrollbarAlwaysDrawVerticalTrack","mScrollCache","scrollBar","setAlwaysDrawVerticalTrack",
                MiscUtil.resolveClass("android.view.View$ScrollabilityCache"),MiscUtil.resolveClass("android.widget.ScrollBarDrawable"),
                boolean.class);
    }

    public void setAttribute(final View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        final boolean convertedValue = getBoolean(value, view.getContext());

        // Delegamos al final para que esté claro si hay o no scrollbars
        pending.addTask(new Runnable()
        {
            @Override
            public void run()
            {
                callFieldFieldMethod(view, convertedValue);
            }
        });
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"false",null,null);
    }


}
