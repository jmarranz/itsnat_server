package org.itsnat.droid.impl.xmlinflater.classtree;

import android.widget.RelativeLayout;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecCharSequence;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecDimensionFloat;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecDimensionInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecDrawable;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecFloat;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecId;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecObject;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_drawingCacheQuality;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_id;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layerType;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_alignWithParentIfMissing;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_column;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_columnSpan;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_gravity;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_height;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_margin;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_marginBottom;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_marginLeft;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_marginRight;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_marginTop;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_rellayout_byBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_rellayout_byId;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_row;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_rowSpan;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_weight;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_width;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_onClick;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_padding;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_requiresFadingEdge;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_scrollbarAlwaysDrawHorizontalTrack;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_scrollbarAlwaysDrawVerticalTrack;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_scrollbarStyle;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_scrollbarThumbHorizontal;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_scrollbarThumbVertical;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_scrollbarTrackHorizontal;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_scrollbarTrackVertical;
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

        // Atributos analizados para Android 4.4 (API Level: 19) pero teniendo en cuenta que sólo soportamos Level 15 (Android 4.0.3)

        // android:accessibilityLiveRegion es Level 19
        addAttrDesc(new AttrDescReflecFloat(this,"alpha",1f));
        addAttrDesc(new AttrDescReflecDrawable(this,"background","setBackgroundDrawable"));  // setBackground() es desde Android 4.1
        addAttrDesc(new AttrDescReflecBoolean(this,"clickable",true));
        addAttrDesc(new AttrDescReflecCharSequence(this,"contentDescription"));
        addAttrDesc(new AttrDesc_view_View_drawingCacheQuality(this)); // drawingCacheQuality
        addAttrDesc(new AttrDescReflecBoolean(this,"duplicateParentState","setDuplicateParentStateEnabled",false)); // Según dice la doc no hace nada este flag a true si el atributo no se define antes de insertar en un ViewGroup
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
        addAttrDesc(new AttrDescReflecBoolean(this,"keepScreenOn",false));
        addAttrDesc(new AttrDesc_view_View_layerType(this)); // layerType
        // android:layoutDirection es Level 17
        addAttrDesc(new AttrDescReflecBoolean(this,"longClickable",false));
        addAttrDesc(new AttrDescReflecDimensionInt(this,"minHeight","setMinimumHeight",null));
        addAttrDesc(new AttrDescReflecDimensionInt(this,"minWidth","setMinimumWidth",null));
        addAttrDesc(new AttrDescReflecId(this,"nextFocusDown","setNextFocusDownId"));
        addAttrDesc(new AttrDescReflecId(this,"nextFocusForward","setNextFocusForwardId"));
        addAttrDesc(new AttrDescReflecId(this,"nextFocusLeft","setNextFocusLeftId"));
        addAttrDesc(new AttrDescReflecId(this,"nextFocusRight","setNextFocusRightId"));
        addAttrDesc(new AttrDescReflecId(this,"nextFocusUp","setNextFocusUpId"));
        addAttrDesc(new AttrDesc_view_View_onClick(this));
        addAttrDesc(new AttrDesc_view_View_padding(this,"padding"));
        addAttrDesc(new AttrDesc_view_View_padding(this,"paddingBottom"));
        // android:paddingEnd es Level 17
        addAttrDesc(new AttrDesc_view_View_padding(this,"paddingLeft"));
        addAttrDesc(new AttrDesc_view_View_padding(this,"paddingRight"));
        // android:paddingStart es Level 17
        addAttrDesc(new AttrDesc_view_View_padding(this,"paddingTop"));
        addAttrDesc(new AttrDesc_view_View_requiresFadingEdge(this)); // requiresFadingEdge
        addAttrDesc(new AttrDescReflecFloat(this,"rotation",0f));
        addAttrDesc(new AttrDescReflecFloat(this,"rotationX",0f));
        addAttrDesc(new AttrDescReflecFloat(this,"rotationY",0f));
        addAttrDesc(new AttrDescReflecBoolean(this,"saveEnabled",true));
        addAttrDesc(new AttrDescReflecFloat(this,"scaleX",1f));
        addAttrDesc(new AttrDescReflecFloat(this,"scaleY",1f));
        addAttrDesc(new AttrDescReflecDimensionInt(this,"scrollX",0f));
        addAttrDesc(new AttrDescReflecDimensionInt(this,"scrollY",0f));
        addAttrDesc(new AttrDesc_view_View_scrollbarAlwaysDrawHorizontalTrack(this));
        addAttrDesc(new AttrDesc_view_View_scrollbarAlwaysDrawVerticalTrack(this));

        // android:scrollbarDefaultDelayBeforeFade es Level 16
        // android:scrollbarFadeDuration es Level 16
        // android:scrollbarSize es Level 16
        addAttrDesc(new AttrDesc_view_View_scrollbarStyle(this)); // scrollbarStyle

        addAttrDesc(new AttrDesc_view_View_scrollbarThumbHorizontal(this));
        addAttrDesc(new AttrDesc_view_View_scrollbarThumbVertical(this));
        addAttrDesc(new AttrDesc_view_View_scrollbarTrackHorizontal(this));
        addAttrDesc(new AttrDesc_view_View_scrollbarTrackVertical(this));

        // android:scrollbars está basado en flags, es difícil de implementar
        addAttrDesc(new AttrDescReflecBoolean(this,"soundEffectsEnabled",true));
        addAttrDesc(new AttrDescReflecObject(this,"tag"));
        // android:textAlignment es Level 17
        // android:textDirection es Level 17
        addAttrDesc(new AttrDescReflecDimensionFloat(this,"transformPivotX","setPivotX",0f));
        addAttrDesc(new AttrDescReflecDimensionFloat(this,"transformPivotY","setPivotY",0f));
        addAttrDesc(new AttrDescReflecDimensionFloat(this,"translationX",0f));
        addAttrDesc(new AttrDescReflecDimensionFloat(this,"translationY",0f));
        addAttrDesc(new AttrDesc_view_View_visibility(this)); // "visibility"

        // Debidos a ViewGroup
        addAttrDesc(new AttrDesc_view_View_layout_width(this));
        addAttrDesc(new AttrDesc_view_View_layout_height(this));


        // Debidos a ViewGroup.MarginLayoutParams
        addAttrDesc(new AttrDesc_view_View_layout_marginBottom(this));
        addAttrDesc(new AttrDesc_view_View_layout_marginLeft(this));
        addAttrDesc(new AttrDesc_view_View_layout_marginTop(this));
        addAttrDesc(new AttrDesc_view_View_layout_marginRight(this));
        addAttrDesc(new AttrDesc_view_View_layout_margin(this));

        // Debidos a LinearLayout.LayoutParams
        addAttrDesc(new AttrDesc_view_View_layout_weight(this));

        // Debidos a LinearLayout.LayoutParams, FrameLayout.LayoutParams y GridLayout.LayoutParams
        addAttrDesc(new AttrDesc_view_View_layout_gravity(this));

        // Debidos a GridLayout.LayoutParams
        addAttrDesc(new AttrDesc_view_View_layout_column(this));
        addAttrDesc(new AttrDesc_view_View_layout_columnSpan(this));
        addAttrDesc(new AttrDesc_view_View_layout_row(this));
        addAttrDesc(new AttrDesc_view_View_layout_rowSpan(this));

        // Debidos a RelativeLayout.LayoutParams

        addAttrDesc(new AttrDesc_view_View_layout_rellayout_byId(this,"layout_above",RelativeLayout.ABOVE));
        addAttrDesc(new AttrDesc_view_View_layout_rellayout_byId(this,"layout_alignBaseline",RelativeLayout.ALIGN_BASELINE));
        addAttrDesc(new AttrDesc_view_View_layout_rellayout_byId(this,"layout_alignBottom",RelativeLayout.ALIGN_BOTTOM));
        addAttrDesc(new AttrDesc_view_View_layout_rellayout_byId(this,"layout_alignEnd",RelativeLayout.ALIGN_END));
        addAttrDesc(new AttrDesc_view_View_layout_rellayout_byId(this,"layout_alignLeft",RelativeLayout.ALIGN_LEFT));
        addAttrDesc(new AttrDesc_view_View_layout_rellayout_byBoolean(this,"layout_alignParentBottom", RelativeLayout.ALIGN_PARENT_BOTTOM));
        addAttrDesc(new AttrDesc_view_View_layout_rellayout_byBoolean(this,"layout_alignParentEnd", RelativeLayout.ALIGN_PARENT_END));
        addAttrDesc(new AttrDesc_view_View_layout_rellayout_byBoolean(this,"layout_alignParentLeft", RelativeLayout.ALIGN_PARENT_LEFT));
        addAttrDesc(new AttrDesc_view_View_layout_rellayout_byBoolean(this,"layout_alignParentRight", RelativeLayout.ALIGN_PARENT_RIGHT));
        addAttrDesc(new AttrDesc_view_View_layout_rellayout_byBoolean(this,"layout_alignParentStart", RelativeLayout.ALIGN_PARENT_START));
        addAttrDesc(new AttrDesc_view_View_layout_rellayout_byBoolean(this,"layout_alignParentTop", RelativeLayout.ALIGN_PARENT_TOP));
        addAttrDesc(new AttrDesc_view_View_layout_rellayout_byId(this,"layout_alignRight",RelativeLayout.ALIGN_RIGHT));
        addAttrDesc(new AttrDesc_view_View_layout_rellayout_byId(this,"layout_alignStart",RelativeLayout.ALIGN_START));
        addAttrDesc(new AttrDesc_view_View_layout_rellayout_byId(this,"layout_alignTop",RelativeLayout.ALIGN_TOP));
        addAttrDesc(new AttrDesc_view_View_layout_alignWithParentIfMissing(this));
        addAttrDesc(new AttrDesc_view_View_layout_rellayout_byId(this,"layout_below",RelativeLayout.BELOW));
        addAttrDesc(new AttrDesc_view_View_layout_rellayout_byBoolean(this,"layout_centerHorizontal",RelativeLayout.CENTER_HORIZONTAL));
        addAttrDesc(new AttrDesc_view_View_layout_rellayout_byBoolean(this,"layout_centerInParent",RelativeLayout.CENTER_IN_PARENT));
        addAttrDesc(new AttrDesc_view_View_layout_rellayout_byBoolean(this,"layout_centerVertical",RelativeLayout.CENTER_VERTICAL));
        addAttrDesc(new AttrDesc_view_View_layout_rellayout_byId(this,"layout_toEndOf",RelativeLayout.END_OF));
        addAttrDesc(new AttrDesc_view_View_layout_rellayout_byId(this,"layout_toLeftOf",RelativeLayout.LEFT_OF));
        addAttrDesc(new AttrDesc_view_View_layout_rellayout_byId(this,"layout_toRightOf",RelativeLayout.RIGHT_OF));
        addAttrDesc(new AttrDesc_view_View_layout_rellayout_byId(this,"layout_toStartOf",RelativeLayout.START_OF));
    }
}
