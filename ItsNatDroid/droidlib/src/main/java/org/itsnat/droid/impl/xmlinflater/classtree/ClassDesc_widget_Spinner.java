package org.itsnat.droid.impl.xmlinflater.classtree;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Spinner;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.attr.GravityUtil;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecCharSequence;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecDimensionInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMultipleName;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_Spinner_dropDownWidth;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_Spinner_popupBackground;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_Spinner extends ClassDescViewBased
{
    public ClassDesc_widget_Spinner(ClassDesc_widget_AbsSpinner parentClass)
    {
        super("android.widget.Spinner",parentClass);
    }

    public View createAndAddSpinnerObject(View viewParent,int index,int idStyle,String spinnerMode,Context ctx)
    {
        AttributeSet attributes = null; // createEmptyAttributeSet(ctx);

        if (idStyle == 0)
            idStyle = android.R.attr.spinnerStyle; // Inspirado en el código fuente de Spinner

        int mode;
        if (spinnerMode != null)
        {
            if ("dialog".equals(spinnerMode)) mode = Spinner.MODE_DIALOG;
            else if ("dropdown".equals(spinnerMode)) mode = Spinner.MODE_DROPDOWN;
            else throw new ItsNatDroidException("Unrecognized value name " + spinnerMode + " for attribute");
        }
        else mode = -1; // MODE_THEME = -1  es decir se delega en el atributo spinnerMode y si no está definido (que es el caso de layouts dinámico) en lo que diga el theme que suele ser dropdown

        View view = new Spinner(ctx, attributes, idStyle,mode);
        addViewObject(viewParent,view,index);
        return view;
    }


    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecDimensionInt(this,"dropDownHorizontalOffset",0f));
        // Es de traca pero android:dropDownSelector NO tiene implementación alguna en el código fuente
        addAttrDesc(new AttrDescReflecDimensionInt(this,"dropDownVerticalOffset",0f));
        addAttrDesc(new AttrDescReflecDimensionInt(this,"dropDownVerticalOffset",0f));
        addAttrDesc(new AttrDesc_widget_Spinner_dropDownWidth(this));
        addAttrDesc(new AttrDescReflecMultipleName(this,"gravity", GravityUtil.valueMap,"center"));
        addAttrDesc(new AttrDesc_widget_Spinner_popupBackground(this));
        addAttrDesc(new AttrDescReflecCharSequence(this,"prompt")); // En un layout compilado no se admiten literales, aquí sí, no es importante y es más flexible aún

    }
}

