package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecDrawable;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_ExpandableListView_childIndicatorLeft;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_ExpandableListView_childIndicatorRight;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_ExpandableListView_indicatorLeft;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_ExpandableListView_indicatorRight;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_ExpandableListView extends ClassDescViewBased
{
    public ClassDesc_widget_ExpandableListView(ClassDesc_widget_ListView parentClass)
    {
        super("android.widget.ExpandableListView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecDrawable(this,"childDivider",null)); // Android tiene un Drawable por defecto
        addAttrDesc(new AttrDescReflecDrawable(this,"childIndicator","@null"));
        // android:childIndicatorEnd es Level 18
        addAttrDesc(new AttrDesc_widget_ExpandableListView_childIndicatorLeft(this));
        addAttrDesc(new AttrDesc_widget_ExpandableListView_childIndicatorRight(this));
        // android:childIndicatorStart es Level 18
        addAttrDesc(new AttrDescReflecDrawable(this,"groupIndicator",null)); // Android tiene un Drawable por defecto
        // android:indicatorEnd es Level 18
        addAttrDesc(new AttrDesc_widget_ExpandableListView_indicatorLeft(this));
        addAttrDesc(new AttrDesc_widget_ExpandableListView_indicatorRight(this));
        // android:indicatorStart es Level 18
    }
}

