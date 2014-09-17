package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecDimensionInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecDrawable;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_ListViewAndAbsSpinner_entries;

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

        addAttrDesc(new AttrDescReflecDrawable(this,"divider"));
        addAttrDesc(new AttrDescReflecDimensionInt(this,"dividerHeight",0f));
        addAttrDesc(new AttrDesc_widget_ListViewAndAbsSpinner_entries(this));
        addAttrDesc(new AttrDescReflecBoolean(this,"footerDividersEnabled",true));
        addAttrDesc(new AttrDescReflecBoolean(this,"headerDividersEnabled",true));
    }
}

