package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.view.View;
import android.widget.ExpandableListView;

import org.itsnat.droid.impl.dom.AttrParsed;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecFieldGet;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_ExpandableListView_indicatorRight extends AttrDescViewReflecFieldGet
{
    public AttrDescView_widget_ExpandableListView_indicatorRight(ClassDescViewBased parent)
    {
        super(parent,"indicatorRight","mIndicatorLeft");
    }

    private void callMethod(View view,int value)
    {
        ((ExpandableListView) view).setIndicatorBounds((Integer)getField(view),value);
    }

    @Override
    public void setAttribute(View view, AttrParsed attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int convValue = getDimensionInt(attr.getValue(),ctx);

        callMethod(view,convValue);
    }

    @Override
    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        callMethod(view,-1);
    }

}
