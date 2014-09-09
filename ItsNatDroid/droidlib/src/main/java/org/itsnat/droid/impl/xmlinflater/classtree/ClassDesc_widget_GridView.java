package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecDimensionInt;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_GridView extends ClassDescViewBased
{
    public ClassDesc_widget_GridView(ClassDesc_widget_AbsListView parentClass)
    {
        super("android.widget.GridView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecDimensionInt(this,"columnWidth",null));
    }
}

