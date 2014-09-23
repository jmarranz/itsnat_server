package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodCharSequence;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodColor;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodDimensionInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodMultipleName;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodString;
import org.itsnat.droid.impl.xmlinflater.attr.GravityUtil;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_TextView_autoLink;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_TextView_bufferType;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_TextView_compoundDrawables;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_TextView_ellipsize;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_TextView_imeActionId;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_TextView_imeActionLabel;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_TextView_imeOptions;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_TextView_inputType;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_TextView_lineSpacingExtra;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_TextView_lineSpacingMultiplier;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_TextView_marqueeRepeatLimit;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_TextView_maxLength;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_TextView_textAppearance;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_TextView_textSize;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_TextView extends ClassDescViewBased
{
    public ClassDesc_widget_TextView(ClassDesc_view_View parentClass)
    {
        super("android.widget.TextView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDesc_widget_TextView_autoLink(this));
        // android:autoText está deprecated desde Level 3, no se implementarlo y tiene alternativa (inputType)
        addAttrDesc(new AttrDesc_widget_TextView_bufferType(this));
        // android:capitalize está deprecated desde Level 3, no se implementarlo y tiene alternativa (inputType)
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"cursorVisible",true));
        // android:digits no se implementarlo

        addAttrDesc(new AttrDesc_widget_TextView_compoundDrawables(this,"drawableLeft"));
        addAttrDesc(new AttrDesc_widget_TextView_compoundDrawables(this,"drawableTop"));
        addAttrDesc(new AttrDesc_widget_TextView_compoundDrawables(this,"drawableRight"));
        addAttrDesc(new AttrDesc_widget_TextView_compoundDrawables(this,"drawableBottom"));
        // android:drawableStart y android:drawableEnd en teoría existen pero su acceso via métodos es desde Level 17 y no los veo relevantes
        addAttrDesc(new AttrDescReflecMethodDimensionInt(this,"drawablePadding","setCompoundDrawablePadding",0f));
        // android:editable está deprecated desde Level 3, no se implementarlo y tiene alternativa (inputType)
        // android:editorExtras tiene un bug y no funciona ni con un layout compilado: https://code.google.com/p/android/issues/detail?id=38122
        addAttrDesc(new AttrDesc_widget_TextView_ellipsize(this));
        addAttrDesc(new AttrDescReflecMethodInt(this, "ems", -1));
        // android:fontFamily creo que es Level 16
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"freezesText",false));
        addAttrDesc(new AttrDescReflecMethodMultipleName(this,"gravity", GravityUtil.valueMap,"top|start"));
        addAttrDesc(new AttrDescReflecMethodDimensionInt(this,"height",-1f));
        addAttrDesc(new AttrDescReflecMethodCharSequence(this,"hint"));
        addAttrDesc(new AttrDesc_widget_TextView_imeActionId(this));
        addAttrDesc(new AttrDesc_widget_TextView_imeActionLabel(this));
        addAttrDesc(new AttrDesc_widget_TextView_imeOptions(this));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"includeFontPadding",true));
        // android:inputMethod lleva deprecated desde Level 3, mal documentado, es difícil de implementar y tiene substituto en inputType
        //    una clase de ejemplo podría ser android.text.method.DateTimeInputMethod
        addAttrDesc(new AttrDesc_widget_TextView_inputType(this));
        addAttrDesc(new AttrDesc_widget_TextView_lineSpacingExtra(this));
        addAttrDesc(new AttrDesc_widget_TextView_lineSpacingMultiplier(this));
        addAttrDesc(new AttrDescReflecMethodInt(this,"lines",-1));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"linksClickable",true));
        addAttrDesc(new AttrDesc_widget_TextView_marqueeRepeatLimit(this));
        addAttrDesc(new AttrDescReflecMethodInt(this,"maxEms", -1));
        addAttrDesc(new AttrDescReflecMethodDimensionInt(this,"maxHeight",-1f));
        addAttrDesc(new AttrDesc_widget_TextView_maxLength(this));
        addAttrDesc(new AttrDescReflecMethodInt(this,"maxLines",-1));
        addAttrDesc(new AttrDescReflecMethodDimensionInt(this,"maxWidth",-1f));
        addAttrDesc(new AttrDescReflecMethodInt(this,"minEms", -1));
        addAttrDesc(new AttrDescReflecMethodDimensionInt(this,"minHeight",-1f));
        addAttrDesc(new AttrDescReflecMethodInt(this,"minLines",-1));
        // android:numeric está deprecated desde Level 3, no se implementarlo y tiene alternativa (inputType)
        // android:password está deprecated desde Level 3, no se implementarlo y tiene alternativa (inputType)
        // android:phoneNumber está deprecated desde Level 3, no se implementarlo y tiene alternativa (inputType)
        addAttrDesc(new AttrDescReflecMethodString(this,"privateImeOptions"));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"scrollHorizontally","setHorizontallyScrolling",false));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"selectAllOnFocus",false));



        addAttrDesc(new AttrDescReflecMethodBoolean(this,"singleLine",false));
        addAttrDesc(new AttrDescReflecMethodCharSequence(this, "text"));
        addAttrDesc(new AttrDescReflecMethodColor(this,"textColor","#000000"));
        addAttrDesc(new AttrDesc_widget_TextView_textSize(this)); // textSize
        addAttrDesc(new AttrDesc_widget_TextView_textAppearance(this)); // "textAppearance"
    }
}

