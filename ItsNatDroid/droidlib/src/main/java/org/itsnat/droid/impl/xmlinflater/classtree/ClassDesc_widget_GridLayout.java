package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodInt;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_GridLayout_orientation;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_GridLayout_alignmentMode;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_GridLayout extends ClassDescViewBased
{
    public ClassDesc_widget_GridLayout(ClassDesc_widget_ViewGroup parentClass)
    {
        super("android.widget.GridLayout",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDesc_widget_GridLayout_alignmentMode(this));
        addAttrDesc(new AttrDescReflecMethodInt(this,"columnCount",Integer.MIN_VALUE)); // El MIN_VALUE está sacado del código fuente
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"columnOrderPreserved",true));
        addAttrDesc(new AttrDesc_widget_GridLayout_orientation(this));
        addAttrDesc(new AttrDescReflecMethodInt(this,"rowCount",Integer.MIN_VALUE)); // El MIN_VALUE está sacado del código fuente
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"rowOrderPreserved",true));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"useDefaultMargins",false));

    }
}

