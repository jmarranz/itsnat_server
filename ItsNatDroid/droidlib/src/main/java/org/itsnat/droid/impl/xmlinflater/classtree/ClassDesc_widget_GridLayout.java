package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecInt;
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
        addAttrDesc(new AttrDescReflecInt(this,"columnCount",Integer.MIN_VALUE)); // El MIN_VALUE está sacado del código fuente
        addAttrDesc(new AttrDescReflecBoolean(this,"columnOrderPreserved",true));
        addAttrDesc(new AttrDesc_widget_GridLayout_orientation(this));
        addAttrDesc(new AttrDescReflecInt(this,"rowCount",Integer.MIN_VALUE)); // El MIN_VALUE está sacado del código fuente
        addAttrDesc(new AttrDescReflecBoolean(this,"rowOrderPreserved",true));
        addAttrDesc(new AttrDescReflecBoolean(this,"useDefaultMargins",false));

    }
}

