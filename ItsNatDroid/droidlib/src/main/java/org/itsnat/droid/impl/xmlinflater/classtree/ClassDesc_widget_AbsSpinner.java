package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_ListViewAndAbsSpinner_entries;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_AbsSpinner extends ClassDescViewBased
{
    public ClassDesc_widget_AbsSpinner(ClassDescViewBased parentClass)
    {
        super("android.widget.AbsSpinner",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDesc_widget_ListViewAndAbsSpinner_entries(this));
    }
}

