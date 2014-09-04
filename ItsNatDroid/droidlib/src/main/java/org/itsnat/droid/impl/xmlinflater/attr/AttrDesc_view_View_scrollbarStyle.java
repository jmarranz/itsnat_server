package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingAttrTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_scrollbarStyle extends AttrDesc
{
    public AttrDesc_view_View_scrollbarStyle(ClassDescViewBased parent)
    {
        super(parent,"scrollbarStyle");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingAttrTasks pending)
    {
        int intValue;
        if      ("insideOverlay".equals(value))
            intValue = View.SCROLLBARS_INSIDE_OVERLAY;
        else if ("insideInset".equals(value))
            intValue = View.SCROLLBARS_INSIDE_INSET;
        else if ("outsideOverlay".equals(value))
            intValue = View.SCROLLBARS_OUTSIDE_OVERLAY;
        else if ("outsideInset".equals(value))
            intValue = View.SCROLLBARS_OUTSIDE_INSET;
        else
            throw new ItsNatDroidException("Unrecognized value " + value + " for attribute " + name);

        view.setScrollBarStyle(intValue);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"insideOverlay",null,null);
    }
}
