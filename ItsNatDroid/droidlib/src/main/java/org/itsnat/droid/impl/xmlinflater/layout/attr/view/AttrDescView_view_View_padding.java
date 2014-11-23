package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.content.Context;
import android.view.View;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_view_View_padding extends AttrDescView
{
    public AttrDescView_view_View_padding(ClassDescViewBased parent, String name)
    {
        super(parent,name);
    }

    public void setAttribute(View view, DOMAttr attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int convValue = getDimensionInt(attr.getValue(), ctx);

        String name = getName();
        if ("padding".equals(name))
            view.setPadding(convValue,convValue,convValue,convValue);
        else if ("paddingLeft".equals(name))
            view.setPadding(convValue,view.getPaddingTop(),view.getPaddingRight(),view.getPaddingBottom());
        else if ("paddingTop".equals(name))
            view.setPadding(view.getPaddingLeft(),convValue,view.getPaddingRight(),view.getPaddingBottom());
        else if ("paddingRight".equals(name))
            view.setPadding(view.getPaddingLeft(),view.getPaddingTop(),convValue,view.getPaddingBottom());
        else if ("paddingBottom".equals(name))
            view.setPadding(view.getPaddingLeft(),view.getPaddingTop(),view.getPaddingRight(),convValue);
    }

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        setAttribute(view,"0dp",xmlInflaterLayout,ctx,null,null);
    }
}
