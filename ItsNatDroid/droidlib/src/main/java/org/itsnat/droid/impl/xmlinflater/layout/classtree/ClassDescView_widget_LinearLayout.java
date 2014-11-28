package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodDimensionInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodFloat;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodMultipleName;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodSingleName;
import org.itsnat.droid.impl.xmlinflater.GravityUtil;
import org.itsnat.droid.impl.xmlinflater.layout.attr.OrientationUtil;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_LinearLayout_showDividers;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_LinearLayout_baselineAlignedChildIndex;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_LinearLayout extends ClassDescViewBased
{
    public ClassDescView_widget_LinearLayout(ClassDescViewMgr classMgr,ClassDescView_view_ViewGroup parentClass)
    {
        super(classMgr,"android.widget.LinearLayout",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"baselineAligned",true));
        addAttrDesc(new AttrDescView_widget_LinearLayout_baselineAlignedChildIndex(this));
        addAttrDesc(new AttrDescViewReflecMethodDrawable(this,"divider","setDividerDrawable",null)); // Hay un drawable por defecto de Android
        // showDividers y dividerPadding atributos los he descubierto por casualidad en StackOverflow y resulta que son atributos NO documentados de LinearLayout (se ven en el código fuente)
        addAttrDesc(new AttrDescView_widget_LinearLayout_showDividers(this));  // showDividers
        addAttrDesc(new AttrDescViewReflecMethodDimensionInt(this,"dividerPadding",0f));
        addAttrDesc(new AttrDescViewReflecMethodMultipleName(this,"gravity", GravityUtil.valueMap,"start|top"));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"measureWithLargestChild","setMeasureWithLargestChildEnabled",false));
        addAttrDesc(new AttrDescViewReflecMethodSingleName(this,"orientation",int.class, OrientationUtil.valueMap,"horizontal"));
        addAttrDesc(new AttrDescViewReflecMethodFloat(this,"weightSum",-1.0f));
    }
}

