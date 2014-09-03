package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescGravityUtil;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecDimensionInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecDrawable;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecFloat;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMultipleName;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_widget_LinearLayout_orientation;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_widget_LinearLayout_showDividers;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_LinearLayout extends ClassDescViewBased
{
    public ClassDesc_widget_LinearLayout(ClassDesc_widget_ViewGroup parentClass)
    {
        super("android.widget.LinearLayout",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecBoolean(this,"baselineAligned",true));
        addAttrDesc(new AttrDescReflecInt(this,"baselineAlignedChildIndex",-1));
        addAttrDesc(new AttrDescReflecDrawable(this,"divider","setDividerDrawable"));
        // showDividers y dividerPadding atributos los he descubierto por casualidad en StackOverflow y resulta que son atributos NO documentados de LinearLayout (se ven en el código fuente)
        addAttrDesc(new AttrDesc_widget_LinearLayout_showDividers(this));  // showDividers
        addAttrDesc(new AttrDescReflecDimensionInt(this,"dividerPadding",0f));
        addAttrDesc(new AttrDescReflecMultipleName(this,"gravity", AttrDescGravityUtil.valueMap,""));
        addAttrDesc(new AttrDescReflecBoolean(this,"measureWithLargestChild","setMeasureWithLargestChildEnabled",false));
        addAttrDesc(new AttrDesc_widget_LinearLayout_orientation(this)); // "orientation"
        addAttrDesc(new AttrDescReflecFloat(this,"weightSum",-1.0f));
    }
}

