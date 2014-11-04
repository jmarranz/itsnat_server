package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodSingleName;
import org.itsnat.droid.impl.xmlinflater.layout.attr.OrientationUtil;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_GridLayout_alignmentMode;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_GridLayout extends ClassDescViewBased
{
    public ClassDescView_widget_GridLayout(ClassDescViewMgr classMgr,ClassDescView_view_ViewGroup parentClass)
    {
        super(classMgr,"android.widget.GridLayout",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescView_widget_GridLayout_alignmentMode(this));
        addAttrDesc(new AttrDescViewReflecMethodInt(this,"columnCount",Integer.MIN_VALUE)); // El MIN_VALUE está sacado del código fuente
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"columnOrderPreserved",true));
        addAttrDesc(new AttrDescViewReflecMethodSingleName(this,"orientation",int.class, OrientationUtil.valueMap,"horizontal"));

        addAttrDesc(new AttrDescViewReflecMethodInt(this,"rowCount",Integer.MIN_VALUE)); // El MIN_VALUE está sacado del código fuente
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"rowOrderPreserved",true));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"useDefaultMargins",false));

    }
}

