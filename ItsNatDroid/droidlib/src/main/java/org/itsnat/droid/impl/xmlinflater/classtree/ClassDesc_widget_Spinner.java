package org.itsnat.droid.impl.xmlinflater.classtree;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecFieldSetDimensionWithNameInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodCharSequence;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodMultipleName;
import org.itsnat.droid.impl.xmlinflater.attr.GravityUtil;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_Spinner_dropDownHorizontalOffset;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_Spinner_dropDownVerticalOffset;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_Spinner_popupBackground;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_Spinner extends ClassDescViewBased
{
    public ClassDesc_widget_Spinner(ClassDescViewMgr classMgr,ClassDesc_widget_AbsSpinner parentClass)
    {
        super(classMgr,"android.widget.Spinner",parentClass);
    }

    public View createSpinnerObject(ViewGroup viewParent,int idStyle, String spinnerMode, Context ctx)
    {
        int mode;
        if (spinnerMode != null)
        {
            if ("dialog".equals(spinnerMode)) mode = Spinner.MODE_DIALOG;
            else if ("dropdown".equals(spinnerMode)) mode = Spinner.MODE_DROPDOWN;
            else throw new ItsNatDroidException("Unrecognized value name " + spinnerMode + " for attribute");
        }
        else mode = -1; // MODE_THEME = -1  es decir se delega en el atributo spinnerMode y si no está definido (que es el caso de layouts dinámico) en lo que diga el theme que suele ser dropdown

        View view;
        if (idStyle != 0)
        {
            // Pasamos new ContextThemeWrapper(ctx,idStyle) porque como parámetro el idStyle es ignorado
            // La aplicación de un style de todas formas hace alguna cosa rara, si se puede evitar usar style en un Spinner
            AttributeSet attributes = null;
            if (mode != -1)
                view = new Spinner(new ContextThemeWrapper(ctx,idStyle), attributes,idStyle,mode);
            else
                view = new Spinner(new ContextThemeWrapper(ctx,idStyle), attributes,idStyle);
        }
        else
        {
            // Es importante llamar a estos constructores y no pasar un idStyle con 0 pues Spinner define un style por defecto en el constructor que es "mandatory" si no especificamos uno explícitamente
            if (mode != -1)
                view = new Spinner(ctx, mode);
            else
                view = new Spinner(ctx);
        }

        return view;
    }


    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDesc_widget_Spinner_dropDownHorizontalOffset(this));
        // Es de traca pero android:dropDownSelector NO tiene implementación alguna en el código fuente
        addAttrDesc(new AttrDesc_widget_Spinner_dropDownVerticalOffset(this));
        addAttrDesc(new AttrDescReflecFieldSetDimensionWithNameInt(this,"dropDownWidth","mDropDownWidth","wrap_content"));
        addAttrDesc(new AttrDescReflecMethodMultipleName(this,"gravity", GravityUtil.valueMap,"center"));
        addAttrDesc(new AttrDesc_widget_Spinner_popupBackground(this));
        addAttrDesc(new AttrDescReflecMethodCharSequence(this,"prompt")); // En un layout compilado no se admiten literales, aquí sí, no es importante y es más flexible aún
        // android:spinnerMode no es un atributo normal, se pasa por el constructor del objeto Spinner
    }
}

