package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodSingleName;
import org.itsnat.droid.impl.xmlinflater.layout.attr.OrientationUtil;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDesc_widget_GridLayout_alignmentMode;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_GridLayout extends ClassDescViewBased
{
    public ClassDesc_widget_GridLayout(ClassDescViewMgr classMgr,ClassDesc_view_ViewGroup parentClass)
    {
        super(classMgr,"android.widget.GridLayout",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDesc_widget_GridLayout_alignmentMode(this));
        addAttrDesc(new AttrDescReflecMethodInt(this,"columnCount",Integer.MIN_VALUE)); // El MIN_VALUE está sacado del código fuente
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"columnOrderPreserved",true));
        addAttrDesc(new AttrDescReflecMethodSingleName(this,"orientation",int.class, OrientationUtil.valueMap,"horizontal"));

        addAttrDesc(new AttrDescReflecMethodInt(this,"rowCount",Integer.MIN_VALUE)); // El MIN_VALUE está sacado del código fuente
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"rowOrderPreserved",true));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"useDefaultMargins",false));

    }
}

