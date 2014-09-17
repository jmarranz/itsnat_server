package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.GravityUtil;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecCharSequence;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecColor;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMultipleName;
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

        addAttrDesc(new AttrDescReflecMultipleName(this,"gravity", GravityUtil.valueMap,"top|start"));
        addAttrDesc(new AttrDescReflecCharSequence(this, "text"));
        addAttrDesc(new AttrDescReflecColor(this,"textColor","#000000")); // textColor
        addAttrDesc(new AttrDesc_widget_TextView_textSize(this)); // textSize
        addAttrDesc(new AttrDesc_widget_TextView_textAppearance(this)); // "textAppearance"
    }
}

