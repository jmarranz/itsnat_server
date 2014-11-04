package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecFieldSetDimensionInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecFieldSetDrawable;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodCharSequence;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDesc_widget_Switch_switchTextAppearance;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDesc_widget_Switch_textStyle;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDesc_widget_Switch_typeface;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_Switch extends ClassDescViewBased
{
    public ClassDesc_widget_Switch(ClassDescViewMgr classMgr,ClassDesc_widget_CompoundButton parentClass)
    {
        super(classMgr,"android.widget.Switch",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecFieldSetDimensionInt(this,"switchMinWidth","mSwitchMinWidth",0));
        addAttrDesc(new AttrDescReflecFieldSetDimensionInt(this,"switchPadding","mSwitchPadding",0));
        addAttrDesc(new AttrDesc_widget_Switch_switchTextAppearance(this));
        addAttrDesc(new AttrDescReflecMethodCharSequence(this,"textOff",null)); // Adnroid tiene un texto por defecto
        addAttrDesc(new AttrDescReflecMethodCharSequence(this,"textOn",null)); // Adnroid tiene un texto por defecto
        addAttrDesc(new AttrDesc_widget_Switch_textStyle(this));
        addAttrDesc(new AttrDescReflecFieldSetDrawable(this,"thumb","mThumbDrawable",null)); // Android tiene un drawable por defecto
        addAttrDesc(new AttrDescReflecFieldSetDimensionInt(this,"thumbTextPadding","mThumbTextPadding",0));
        addAttrDesc(new AttrDescReflecFieldSetDrawable(this,"track","mTrackDrawable",null)); // Android tiene un drawable por defecto
        addAttrDesc(new AttrDesc_widget_Switch_typeface(this));

    }
}

