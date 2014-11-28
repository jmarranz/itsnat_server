package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodDimensionInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodMultipleName;
import org.itsnat.droid.impl.xmlinflater.GravityUtil;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_GridView_stretchMode;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_GridView extends ClassDescViewBased
{
    public ClassDescView_widget_GridView(ClassDescViewMgr classMgr,ClassDescView_widget_AbsListView parentClass)
    {
        super(classMgr,"android.widget.GridView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescViewReflecMethodDimensionInt(this,"columnWidth",null));
        addAttrDesc(new AttrDescViewReflecMethodMultipleName(this,"gravity", GravityUtil.valueMap,"left"));
        addAttrDesc(new AttrDescViewReflecMethodDimensionInt(this,"horizontalSpacing",0f));
        addAttrDesc(new AttrDescViewReflecMethodInt(this,"numColumns",1));
        addAttrDesc(new AttrDescView_widget_GridView_stretchMode(this));
        addAttrDesc(new AttrDescViewReflecMethodDimensionInt(this,"verticalSpacing",0f));
    }
}

