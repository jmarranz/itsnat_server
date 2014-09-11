package org.itsnat.droid.impl.xmlinflater.classtree;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_Spinner extends ClassDescViewBased
{
    public ClassDesc_widget_Spinner(ClassDesc_widget_AbsSpinner parentClass)
    {
        super("android.widget.Spinner",parentClass);
    }

    protected void init()
    {
        super.init();

        /*
        addAttrDesc(new AttrDescReflecInt(this,"animationDuration",400));
        addAttrDesc(new AttrDescReflecMultipleName(this,"gravity", AttrDescGravityUtil.valueMap,"left")); // No está claro que left sea el valor por defecto
        addAttrDesc(new AttrDescReflecDimensionInt(this,"spacing",0f));
        addAttrDesc(new AttrDescReflecFloat(this,"unselectedAlpha",0.5f));
        */

    }
}

