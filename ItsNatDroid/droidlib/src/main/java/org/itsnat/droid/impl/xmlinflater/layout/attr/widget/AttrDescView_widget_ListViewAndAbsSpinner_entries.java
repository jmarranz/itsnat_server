package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.view.View;
import android.widget.AbsSpinner;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_ListViewAndAbsSpinner_entries extends AttrDescView
{
    public AttrDescView_widget_ListViewAndAbsSpinner_entries(ClassDescViewBased parent)
    {
        super(parent,"entries");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        Context ctx = view.getContext();
        CharSequence[] entries = getTextArray(value,view.getContext());

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(ctx, android.R.layout.simple_list_item_1, entries);
        if (view instanceof ListView)
            ((ListView)view).setAdapter(adapter);
        else if (view instanceof AbsSpinner)
            ((AbsSpinner)view).setAdapter(adapter);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"0",null,null);
    }

}
