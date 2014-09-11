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
public class AttrDesc_widget_ExpandableListView_childIndicatorLeftRight extends AttrDesc
{
    protected Field mChildIndicatorField;

    public AttrDesc_widget_ExpandableListView_childIndicatorLeftRight(ClassDescViewBased parent, String name)
    {
        super(parent,name);
    }

    private void callMethod(View view,int value)
    {
        if (name.equals("childIndicatorLeft"))
            ((ExpandableListView) view).setChildIndicatorBounds(value, (Integer)getField(view,"mChildIndicatorRight"));
        else if (name.equals("childIndicatorRight"))
            ((ExpandableListView) view).setChildIndicatorBounds((Integer)getField(view,"mChildIndicatorLeft"), value);
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
            if (mChildIndicatorField == null)
            {
                this.mChildIndicatorField = parent.getViewClass().getDeclaredField(fieldName);
                mChildIndicatorField.setAccessible(true);
            }
            return mChildIndicatorField.get(view);
        }
        catch (NoSuchFieldException ex) { throw new ItsNatDroidException(ex); }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
    }
}
