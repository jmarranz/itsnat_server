package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;
import android.widget.ExpandableListView;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.lang.reflect.Field;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_ExpandableListView_indicatorLeftRight extends AttrDesc
{
    protected Field mIndicatorField;

    public AttrDesc_widget_ExpandableListView_indicatorLeftRight(ClassDescViewBased parent, String name)
    {
        super(parent,name);
    }

    private void callMethod(View view,int value)
    {
        if (name.equals("indicatorLeft"))
            ((ExpandableListView) view).setIndicatorBounds(value, (Integer) getField(view, "mIndicatorRight"));
        else if (name.equals("indicatorRight"))
            ((ExpandableListView) view).setIndicatorBounds((Integer) getField(view, "mIndicatorLeft"), value);
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

    protected Object getField(View view,String fieldName)
    {
        try
        {
            if (mIndicatorField == null)
            {
                this.mIndicatorField = parent.getViewClass().getDeclaredField(fieldName);
                mIndicatorField.setAccessible(true);
            }
            return mIndicatorField.get(view);
        }
        catch (NoSuchFieldException ex) { throw new ItsNatDroidException(ex); }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
    }
}
