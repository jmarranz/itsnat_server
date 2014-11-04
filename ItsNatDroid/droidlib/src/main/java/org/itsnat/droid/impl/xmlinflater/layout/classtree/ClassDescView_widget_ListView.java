package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodDimensionInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_ListViewAndAbsSpinner_entries;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_ListView extends ClassDescViewBased
{
    public ClassDescView_widget_ListView(ClassDescViewMgr classMgr,ClassDescView_widget_AbsListView parentClass)
    {
        super(classMgr,"android.widget.ListView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescViewReflecMethodDrawable(this,"divider",null)); // Android tiene un drawable por defecto
        addAttrDesc(new AttrDescViewReflecMethodDimensionInt(this,"dividerHeight",0f));
        addAttrDesc(new AttrDescView_widget_ListViewAndAbsSpinner_entries(this));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"footerDividersEnabled",true));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"headerDividersEnabled",true));
    }
}

