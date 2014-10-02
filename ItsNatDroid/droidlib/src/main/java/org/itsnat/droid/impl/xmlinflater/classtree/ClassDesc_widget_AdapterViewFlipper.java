package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodInt;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_AdapterViewFlipper extends ClassDescViewBased
{
    public ClassDesc_widget_AdapterViewFlipper(ClassDescViewMgr classMgr,ClassDesc_widget_AdapterViewAnimator parentClass)
    {
        super(classMgr,"android.widget.AdapterViewFlipper",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecMethodBoolean(this,"autoStart",false));
        addAttrDesc(new AttrDescReflecMethodInt(this,"flipInterval",10000));
    }
}

