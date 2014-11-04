package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodInt;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_ViewFlipper extends ClassDescViewBased
{
    public ClassDesc_widget_ViewFlipper(ClassDescViewMgr classMgr,ClassDesc_widget_ViewAnimator parentClass)
    {
        super(classMgr,"android.widget.ViewFlipper",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecMethodBoolean(this,"autoStart",false));
        addAttrDesc(new AttrDescReflecMethodInt(this,"flipInterval",3000));
    }
}

