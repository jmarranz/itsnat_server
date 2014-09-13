package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescGravityUtil;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecCharSequence;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecDimensionInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMultipleName;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_widget_Spinner_dropDownWidth;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_widget_Spinner_popupBackground;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_Spinner extends ClassDescViewBased
{
    public ClassDesc_widget_Spinner(ClassDesc_widget_AbsSpinner parentClass)
    {
        super("android.widget.Spinner",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecDimensionInt(this,"dropDownHorizontalOffset",0f));
        // Es de traca pero android:dropDownSelector NO tiene implementación
        addAttrDesc(new AttrDescReflecDimensionInt(this,"dropDownVerticalOffset",0f));
        addAttrDesc(new AttrDescReflecDimensionInt(this,"dropDownVerticalOffset",0f));
        addAttrDesc(new AttrDesc_widget_Spinner_dropDownWidth(this));
        addAttrDesc(new AttrDescReflecMultipleName(this,"gravity", AttrDescGravityUtil.valueMap,"center"));
        addAttrDesc(new AttrDesc_widget_Spinner_popupBackground(this));
        addAttrDesc(new AttrDescReflecCharSequence(this,"prompt")); // En un layout compilado no se admiten literales, aquí sí, no es importante y es más flexible aún
        // MAL: addAttrDesc(new AttrDesc_widget_Spinner_spinnerMode(this));

    }
}

