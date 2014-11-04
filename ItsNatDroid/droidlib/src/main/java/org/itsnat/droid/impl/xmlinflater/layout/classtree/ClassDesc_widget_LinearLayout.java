package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodDimensionInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodFloat;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodMultipleName;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodSingleName;
import org.itsnat.droid.impl.xmlinflater.layout.attr.GravityUtil;
import org.itsnat.droid.impl.xmlinflater.layout.attr.OrientationUtil;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDesc_widget_LinearLayout_baselineAlignedChildIndex;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDesc_widget_LinearLayout_showDividers;

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

