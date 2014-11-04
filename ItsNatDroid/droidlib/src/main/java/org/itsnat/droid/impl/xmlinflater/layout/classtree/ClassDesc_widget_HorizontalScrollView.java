package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodBoolean;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_HorizontalScrollView extends ClassDescViewBased
{
    public ClassDesc_widget_HorizontalScrollView(ClassDescViewMgr classMgr,ClassDesc_widget_FrameLayout parentClass)
    {
        super(classMgr,"android.widget.HorizontalScrollView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecMethodBoolean(this,"fillViewport",false));
    }
}

