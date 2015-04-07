package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import android.widget.RelativeLayout;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodCharSequence;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodDimensionFloat;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodDimensionInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodFloat;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodId;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodObject;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_drawingCacheQuality;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_layout_alignWithParentIfMissing;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_layout_gravity;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_layout_margin;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_layout_marginBottom;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_layout_marginRight;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_layout_marginTop;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_layout_rellayout_byBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_layout_weight;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_padding;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_scrollbarThumbVertical;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_visibility;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_fadeScrollbars;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_layerType;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_layout_column;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_layout_columnSpan;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_layout_height;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_layout_marginLeft;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_layout_rellayout_byId;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_layout_row;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_layout_rowSpan;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_layout_span;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_layout_width;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_onClick;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_requiresFadingEdge;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_scrollbarAlwaysDrawHorizontalTrack;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_scrollbarAlwaysDrawVerticalTrack;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_scrollbarStyle;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_scrollbarThumbHorizontal;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_scrollbarTrackHorizontal;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_scrollbarTrackVertical;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_View_scrollbars;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_view_View extends ClassDescViewBased
{
    public ClassDescView_view_View(ClassDescViewMgr classMgr)
    {
        super(classMgr,"android.view.View",null);
    }

    @Override
    protected void init()
    {
        super.init();

        // Atributos analizados para Android 4.4 (API Level: 19) pero teniendo en cuenta que sólo soportamos Level 15 (Android 4.0.3)

        // android:accessibilityLiveRegion es Level 19
        addAttrDesc(new AttrDescViewReflecMethodFloat(this,"alpha",1f));
        addAttrDesc(new AttrDescViewReflecMethodDrawable(this,"background","setBackgroundDrawable","@null"));  // setBackground() es desde Android 4.1
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"clickable",true));
        addAttrDesc(new AttrDescViewReflecMethodCharSequence(this,"contentDescription",""));
        addAttrDesc(new AttrDescView_view_View_drawingCacheQuality(this)); // drawingCacheQuality
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"duplicateParentState","setDuplicateParentStateEnabled",false)); // Según dice la doc no hace nada este flag a true si el atributo no se define antes de insertar en un ViewGroup
        addAttrDesc(new AttrDescView_view_View_fadeScrollbars(this));
        addAttrDesc(new AttrDescViewReflecMethodDimensionInt(this,"fadingEdgeLength",null));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"filterTouchesWhenObscured",false));
        // android:fitsSystemWindows es Level 16
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"focusable",false));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"focusableInTouchMode",false));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"hapticFeedbackEnabled",true));
        addAttrDesc(new AttrDescViewReflecMethodId(this,"id",-1));


        // android:importantForAccessibility es Level 16
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"isScrollContainer","setScrollContainer",false)); // No estoy seguro de si el valor por defecto es false, dependerá seguramente del componente, isScrollContainer() se define en un Level > 15
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"keepScreenOn",false));
        addAttrDesc(new AttrDescView_view_View_layerType(this)); // layerType
        // android:layoutDirection es Level 17
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"longClickable",false));
        addAttrDesc(new AttrDescViewReflecMethodDimensionInt(this,"minHeight","setMinimumHeight",null));
        addAttrDesc(new AttrDescViewReflecMethodDimensionInt(this,"minWidth","setMinimumWidth",null));
        addAttrDesc(new AttrDescViewReflecMethodId(this,"nextFocusDown","setNextFocusDownId",-1));
        addAttrDesc(new AttrDescViewReflecMethodId(this,"nextFocusForward","setNextFocusForwardId",-1));
        addAttrDesc(new AttrDescViewReflecMethodId(this,"nextFocusLeft","setNextFocusLeftId",-1));
        addAttrDesc(new AttrDescViewReflecMethodId(this,"nextFocusRight","setNextFocusRightId",-1));
        addAttrDesc(new AttrDescViewReflecMethodId(this,"nextFocusUp","setNextFocusUpId",-1));
        addAttrDesc(new AttrDescView_view_View_onClick(this));
        addAttrDesc(new AttrDescView_view_View_padding(this,"padding"));
        addAttrDesc(new AttrDescView_view_View_padding(this,"paddingBottom"));
        // android:paddingEnd es Level 17
        addAttrDesc(new AttrDescView_view_View_padding(this,"paddingLeft"));
        addAttrDesc(new AttrDescView_view_View_padding(this,"paddingRight"));
        // android:paddingStart es Level 17
        addAttrDesc(new AttrDescView_view_View_padding(this,"paddingTop"));
        addAttrDesc(new AttrDescView_view_View_requiresFadingEdge(this)); // requiresFadingEdge
        addAttrDesc(new AttrDescViewReflecMethodFloat(this,"rotation",0f));
        addAttrDesc(new AttrDescViewReflecMethodFloat(this,"rotationX",0f));
        addAttrDesc(new AttrDescViewReflecMethodFloat(this,"rotationY",0f));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"saveEnabled",true));
        addAttrDesc(new AttrDescViewReflecMethodFloat(this,"scaleX",1f));
        addAttrDesc(new AttrDescViewReflecMethodFloat(this,"scaleY",1f));
        addAttrDesc(new AttrDescViewReflecMethodDimensionInt(this,"scrollX",0f));
        addAttrDesc(new AttrDescViewReflecMethodDimensionInt(this,"scrollY",0f));
        addAttrDesc(new AttrDescView_view_View_scrollbarAlwaysDrawHorizontalTrack(this));
        addAttrDesc(new AttrDescView_view_View_scrollbarAlwaysDrawVerticalTrack(this));

        // android:scrollbarDefaultDelayBeforeFade es Level 16
        // android:scrollbarFadeDuration es Level 16
        // android:scrollbarSize es Level 16
        addAttrDesc(new AttrDescView_view_View_scrollbarStyle(this)); // scrollbarStyle

        addAttrDesc(new AttrDescView_view_View_scrollbarThumbHorizontal(this));
        addAttrDesc(new AttrDescView_view_View_scrollbarThumbVertical(this));
        addAttrDesc(new AttrDescView_view_View_scrollbarTrackHorizontal(this));
        addAttrDesc(new AttrDescView_view_View_scrollbarTrackVertical(this));

        addAttrDesc(new AttrDescView_view_View_scrollbars(this));

        // android:scrollbars está basado en flags, es difícil de implementar
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"soundEffectsEnabled",true));
        addAttrDesc(new AttrDescViewReflecMethodObject(this,"tag"));
        // android:textAlignment es Level 17
        // android:textDirection es Level 17
        addAttrDesc(new AttrDescViewReflecMethodDimensionFloat(this,"transformPivotX","setPivotX",0f));
        addAttrDesc(new AttrDescViewReflecMethodDimensionFloat(this,"transformPivotY","setPivotY",0f));
        addAttrDesc(new AttrDescViewReflecMethodDimensionFloat(this,"translationX",0f));
        addAttrDesc(new AttrDescViewReflecMethodDimensionFloat(this,"translationY",0f));
        addAttrDesc(new AttrDescView_view_View_visibility(this)); // "visibility"

        // Debidos a ViewGroup.LayoutParams
        addAttrDesc(new AttrDescView_view_View_layout_width(this));
        addAttrDesc(new AttrDescView_view_View_layout_height(this));


        // Debidos a ViewGroup.MarginLayoutParams
        addAttrDesc(new AttrDescView_view_View_layout_marginBottom(this));
        addAttrDesc(new AttrDescView_view_View_layout_marginLeft(this));
        addAttrDesc(new AttrDescView_view_View_layout_marginTop(this));
        addAttrDesc(new AttrDescView_view_View_layout_marginRight(this));
        addAttrDesc(new AttrDescView_view_View_layout_margin(this));

        // Debidos a LinearLayout.LayoutParams
        addAttrDesc(new AttrDescView_view_View_layout_weight(this));

        // Debidos a LinearLayout.LayoutParams, FrameLayout.LayoutParams y GridLayout.LayoutParams
        addAttrDesc(new AttrDescView_view_View_layout_gravity(this));

        // Debidos a GridLayout.LayoutParams
        addAttrDesc(new AttrDescView_view_View_layout_column(this));
        addAttrDesc(new AttrDescView_view_View_layout_columnSpan(this));
        addAttrDesc(new AttrDescView_view_View_layout_row(this));
        addAttrDesc(new AttrDescView_view_View_layout_rowSpan(this));

        // Debidos a RelativeLayout.LayoutParams

        addAttrDesc(new AttrDescView_view_View_layout_rellayout_byId(this,"layout_above",RelativeLayout.ABOVE));
        addAttrDesc(new AttrDescView_view_View_layout_rellayout_byId(this,"layout_alignBaseline",RelativeLayout.ALIGN_BASELINE));
        addAttrDesc(new AttrDescView_view_View_layout_rellayout_byId(this,"layout_alignBottom",RelativeLayout.ALIGN_BOTTOM));
        //addAttrDesc(new AttrDescView_view_View_layout_rellayout_byId(this,"layout_alignEnd",RelativeLayout.ALIGN_END)); // API 17
        addAttrDesc(new AttrDescView_view_View_layout_rellayout_byId(this,"layout_alignLeft",RelativeLayout.ALIGN_LEFT));
        addAttrDesc(new AttrDescView_view_View_layout_rellayout_byBoolean(this,"layout_alignParentBottom", RelativeLayout.ALIGN_PARENT_BOTTOM));
        //addAttrDesc(new AttrDescView_view_View_layout_rellayout_byBoolean(this,"layout_alignParentEnd", RelativeLayout.ALIGN_PARENT_END)); // API 17
        addAttrDesc(new AttrDescView_view_View_layout_rellayout_byBoolean(this,"layout_alignParentLeft", RelativeLayout.ALIGN_PARENT_LEFT));
        addAttrDesc(new AttrDescView_view_View_layout_rellayout_byBoolean(this,"layout_alignParentRight", RelativeLayout.ALIGN_PARENT_RIGHT));
        //addAttrDesc(new AttrDescView_view_View_layout_rellayout_byBoolean(this,"layout_alignParentStart", RelativeLayout.ALIGN_PARENT_START)); // API 17
        addAttrDesc(new AttrDescView_view_View_layout_rellayout_byBoolean(this,"layout_alignParentTop", RelativeLayout.ALIGN_PARENT_TOP));
        addAttrDesc(new AttrDescView_view_View_layout_rellayout_byId(this,"layout_alignRight",RelativeLayout.ALIGN_RIGHT));
        //addAttrDesc(new AttrDescView_view_View_layout_rellayout_byId(this,"layout_alignStart",RelativeLayout.ALIGN_START)); // API 17
        addAttrDesc(new AttrDescView_view_View_layout_rellayout_byId(this,"layout_alignTop",RelativeLayout.ALIGN_TOP));
        addAttrDesc(new AttrDescView_view_View_layout_alignWithParentIfMissing(this));
        addAttrDesc(new AttrDescView_view_View_layout_rellayout_byId(this,"layout_below",RelativeLayout.BELOW));
        addAttrDesc(new AttrDescView_view_View_layout_rellayout_byBoolean(this,"layout_centerHorizontal",RelativeLayout.CENTER_HORIZONTAL));
        addAttrDesc(new AttrDescView_view_View_layout_rellayout_byBoolean(this,"layout_centerInParent",RelativeLayout.CENTER_IN_PARENT));
        addAttrDesc(new AttrDescView_view_View_layout_rellayout_byBoolean(this,"layout_centerVertical",RelativeLayout.CENTER_VERTICAL));
        //addAttrDesc(new AttrDescView_view_View_layout_rellayout_byId(this,"layout_toEndOf",RelativeLayout.END_OF));  // API 17
        addAttrDesc(new AttrDescView_view_View_layout_rellayout_byId(this,"layout_toLeftOf",RelativeLayout.LEFT_OF));
        addAttrDesc(new AttrDescView_view_View_layout_rellayout_byId(this,"layout_toRightOf",RelativeLayout.RIGHT_OF));
        //addAttrDesc(new AttrDescView_view_View_layout_rellayout_byId(this,"layout_toStartOf",RelativeLayout.START_OF));  // API 17

        // Debidos a TableRow.LayoutParams
        addAttrDesc(new AttrDescView_view_View_layout_span(this));

    }
}
