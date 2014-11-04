package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodDimensionInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodFloat;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodMultipleName;
import org.itsnat.droid.impl.xmlinflater.layout.attr.GravityUtil;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_Gallery extends ClassDescViewBased
{
    public ClassDesc_widget_Gallery(ClassDescViewMgr classMgr,ClassDesc_widget_AbsSpinner parentClass)
    {
        super(classMgr,"android.widget.Gallery",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecMethodInt(this,"animationDuration",400));
        addAttrDesc(new AttrDescReflecMethodMultipleName(this,"gravity", GravityUtil.valueMap,"left")); // No está claro que left sea el valor por defecto
        addAttrDesc(new AttrDescReflecMethodDimensionInt(this,"spacing",0f));
        addAttrDesc(new AttrDescReflecMethodFloat(this,"unselectedAlpha",0.5f));


    }
}

