package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_TableLayout_collapseColumns;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_TableLayout_shrinkColumns;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_TableLayout_stretchColumns;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_TableLayout extends ClassDescViewBased
{
    public ClassDesc_widget_TableLayout(ClassDescViewMgr classMgr, ClassDesc_widget_LinearLayout parentClass)
    {
        super(classMgr,"android.widget.TableLayout",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDesc_widget_TableLayout_collapseColumns(this));
        addAttrDesc(new AttrDesc_widget_TableLayout_shrinkColumns(this));
        addAttrDesc(new AttrDesc_widget_TableLayout_stretchColumns(this));

    }
}

