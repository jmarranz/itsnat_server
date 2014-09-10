package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescGravityUtil;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecDimensionInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMultipleName;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_widget_GridView_stretchMode;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_GridView extends ClassDescViewBased
{
    public ClassDesc_widget_GridView(ClassDesc_widget_AbsListView parentClass)
    {
        super("android.widget.GridView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecDimensionInt(this,"columnWidth",null));
        addAttrDesc(new AttrDescReflecMultipleName(this,"gravity", AttrDescGravityUtil.valueMap,"left"));
        addAttrDesc(new AttrDescReflecDimensionInt(this,"horizontalSpacing",0f));
        addAttrDesc(new AttrDescReflecInt(this,"numColumns",1));
        addAttrDesc(new AttrDesc_widget_GridView_stretchMode(this));
        addAttrDesc(new AttrDescReflecDimensionInt(this,"verticalSpacing",0f));
    }
}

