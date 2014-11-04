package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_ListViewAndAbsSpinner_entries;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_AbsSpinner extends ClassDescViewBased
{
    public ClassDescView_widget_AbsSpinner(ClassDescViewMgr classMgr,ClassDescViewBased parentClass)
    {
        super(classMgr,"android.widget.AbsSpinner",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescView_widget_ListViewAndAbsSpinner_entries(this));
    }
}

