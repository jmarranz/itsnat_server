package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecFieldSetBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecFieldSetDimensionInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecFieldSetInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_ProgressBar_indeterminate;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_ProgressBar_interpolator;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_ProgressBar_indeterminateBehavior;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_ProgressBar_progressDrawable;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_ProgressBar extends ClassDescViewBased
{
    public ClassDescView_widget_ProgressBar(ClassDescViewMgr classMgr,ClassDescView_view_View parentClass)
    {
        super(classMgr,"android.widget.ProgressBar",parentClass);
    }

    protected void init()
    {
        super.init();

        // android:animationResolution es la traca, se procesa por ejemplo en 4.0.3 pero NI RASTRO a partir de 4.1.1 aunque sigue estando en la documentación
        addAttrDesc(new AttrDescView_widget_ProgressBar_indeterminate(this,"indeterminate",true));
        addAttrDesc(new AttrDescView_widget_ProgressBar_indeterminateBehavior(this));
        addAttrDesc(new AttrDescViewReflecMethodDrawable(this,"indeterminateDrawable",null)); // Android tiene un drawable por defecto
        addAttrDesc(new AttrDescViewReflecFieldSetInt(this,"indeterminateDuration","mDuration",4000));
        addAttrDesc(new AttrDescViewReflecFieldSetBoolean(this,"indeterminateOnly","mOnlyIndeterminate",false));
        addAttrDesc(new AttrDescView_widget_ProgressBar_interpolator(this));
        addAttrDesc(new AttrDescViewReflecMethodInt(this,"max",100));
        addAttrDesc(new AttrDescViewReflecFieldSetDimensionInt(this,"maxHeight","mMaxHeight",48));
        addAttrDesc(new AttrDescViewReflecFieldSetDimensionInt(this,"maxWidth","mMaxWidth",48));
        addAttrDesc(new AttrDescViewReflecFieldSetDimensionInt(this,"minHeight","mMinHeight",24));
        addAttrDesc(new AttrDescViewReflecFieldSetDimensionInt(this,"minWidth","mMinWidth",24));
        // android:mirrorForRtl es de un Level superior a 15
        addAttrDesc(new AttrDescViewReflecMethodInt(this,"progress",0));
        addAttrDesc(new AttrDescView_widget_ProgressBar_progressDrawable(this));
        addAttrDesc(new AttrDescViewReflecMethodInt(this,"secondaryProgress",0));
    }

}

