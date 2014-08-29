package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecCharSequence;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecColor;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_widget_TextView_gravity;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_widget_TextView_textAppearance;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_widget_TextView_textSize;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_TextView extends ClassDescViewBased
{
    public ClassDesc_widget_TextView(ClassDescViewBased parent)
    {
        super("android.widget.TextView",parent);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDesc_widget_TextView_gravity(this));  // gravity
        addAttrDesc(new AttrDescReflecCharSequence(this, "text"));
        addAttrDesc(new AttrDescReflecColor(this,"textColor")); // textColor
        addAttrDesc(new AttrDesc_widget_TextView_textSize(this)); // textSize
        addAttrDesc(new AttrDesc_widget_TextView_textAppearance(this)); // "textAppearance"
    }
}

