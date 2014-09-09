package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_widget_ListView_entries;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_ListView extends ClassDescViewBased
{
    public ClassDesc_widget_ListView(ClassDesc_widget_AbsListView parentClass)
    {
        super("android.widget.ListView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDesc_widget_ListView_entries(this));

    }
}

