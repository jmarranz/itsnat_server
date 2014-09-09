package org.itsnat.droid.impl.xmlinflater.attr;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_ListView_entries extends AttrDesc
{
    public AttrDesc_widget_ListView_entries(ClassDescViewBased parent)
    {
        super(parent,"entries");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        Context ctx = view.getContext();
        CharSequence[] entries = getTextArray(value,view.getContext());

        ((ListView)view).setAdapter(new ArrayAdapter<CharSequence>(ctx, android.R.layout.simple_list_item_1, entries));
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"0",null,null);
    }

}
