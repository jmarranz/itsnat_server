package org.itsnat.droid.impl.xmlinflater.classtree;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecDrawable;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecFieldSetBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecFieldSetDimensionInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecFieldSetInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecInt;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_ProgressBar_indeterminate;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_ProgressBar_indeterminateBehavior;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_ProgressBar_interpolator;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_ProgressBar extends ClassDescViewBased
{
    public ClassDesc_widget_ProgressBar(ClassDesc_view_View parentClass)
    {
        super("android.widget.ProgressBar",parentClass);
    }

    protected void init()
    {
        super.init();

        // android:animationResolution es la traca, se procesa por ejemplo en 4.0.3 pero NI RASTRO en 4.4.1 aunque sigue estando en la documentación
        // por tanto no lo implementamos pues fallará en versiones modernas
        addAttrDesc(new AttrDesc_widget_ProgressBar_indeterminate(this,"indeterminate",true));
        addAttrDesc(new AttrDesc_widget_ProgressBar_indeterminateBehavior(this));
        addAttrDesc(new AttrDescReflecDrawable(this,"indeterminateDrawable",null)); // Android tiene un drawable por defecto
        addAttrDesc(new AttrDescReflecFieldSetInt(this,"indeterminateDuration","mDuration",4000));
        addAttrDesc(new AttrDescReflecFieldSetBoolean(this,"indeterminateOnly","mOnlyIndeterminate",false));
        addAttrDesc(new AttrDesc_widget_ProgressBar_interpolator(this));
        addAttrDesc(new AttrDescReflecInt(this,"max",100));
        addAttrDesc(new AttrDescReflecFieldSetDimensionInt(this,"maxHeight","mMaxHeight",48));
        addAttrDesc(new AttrDescReflecFieldSetDimensionInt(this,"maxWidth","mMaxWidth",48));
        addAttrDesc(new AttrDescReflecFieldSetDimensionInt(this,"minHeight","mMinHeight",24));
        addAttrDesc(new AttrDescReflecFieldSetDimensionInt(this,"minWidth","mMinWidth",24));
        // android:mirrorForRtl es de un Level superior a 15
        addAttrDesc(new AttrDescReflecInt(this,"progress",0));
    }

    public View createAndAddProgressBarObject(View viewParent,int index,int idStyle,Context ctx)
    {
        AttributeSet attributes = null; // createEmptyAttributeSet(ctx);

        if (idStyle == 0)
            idStyle = android.R.attr.progressBarStyle; // Inspirado en el código fuente de ProgressBar

        View view = new ProgressBar(ctx, attributes, idStyle);
        addViewObject(viewParent,view,index);
        return view;
    }
}

