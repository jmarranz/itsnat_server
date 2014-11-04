package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_TableLayout_shrinkColumns;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_TableLayout_collapseColumns;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_TableLayout_stretchColumns;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_TableLayout extends ClassDescViewBased
{
    public ClassDescView_widget_TableLayout(ClassDescViewMgr classMgr, ClassDescView_widget_LinearLayout parentClass)
    {
        super(classMgr,"android.widget.TableLayout",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescView_widget_TableLayout_collapseColumns(this));
        addAttrDesc(new AttrDescView_widget_TableLayout_shrinkColumns(this));
        addAttrDesc(new AttrDescView_widget_TableLayout_stretchColumns(this));

    }
}

