package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodBoolean;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_HorizontalScrollView extends ClassDescViewBased
{
    public ClassDescView_widget_HorizontalScrollView(ClassDescViewMgr classMgr,ClassDescView_widget_FrameLayout parentClass)
    {
        super(classMgr,"android.widget.HorizontalScrollView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"fillViewport",false));
    }
}

