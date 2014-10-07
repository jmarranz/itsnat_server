package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodDimensionInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodFloat;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodMultipleName;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodSingleName;
import org.itsnat.droid.impl.xmlinflater.attr.GravityUtil;
import org.itsnat.droid.impl.xmlinflater.attr.OrientationUtil;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_LinearLayout_baselineAlignedChildIndex;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_LinearLayout_showDividers;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_LinearLayout extends ClassDescViewBased
{
    public ClassDesc_widget_LinearLayout(ClassDescViewMgr classMgr,ClassDesc_view_ViewGroup parentClass)
    {
        super(classMgr,"android.widget.LinearLayout",parentClass);
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
        addAttrDesc(new AttrDescReflecMethodMultipleName(this,"gravity", GravityUtil.valueMap,"start|top"));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"measureWithLargestChild","setMeasureWithLargestChildEnabled",false));
        addAttrDesc(new AttrDescReflecMethodSingleName(this,"orientation",int.class, OrientationUtil.valueMap,"horizontal"));
        addAttrDesc(new AttrDescReflecMethodFloat(this,"weightSum",-1.0f));
    }
}

