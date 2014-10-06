package org.itsnat.droid.impl.xmlinflater.classtree;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.clientdoc.NodeToInsertImpl;
import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.InflatedLayoutImpl;
import org.itsnat.droid.impl.xmlinflater.XMLLayoutInflateService;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecFieldSetDimensionWithNameInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodCharSequence;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodMultipleName;
import org.itsnat.droid.impl.xmlinflater.attr.GravityUtil;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_Spinner_dropDownHorizontalOffset;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_Spinner_dropDownVerticalOffset;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_Spinner_popupBackground;
import org.xmlpull.v1.XmlPullParser;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_Spinner extends ClassDescViewBased
{
    public ClassDesc_widget_Spinner(ClassDescViewMgr classMgr,ClassDesc_widget_AbsSpinner parentClass)
    {
        super(classMgr,"android.widget.Spinner",parentClass);
    }

    @Override
    protected boolean isAttributeIgnored(String namespaceURI,String name)
    {
        if (super.isAttributeIgnored(namespaceURI,name))
            return true;
        return isSpinnerModeAttribute(namespaceURI,name); // Se trata de forma especial en otro lugar
    }

    private static boolean isSpinnerModeAttribute(String namespaceURI,String name)
    {
        return XMLLayoutInflateService.XMLNS_ANDROID.equals(namespaceURI) && name.equals("spinnerMode");
    }

    private static String findSpinnerModeAttribute(ItsNatDocImpl itsNatDoc,NodeToInsertImpl newChildToIn)
    {
        return findAttribute(XMLLayoutInflateService.XMLNS_ANDROID,"spinnerMode",newChildToIn);
    }

    @Override
    public View createViewObjectFromRemote(ItsNatDocImpl itsNatDoc,ViewGroup viewParent,NodeToInsertImpl newChildToIn,int idStyle)
    {
        Context ctx = itsNatDoc.getPageImpl().getContext();
        String spinnerMode = findSpinnerModeAttribute(itsNatDoc,newChildToIn);
        return createSpinnerObject(viewParent, idStyle, spinnerMode, ctx);
    }

    private String findSpinnerModeAttribute(XmlPullParser parser)
    {
        return findAttribute(XMLLayoutInflateService.XMLNS_ANDROID,"spinnerMode",parser);
    }

    @Override
    public View createViewObjectFromParser(InflatedLayoutImpl inflated,ViewGroup viewParent, XmlPullParser parser,int idStyle)
    {
        Context ctx = inflated.getContext();

        String spinnerMode = findSpinnerModeAttribute(parser);
        return createSpinnerObject(viewParent,idStyle, spinnerMode, ctx);
    }

    private View createSpinnerObject(ViewGroup viewParent,int idStyle, String spinnerMode, Context ctx)
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
            if (mode != -1)
                view = new Spinner(new ContextThemeWrapper(ctx,idStyle),(AttributeSet)null,idStyle,mode);
            else
                view = new Spinner(new ContextThemeWrapper(ctx,idStyle),(AttributeSet)null,idStyle);
        }
        else
        {
            // Es importante llamar a estos constructores y no pasar un idStyle con 0 pues Spinner define un style por defecto en el constructor que es "mandatory" si no especificamos uno explícitamente
            if (mode != -1)
                view = new Spinner(ctx, mode);
            else
                view = new Spinner(ctx,(AttributeSet)null); // El constructor de un sólo param también vale
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
        addAttrDesc(new AttrDescReflecMethodCharSequence(this,"prompt","")); // En un layout compilado no se admiten literales, aquí sí, no es importante y es más flexible aún
        // android:spinnerMode no es un atributo normal, se pasa por el constructor del objeto Spinner
    }
}

