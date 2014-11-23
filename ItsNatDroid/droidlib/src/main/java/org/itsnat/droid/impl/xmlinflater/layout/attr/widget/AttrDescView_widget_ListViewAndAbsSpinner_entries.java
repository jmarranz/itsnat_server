package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.view.View;
import android.widget.AbsSpinner;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
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

    public void setAttribute(View view, DOMAttr attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        CharSequence[] entries = getTextArray(attr.getValue(),ctx);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(ctx, android.R.layout.simple_list_item_1, entries);
        if (view instanceof ListView)
            ((ListView)view).setAdapter(adapter);
        else if (view instanceof AbsSpinner)
            ((AbsSpinner)view).setAdapter(adapter);
    }

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        setAttribute(view,"0",xmlInflaterLayout,ctx,null,null);
    }

}
