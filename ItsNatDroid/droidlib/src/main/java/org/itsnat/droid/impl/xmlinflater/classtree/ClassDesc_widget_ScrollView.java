package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodBoolean;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_ScrollView extends ClassDescViewBased
{
    public ClassDesc_widget_ScrollView(ClassDesc_widget_FrameLayout parentClass)
    {
        super("android.widget.ScrollView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecMethodBoolean(this,"fillViewport",false));
    }
}

