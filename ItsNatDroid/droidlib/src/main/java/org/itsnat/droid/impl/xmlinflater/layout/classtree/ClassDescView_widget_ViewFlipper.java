package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodInt;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_ViewFlipper extends ClassDescViewBased
{
    public ClassDescView_widget_ViewFlipper(ClassDescViewMgr classMgr,ClassDescView_widget_ViewAnimator parentClass)
    {
        super(classMgr,"android.widget.ViewFlipper",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"autoStart",false));
        addAttrDesc(new AttrDescViewReflecMethodInt(this,"flipInterval",3000));
    }
}

