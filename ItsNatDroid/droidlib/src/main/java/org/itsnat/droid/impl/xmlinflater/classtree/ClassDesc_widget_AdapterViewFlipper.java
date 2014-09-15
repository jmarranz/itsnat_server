package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecInt;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_AdapterViewFlipper extends ClassDescViewBased
{
    public ClassDesc_widget_AdapterViewFlipper(ClassDesc_widget_AdapterViewAnimator parentClass)
    {
        super("android.widget.AdapterViewFlipper",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecBoolean(this,"autoStart",false));
        addAttrDesc(new AttrDescReflecInt(this,"flipInterval",10000));
    }
}

