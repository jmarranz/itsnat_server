package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.view.View;
import android.widget.ExpandableListView;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecFieldGet;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_ExpandableListView_indicatorRight extends AttrDescReflecFieldGet
{
    public AttrDesc_widget_ExpandableListView_indicatorRight(ClassDescViewBased parent)
    {
        super(parent,"indicatorRight","mIndicatorLeft");
    }

    private void callMethod(View view,int value)
    {
        ((ExpandableListView) view).setIndicatorBounds((Integer)getField(view),value);
    }

    @Override
    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int convValue = getDimensionInt(value, view.getContext());

        callMethod(view,convValue);
    }

    @Override
    public void removeAttribute(View view)
    {
        callMethod(view,-1);
    }

}
