package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingAttrTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_drawingCacheQuality extends AttrDesc
{
    public AttrDesc_view_View_drawingCacheQuality(ClassDescViewBased parent)
    {
        super(parent,"drawingCacheQuality");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingAttrTasks pending)
    {
        int intValue;
        if ("auto".equals(value))
            intValue = View.DRAWING_CACHE_QUALITY_AUTO;
        else if ("low".equals(value))
            intValue = View.DRAWING_CACHE_QUALITY_LOW;
        else if ("high".equals(value))
            intValue = View.DRAWING_CACHE_QUALITY_HIGH;
        else
            throw new ItsNatDroidException("Unrecognized value " + value + " for attribute " + name);

        view.setDrawingCacheQuality(intValue);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"auto",null,null);
    }
}
