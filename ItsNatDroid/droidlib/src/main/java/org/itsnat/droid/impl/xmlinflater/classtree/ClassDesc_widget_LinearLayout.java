package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodDimensionInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodFloat;
import org.itsnat.droid.impl.xmlinflater.attr.GravityUtil;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMultipleName;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_LinearLayout_baselineAlignedChildIndex;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_LinearLayout_orientation;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_LinearLayout_showDividers;

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

        addAttrDesc(new AttrDescReflecMethodBoolean(this,"baselineAligned",true));
        addAttrDesc(new AttrDesc_widget_LinearLayout_baselineAlignedChildIndex(this));
        addAttrDesc(new AttrDescReflecMethodDrawable(this,"divider","setDividerDrawable",null)); // Hay un drawable por defecto de Android
        // showDividers y dividerPadding atributos los he descubierto por casualidad en StackOverflow y resulta que son atributos NO documentados de LinearLayout (se ven en el código fuente)
        addAttrDesc(new AttrDesc_widget_LinearLayout_showDividers(this));  // showDividers
        addAttrDesc(new AttrDescReflecMethodDimensionInt(this,"dividerPadding",0f));
        addAttrDesc(new AttrDescReflecMultipleName(this,"gravity", GravityUtil.valueMap,"start|top"));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"measureWithLargestChild","setMeasureWithLargestChildEnabled",false));
        addAttrDesc(new AttrDesc_widget_LinearLayout_orientation(this)); // "orientation"
        addAttrDesc(new AttrDescReflecMethodFloat(this,"weightSum",-1.0f));
    }
}

