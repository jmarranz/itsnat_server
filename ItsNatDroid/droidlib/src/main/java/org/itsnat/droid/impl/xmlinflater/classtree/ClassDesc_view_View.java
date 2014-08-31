package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecCharSequence;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecDimensionInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecDrawable;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecFloat;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_drawingCacheQuality;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_id;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_alignParentTop;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_below;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_gravity;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_height;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_margin;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_marginBottom;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_marginLeft;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_marginRight;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_marginTop;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_weight;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_width;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_padding;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_requiresFadingEdge;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_visibility;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_view_View extends ClassDescViewBased
{
    public ClassDesc_view_View()
    {
        super("android.view.View",null);
    }

    protected void init()
    {
        super.init();

        // android:accessibilityLiveRegion es Level 19
        addAttrDesc(new AttrDescReflecFloat(this,"alpha",1f));
        addAttrDesc(new AttrDescReflecDrawable(this,"background","setBackgroundDrawable"));  // setBackground() es desde Android 4.1
        addAttrDesc(new AttrDescReflecBoolean(this,"clickable",true));
        addAttrDesc(new AttrDescReflecCharSequence(this,"contentDescription"));
        addAttrDesc(new AttrDesc_view_View_drawingCacheQuality(this)); // drawingCacheQuality
        // android:duplicateParentState no tiene método set nativo asociado
        addAttrDesc(new AttrDescReflecBoolean(this,"fadeScrollbars","setScrollbarFadingEnabled",true));
        addAttrDesc(new AttrDescReflecDimensionInt(this,"fadingEdgeLength",null));
        addAttrDesc(new AttrDescReflecBoolean(this,"filterTouchesWhenObscured",false));
        // android:fitsSystemWindows es Level 16
        addAttrDesc(new AttrDescReflecBoolean(this,"focusable",false));
        addAttrDesc(new AttrDescReflecBoolean(this,"focusableInTouchMode",false));
        addAttrDesc(new AttrDescReflecBoolean(this,"hapticFeedbackEnabled",true));
        addAttrDesc(new AttrDesc_view_View_id(this)); // "id"
        // android:importantForAccessibility es Level 16
        addAttrDesc(new AttrDescReflecBoolean(this,"isScrollContainer","setScrollContainer",false)); // No estoy seguro de si el valor por defecto es false, dependerá seguramente del componente, isScrollContainer() se define en un Level > 15



        addAttrDesc(new AttrDescReflecFloat(this,"rotation",0f));
        addAttrDesc(new AttrDescReflecFloat(this,"rotationX",0f));
        addAttrDesc(new AttrDescReflecFloat(this,"rotationY",0f));




        addAttrDesc(new AttrDesc_view_View_requiresFadingEdge(this)); // requiresFadingEdge






        addAttrDesc(new AttrDesc_view_View_padding(this,"padding"));
        addAttrDesc(new AttrDesc_view_View_padding(this,"paddingLeft"));
        addAttrDesc(new AttrDesc_view_View_padding(this,"paddingTop"));
        addAttrDesc(new AttrDesc_view_View_padding(this,"paddingRight"));
        addAttrDesc(new AttrDesc_view_View_padding(this,"paddingBottom"));
        addAttrDesc(new AttrDesc_view_View_visibility(this)); // "visibility"

        // Debidos a ViewGroup
        addAttrDesc(new AttrDesc_view_View_layout_width(this));
        addAttrDesc(new AttrDesc_view_View_layout_height(this));

        // Debidos a LinearLayout
        addAttrDesc(new AttrDesc_view_View_layout_weight(this));

        // Debidos a views padre con soporte de ViewGroup.MarginLayoutParams
        addAttrDesc(new AttrDesc_view_View_layout_marginBottom(this));
        addAttrDesc(new AttrDesc_view_View_layout_marginLeft(this));
        addAttrDesc(new AttrDesc_view_View_layout_marginTop(this));
        addAttrDesc(new AttrDesc_view_View_layout_marginRight(this));
        addAttrDesc(new AttrDesc_view_View_layout_margin(this));


        // Debidos a LinearLayout y FrameLayout
        addAttrDesc(new AttrDesc_view_View_layout_gravity(this));


        // Debidos a RelativeLayout
        addAttrDesc(new AttrDesc_view_View_layout_alignParentTop(this));
        addAttrDesc(new AttrDesc_view_View_layout_below(this));
    }
}
