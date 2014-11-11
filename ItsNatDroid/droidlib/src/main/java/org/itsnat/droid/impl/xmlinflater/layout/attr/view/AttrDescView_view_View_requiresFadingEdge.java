package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.content.Context;
import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.model.AttrParsed;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_view_View_requiresFadingEdge extends AttrDescView
{
    public AttrDescView_view_View_requiresFadingEdge(ClassDescViewBased parent)
    {
        super(parent,"requiresFadingEdge");
    }

    public void setAttribute(View view, AttrParsed attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        String value = attr.getValue();
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

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        setAttribute(view,"vertical",xmlInflaterLayout,ctx,null,null );
    }
}
