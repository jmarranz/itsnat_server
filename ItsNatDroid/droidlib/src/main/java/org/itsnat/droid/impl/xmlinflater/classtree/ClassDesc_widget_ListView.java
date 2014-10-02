package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodDimensionInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_ListViewAndAbsSpinner_entries;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_ListView extends ClassDescViewBased
{
    public ClassDesc_widget_ListView(ClassDescViewMgr classMgr,ClassDesc_widget_AbsListView parentClass)
    {
        super(classMgr,"android.widget.ListView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecMethodDrawable(this,"divider",null)); // Android tiene un drawable por defecto
        addAttrDesc(new AttrDescReflecMethodDimensionInt(this,"dividerHeight",0f));
        addAttrDesc(new AttrDesc_widget_ListViewAndAbsSpinner_entries(this));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"footerDividersEnabled",true));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"headerDividersEnabled",true));
    }
}

