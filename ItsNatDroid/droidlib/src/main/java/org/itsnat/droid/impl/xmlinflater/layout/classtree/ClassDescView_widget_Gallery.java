package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodDimensionInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodFloat;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodMultipleName;
import org.itsnat.droid.impl.xmlinflater.layout.attr.GravityUtil;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_Gallery extends ClassDescViewBased
{
    public ClassDescView_widget_Gallery(ClassDescViewMgr classMgr,ClassDescView_widget_AbsSpinner parentClass)
    {
        super(classMgr,"android.widget.Gallery",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescViewReflecMethodInt(this,"animationDuration",400));
        addAttrDesc(new AttrDescViewReflecMethodMultipleName(this,"gravity", GravityUtil.valueMap,"left")); // No está claro que left sea el valor por defecto
        addAttrDesc(new AttrDescViewReflecMethodDimensionInt(this,"spacing",0f));
        addAttrDesc(new AttrDescViewReflecMethodFloat(this,"unselectedAlpha",0.5f));


    }
}

