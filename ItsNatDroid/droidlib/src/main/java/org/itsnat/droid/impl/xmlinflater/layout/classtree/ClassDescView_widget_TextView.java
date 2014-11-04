package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodCharSequence;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodColor;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodDimensionInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodFloat;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodMultipleName;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodString;
import org.itsnat.droid.impl.xmlinflater.layout.attr.GravityUtil;
import org.itsnat.droid.impl.xmlinflater.layout.attr.ImeOptionsUtil;
import org.itsnat.droid.impl.xmlinflater.layout.attr.InputTypeUtil;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_TextView_compoundDrawables;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_TextView_imeActionLabel;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_TextView_maxLength;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_TextView_textAppearance;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_TextView_typeface;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_TextView_autoLink;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_TextView_bufferType;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_TextView_ellipsize;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_TextView_imeActionId;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_TextView_lineSpacingExtra;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_TextView_lineSpacingMultiplier;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_TextView_marqueeRepeatLimit;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_TextView_shadowLayer_base;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_TextView_textAllCaps;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_TextView_textSize;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_TextView_textStyle;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_TextView extends ClassDescViewBased
{
    public ClassDescView_widget_TextView(ClassDescViewMgr classMgr,ClassDescView_view_View parentClass)
    {
        super(classMgr,"android.widget.TextView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescView_widget_TextView_autoLink(this));
        // android:autoText está deprecated desde Level 3, no se implementarlo y tiene alternativa (inputType)
        addAttrDesc(new AttrDescView_widget_TextView_bufferType(this));
        // android:capitalize está deprecated desde Level 3, no se implementarlo y tiene alternativa (inputType)
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"cursorVisible",true));
        // android:digits no se implementarlo y mi impresión es que es similar a autoText, capitalize etc

        addAttrDesc(new AttrDescView_widget_TextView_compoundDrawables(this,"drawableLeft"));
        addAttrDesc(new AttrDescView_widget_TextView_compoundDrawables(this,"drawableTop"));
        addAttrDesc(new AttrDescView_widget_TextView_compoundDrawables(this,"drawableRight"));
        addAttrDesc(new AttrDescView_widget_TextView_compoundDrawables(this,"drawableBottom"));
        // android:drawableStart y android:drawableEnd en teoría existen pero su acceso via métodos es desde Level 17 y no los veo relevantes
        addAttrDesc(new AttrDescViewReflecMethodDimensionInt(this,"drawablePadding","setCompoundDrawablePadding",0f));
        // android:editable está deprecated desde Level 3, no se implementarlo y tiene alternativa (inputType)
        // android:editorExtras tiene un bug y no funciona ni con un layout compilado: https://code.google.com/p/android/issues/detail?id=38122
        addAttrDesc(new AttrDescView_widget_TextView_ellipsize(this));
        addAttrDesc(new AttrDescViewReflecMethodInt(this, "ems", -1));
        // android:fontFamily creo que es Level 16
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"freezesText",false));
        addAttrDesc(new AttrDescViewReflecMethodMultipleName(this,"gravity", GravityUtil.valueMap,"top|start"));
        addAttrDesc(new AttrDescViewReflecMethodDimensionInt(this,"height",-1f));
        addAttrDesc(new AttrDescViewReflecMethodCharSequence(this,"hint",""));
        addAttrDesc(new AttrDescView_widget_TextView_imeActionId(this));
        addAttrDesc(new AttrDescView_widget_TextView_imeActionLabel(this));
        addAttrDesc(new AttrDescViewReflecMethodMultipleName(this,"imeOptions", ImeOptionsUtil.valueMap,"actionUnspecified"));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"includeFontPadding",true));
        // android:inputMethod lleva deprecated desde Level 3, mal documentado, es difícil de implementar y tiene substituto en inputType
        //    una clase de ejemplo podría ser android.text.method.DateTimeInputMethod
        addAttrDesc(new AttrDescViewReflecMethodMultipleName(this,"inputType", InputTypeUtil.valueMap,"text")); // No estoy seguro que el valor por defecto sea "text" pero parece el más razonable
        addAttrDesc(new AttrDescView_widget_TextView_lineSpacingExtra(this));
        addAttrDesc(new AttrDescView_widget_TextView_lineSpacingMultiplier(this));
        addAttrDesc(new AttrDescViewReflecMethodInt(this,"lines",-1));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"linksClickable",true));
        addAttrDesc(new AttrDescView_widget_TextView_marqueeRepeatLimit(this));
        addAttrDesc(new AttrDescViewReflecMethodInt(this,"maxEms", -1));
        addAttrDesc(new AttrDescViewReflecMethodDimensionInt(this,"maxHeight",-1f));
        addAttrDesc(new AttrDescView_widget_TextView_maxLength(this));
        addAttrDesc(new AttrDescViewReflecMethodInt(this,"maxLines",-1));
        addAttrDesc(new AttrDescViewReflecMethodDimensionInt(this,"maxWidth",-1f));
        addAttrDesc(new AttrDescViewReflecMethodInt(this,"minEms", -1));
        addAttrDesc(new AttrDescViewReflecMethodDimensionInt(this,"minHeight",-1f));
        addAttrDesc(new AttrDescViewReflecMethodInt(this,"minLines",-1));
        // android:numeric está deprecated desde Level 3, no se implementarlo y tiene alternativa (inputType)
        // android:password está deprecated desde Level 3, no se implementarlo y tiene alternativa (inputType)
        // android:phoneNumber está deprecated desde Level 3, no se implementarlo y tiene alternativa (inputType)
        addAttrDesc(new AttrDescViewReflecMethodString(this,"privateImeOptions",""));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"scrollHorizontally","setHorizontallyScrolling",false));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"selectAllOnFocus",false));
        addAttrDesc(new AttrDescView_widget_TextView_shadowLayer_base(this,"shadowColor"));
        addAttrDesc(new AttrDescView_widget_TextView_shadowLayer_base(this,"shadowDx"));
        addAttrDesc(new AttrDescView_widget_TextView_shadowLayer_base(this,"shadowDy"));
        addAttrDesc(new AttrDescView_widget_TextView_shadowLayer_base(this,"shadowRadius"));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"singleLine",false));
        addAttrDesc(new AttrDescViewReflecMethodCharSequence(this, "text","")); // El tipo de CharSequence resultante (Spannable etc) depende del bufferType definido pero el orden no importa pues al definir el bufferType exige dar el texto como param para "retransformarlo"
        addAttrDesc(new AttrDescView_widget_TextView_textAllCaps(this));
        addAttrDesc(new AttrDescView_widget_TextView_textAppearance(this)); // "textAppearance"
        addAttrDesc(new AttrDescViewReflecMethodColor(this,"textColor","#000000"));
        addAttrDesc(new AttrDescViewReflecMethodColor(this,"textColorHighlight","setHighlightColor","#000000"));
        addAttrDesc(new AttrDescViewReflecMethodColor(this,"textColorHint","setHintTextColor","#000000"));
        addAttrDesc(new AttrDescViewReflecMethodColor(this,"textColorLink","setLinkTextColor","#000000"));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"textIsSelectable",false));
        addAttrDesc(new AttrDescViewReflecMethodFloat(this,"textScaleX",1.0f));
        // No, no es un error, no hay textScaleY (en Level 15 ni en superiores)
        addAttrDesc(new AttrDescView_widget_TextView_textSize(this)); // textSize
        addAttrDesc(new AttrDescView_widget_TextView_textStyle(this));
        addAttrDesc(new AttrDescView_widget_TextView_typeface(this));
        addAttrDesc(new AttrDescViewReflecMethodDimensionInt(this,"width",-1f));
    }
}

