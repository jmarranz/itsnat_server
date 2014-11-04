package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecFieldSetBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecFieldSetDimensionInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecFieldSetInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDesc_widget_ProgressBar_indeterminate;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDesc_widget_ProgressBar_indeterminateBehavior;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDesc_widget_ProgressBar_interpolator;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDesc_widget_ProgressBar_progressDrawable;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_ProgressBar extends ClassDescViewBased
{
    public ClassDesc_widget_ProgressBar(ClassDescViewMgr classMgr,ClassDesc_view_View parentClass)
    {
        super(classMgr,"android.widget.ProgressBar",parentClass);
    }

    protected void init()
    {
        super.init();

        // android:animationResolution es la traca, se procesa por ejemplo en 4.0.3 pero NI RASTRO en 4.4.1 aunque sigue estando en la documentación
        // por tanto no lo implementamos pues fallará en versiones modernas
        addAttrDesc(new AttrDesc_widget_ProgressBar_indeterminate(this,"indeterminate",true));
        addAttrDesc(new AttrDesc_widget_ProgressBar_indeterminateBehavior(this));
        addAttrDesc(new AttrDescReflecMethodDrawable(this,"indeterminateDrawable",null)); // Android tiene un drawable por defecto
        addAttrDesc(new AttrDescReflecFieldSetInt(this,"indeterminateDuration","mDuration",4000));
        addAttrDesc(new AttrDescReflecFieldSetBoolean(this,"indeterminateOnly","mOnlyIndeterminate",false));
        addAttrDesc(new AttrDesc_widget_ProgressBar_interpolator(this));
        addAttrDesc(new AttrDescReflecMethodInt(this,"max",100));
        addAttrDesc(new AttrDescReflecFieldSetDimensionInt(this,"maxHeight","mMaxHeight",48));
        addAttrDesc(new AttrDescReflecFieldSetDimensionInt(this,"maxWidth","mMaxWidth",48));
        addAttrDesc(new AttrDescReflecFieldSetDimensionInt(this,"minHeight","mMinHeight",24));
        addAttrDesc(new AttrDescReflecFieldSetDimensionInt(this,"minWidth","mMinWidth",24));
        // android:mirrorForRtl es de un Level superior a 15
        addAttrDesc(new AttrDescReflecMethodInt(this,"progress",0));
        addAttrDesc(new AttrDesc_widget_ProgressBar_progressDrawable(this));
        addAttrDesc(new AttrDescReflecMethodInt(this,"secondaryProgress",0));
    }

}

