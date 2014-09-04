package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingAttrTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_requiresFadingEdge extends AttrDesc
{
    public AttrDesc_view_View_requiresFadingEdge(ClassDescViewBased parent)
    {
        super(parent,"requiresFadingEdge");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingAttrTasks pending)
    {
        String[] names = value.split("\\|");
        for(String name : names)
        {
            if ("none".equals(name))
            {
                view.setVerticalFadingEdgeEnabled(false);
                view.setHorizontalFadingEdgeEnabled(false);
            }
            else if ("horizontal".equals(name))
            {
                view.setHorizontalFadingEdgeEnabled(true);
            }
            else if ("vertical".equals(name))
            {
                view.setVerticalFadingEdgeEnabled(true);
            }
            else throw new ItsNatDroidException("Unrecognized value " + value + " for attribute " + name);
        }
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"vertical",null,null );
    }
}
