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
public class AttrDesc_widget_ExpandableListView_indicatorLeft extends AttrDescReflecFieldGet
{
    public AttrDesc_widget_ExpandableListView_indicatorLeft(ClassDescViewBased parent)
    {
        super(parent,"indicatorLeft","mIndicatorRight");
    }

    private void callMethod(View view,int value)
    {
        ((ExpandableListView) view).setIndicatorBounds(value, (Integer)getField(view));
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
