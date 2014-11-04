package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodDimensionInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodMultipleName;
import org.itsnat.droid.impl.xmlinflater.layout.attr.GravityUtil;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDesc_widget_GridView_stretchMode;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_GridView extends ClassDescViewBased
{
    public ClassDesc_widget_GridView(ClassDescViewMgr classMgr,ClassDesc_widget_AbsListView parentClass)
    {
        super(classMgr,"android.widget.GridView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecMethodDimensionInt(this,"columnWidth",null));
        addAttrDesc(new AttrDescReflecMethodMultipleName(this,"gravity", GravityUtil.valueMap,"left"));
        addAttrDesc(new AttrDescReflecMethodDimensionInt(this,"horizontalSpacing",0f));
        addAttrDesc(new AttrDescReflecMethodInt(this,"numColumns",1));
        addAttrDesc(new AttrDesc_widget_GridView_stretchMode(this));
        addAttrDesc(new AttrDescReflecMethodDimensionInt(this,"verticalSpacing",0f));
    }
}

