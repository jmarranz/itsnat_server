package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import android.view.ViewGroup;
import android.widget.ListPopupWindow;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecFieldMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecFieldSetId;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodDimensionInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodDimensionWithNameInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodId;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_AutoCompleteTextView_completionHint;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_AutoCompleteTextView extends ClassDescViewBased
{
    public ClassDescView_widget_AutoCompleteTextView(ClassDescViewMgr classMgr, ClassDescViewBased parentClass)
    {
        super(classMgr,"android.widget.AutoCompleteTextView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescView_widget_AutoCompleteTextView_completionHint(this));
        addAttrDesc(new AttrDescViewReflecFieldSetId(this,"completionHintView","mHintResource",null)); // Android tiene un recurso por defecto
        addAttrDesc(new AttrDescViewReflecMethodInt(this,"completionThreshold","setThreshold",2));
        addAttrDesc(new AttrDescViewReflecMethodId(this,"dropDownAnchor",-1));
        addAttrDesc(new AttrDescViewReflecMethodDimensionWithNameInt(this,"dropDownHeight",(float)ViewGroup.LayoutParams.WRAP_CONTENT));
        addAttrDesc(new AttrDescViewReflecMethodDimensionInt(this,"dropDownHorizontalOffset",0.0f));
        addAttrDesc(new AttrDescViewReflecFieldMethodDrawable(this,"dropDownSelector","mPopup","setListSelector",ListPopupWindow.class,null)); // Hay un background por defecto de Android en ListPopupWindow aunque parece que por defecto se pone un null si no hay atributo
        addAttrDesc(new AttrDescViewReflecMethodDimensionInt(this,"dropDownVerticalOffset",0.0f));
        addAttrDesc(new AttrDescViewReflecMethodDimensionWithNameInt(this,"dropDownWidth",(float)ViewGroup.LayoutParams.WRAP_CONTENT));
    }
}

