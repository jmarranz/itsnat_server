package org.itsnat.droid.impl.xmlinflater.classtree;

import android.widget.RelativeLayout;

import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodCharSequence;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodDimensionFloat;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodDimensionInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodFloat;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodId;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodObject;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_drawingCacheQuality;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_fadeScrollbars;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_layerType;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_layout_alignWithParentIfMissing;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_layout_column;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_layout_columnSpan;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_layout_gravity;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_layout_height;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_layout_margin;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_layout_marginBottom;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_layout_marginLeft;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_layout_marginRight;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_layout_marginTop;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_layout_rellayout_byBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_layout_rellayout_byId;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_layout_row;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_layout_rowSpan;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_layout_weight;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_layout_width;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_onClick;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_padding;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_requiresFadingEdge;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_scrollbarAlwaysDrawHorizontalTrack;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_scrollbarAlwaysDrawVerticalTrack;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_scrollbarStyle;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_scrollbarThumbHorizontal;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_scrollbarThumbVertical;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_scrollbarTrackHorizontal;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_scrollbarTrackVertical;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_scrollbars;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_View_visibility;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_view_View extends ClassDescViewBased
{
    public ClassDesc_view_View(ClassDescViewMgr classMgr)
    {
        super(classMgr,"android.view.View",null);
    }

    protected void init()
    {
        super.init();

        // Atributos analizados para Android 4.4 (API Level: 19) pero teniendo en cuenta que sólo soportamos Level 15 (Android 4.0.3)

        // android:accessibilityLiveRegion es Level 19
        addAttrDesc(new AttrDescReflecMethodFloat(this,"alpha",1f));
        addAttrDesc(new AttrDescReflecMethodDrawable(this,"background","setBackgroundDrawable","@null"));  // setBackground() es desde Android 4.1
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"clickable",true));
        addAttrDesc(new AttrDescReflecMethodCharSequence(this,"contentDescription",""));
        addAttrDesc(new AttrDesc_view_View_drawingCacheQuality(this)); // drawingCacheQuality
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"duplicateParentState","setDuplicateParentStateEnabled",false)); // Según dice la doc no hace nada este flag a true si el atributo no se define antes de insertar en un ViewGroup
        addAttrDesc(new AttrDesc_view_View_fadeScrollbars(this));
        addAttrDesc(new AttrDescReflecMethodDimensionInt(this,"fadingEdgeLength",null));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"filterTouchesWhenObscured",false));
        // android:fitsSystemWindows es Level 16
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"focusable",false));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"focusableInTouchMode",false));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"hapticFeedbackEnabled",true));
        addAttrDesc(new AttrDescReflecMethodId(this,"id"));


        // android:importantForAccessibility es Level 16
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"isScrollContainer","setScrollContainer",false)); // No estoy seguro de si el valor por defecto es false, dependerá seguramente del componente, isScrollContainer() se define en un Level > 15
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"keepScreenOn",false));
        addAttrDesc(new AttrDesc_view_View_layerType(this)); // layerType
        // android:layoutDirection es Level 17
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"longClickable",false));
        addAttrDesc(new AttrDescReflecMethodDimensionInt(this,"minHeight","setMinimumHeight",null));
        addAttrDesc(new AttrDescReflecMethodDimensionInt(this,"minWidth","setMinimumWidth",null));
        addAttrDesc(new AttrDescReflecMethodId(this,"nextFocusDown","setNextFocusDownId"));
        addAttrDesc(new AttrDescReflecMethodId(this,"nextFocusForward","setNextFocusForwardId"));
        addAttrDesc(new AttrDescReflecMethodId(this,"nextFocusLeft","setNextFocusLeftId"));
        addAttrDesc(new AttrDescReflecMethodId(this,"nextFocusRight","setNextFocusRightId"));
        addAttrDesc(new AttrDescReflecMethodId(this,"nextFocusUp","setNextFocusUpId"));
        addAttrDesc(new AttrDesc_view_View_onClick(this));
        addAttrDesc(new AttrDesc_view_View_padding(this,"padding"));
        addAttrDesc(new AttrDesc_view_View_padding(this,"paddingBottom"));
        // android:paddingEnd es Level 17
        addAttrDesc(new AttrDesc_view_View_padding(this,"paddingLeft"));
        addAttrDesc(new AttrDesc_view_View_padding(this,"paddingRight"));
        // android:paddingStart es Level 17
        addAttrDesc(new AttrDesc_view_View_padding(this,"paddingTop"));
        addAttrDesc(new AttrDesc_view_View_requiresFadingEdge(this)); // requiresFadingEdge
        addAttrDesc(new AttrDescReflecMethodFloat(this,"rotation",0f));
        addAttrDesc(new AttrDescReflecMethodFloat(this,"rotationX",0f));
        addAttrDesc(new AttrDescReflecMethodFloat(this,"rotationY",0f));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"saveEnabled",true));
        addAttrDesc(new AttrDescReflecMethodFloat(this,"scaleX",1f));
        addAttrDesc(new AttrDescReflecMethodFloat(this,"scaleY",1f));
        addAttrDesc(new AttrDescReflecMethodDimensionInt(this,"scrollX",0f));
        addAttrDesc(new AttrDescReflecMethodDimensionInt(this,"scrollY",0f));
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

        addAttrDesc(new AttrDesc_view_View_scrollbars(this));

        // android:scrollbars está basado en flags, es difícil de implementar
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"soundEffectsEnabled",true));
        addAttrDesc(new AttrDescReflecMethodObject(this,"tag"));
        // android:textAlignment es Level 17
        // android:textDirection es Level 17
        addAttrDesc(new AttrDescReflecMethodDimensionFloat(this,"transformPivotX","setPivotX",0f));
        addAttrDesc(new AttrDescReflecMethodDimensionFloat(this,"transformPivotY","setPivotY",0f));
        addAttrDesc(new AttrDescReflecMethodDimensionFloat(this,"translationX",0f));
        addAttrDesc(new AttrDescReflecMethodDimensionFloat(this,"translationY",0f));
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
