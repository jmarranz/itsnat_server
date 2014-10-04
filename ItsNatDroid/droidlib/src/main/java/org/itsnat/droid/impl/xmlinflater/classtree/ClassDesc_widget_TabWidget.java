package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodDrawable;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_TabWidget extends ClassDescViewBased
{
    public ClassDesc_widget_TabWidget(ClassDescViewMgr classMgr, ClassDesc_widget_LinearLayout parentClass)
    {
        super(classMgr,"android.widget.TabWidget",parentClass);
    }

    protected void init()
    {
        super.init();

        // android:divider es un atributo definido en LinearLayout
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"tabStripEnabled","setStripEnabled",true));
        addAttrDesc(new AttrDescReflecMethodDrawable(this,"tabStripLeft","setLeftStripDrawable",null)); // existe un Drawable por defecto
        addAttrDesc(new AttrDescReflecMethodDrawable(this,"tabStripRight","setRightStripDrawable",null));


        /*
        addAttrDesc(new AttrDesc_widget_Spinner_dropDownHorizontalOffset(this));
        // Es de traca pero android:dropDownSelector NO tiene implementación alguna en el código fuente
        addAttrDesc(new AttrDesc_widget_Spinner_dropDownVerticalOffset(this));
        addAttrDesc(new AttrDescReflecFieldSetDimensionWithNameInt(this,"dropDownWidth","mDropDownWidth","wrap_content"));
        addAttrDesc(new AttrDescReflecMethodMultipleName(this,"gravity", GravityUtil.valueMap,"center"));
        addAttrDesc(new AttrDesc_widget_Spinner_popupBackground(this));
        addAttrDesc(new AttrDescReflecMethodCharSequence(this,"prompt","")); // En un layout compilado no se admiten literales, aquí sí, no es importante y es más flexible aún
        // android:spinnerMode no es un atributo normal, se pasa por el constructor del objeto Spinner
        */
    }
}

