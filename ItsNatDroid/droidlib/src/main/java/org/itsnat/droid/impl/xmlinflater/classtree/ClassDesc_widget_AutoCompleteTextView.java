package org.itsnat.droid.impl.xmlinflater.classtree;

import android.view.ViewGroup;
import android.widget.ListPopupWindow;

import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecFieldMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecFieldSetId;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodDimensionInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodDimensionWithNameInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodId;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodInt;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_AutoCompleteTextView_completionHint;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_AutoCompleteTextView extends ClassDescViewBased
{
    public ClassDesc_widget_AutoCompleteTextView(ClassDescViewMgr classMgr, ClassDescViewBased parentClass)
    {
        super(classMgr,"android.widget.AutoCompleteTextView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDesc_widget_AutoCompleteTextView_completionHint(this));
        addAttrDesc(new AttrDescReflecFieldSetId(this,"completionHintView","mHintResource",null)); // Android tiene un recurso por defecto
        addAttrDesc(new AttrDescReflecMethodInt(this,"completionThreshold","setThreshold",2));
        addAttrDesc(new AttrDescReflecMethodId(this,"dropDownAnchor",-1));
        addAttrDesc(new AttrDescReflecMethodDimensionWithNameInt(this,"dropDownHeight",(float)ViewGroup.LayoutParams.WRAP_CONTENT));
        addAttrDesc(new AttrDescReflecMethodDimensionInt(this,"dropDownHorizontalOffset",0.0f));
        addAttrDesc(new AttrDescReflecFieldMethodDrawable(this,"dropDownSelector","mPopup","setListSelector",ListPopupWindow.class,null)); // Hay un background por defecto de Android en ListPopupWindow aunque parece que por defecto se pone un null si no hay atributo
        addAttrDesc(new AttrDescReflecMethodDimensionInt(this,"dropDownVerticalOffset",0.0f));
        addAttrDesc(new AttrDescReflecMethodDimensionWithNameInt(this,"dropDownWidth",(float)ViewGroup.LayoutParams.WRAP_CONTENT));
    }
}

