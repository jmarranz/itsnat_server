package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecDrawable;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_widget_ExpandableListView_childIndicatorLeftRight;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_widget_ExpandableListView_indicatorLeftRight;

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

        addAttrDesc(new AttrDescReflecDrawable(this,"childDivider"));
        addAttrDesc(new AttrDescReflecDrawable(this,"childIndicator"));
        // android:childIndicatorEnd es Level 18
        addAttrDesc(new AttrDesc_widget_ExpandableListView_childIndicatorLeftRight(this, "childIndicatorLeft"));
        addAttrDesc(new AttrDesc_widget_ExpandableListView_childIndicatorLeftRight(this, "childIndicatorRight"));
        // android:childIndicatorStart es Level 18
        addAttrDesc(new AttrDescReflecDrawable(this,"groupIndicator"));
        // android:indicatorEnd es Level 18
        addAttrDesc(new AttrDesc_widget_ExpandableListView_indicatorLeftRight(this, "indicatorLeft"));
        addAttrDesc(new AttrDesc_widget_ExpandableListView_indicatorLeftRight(this, "indicatorRight"));
        // android:indicatorStart es Level 18
    }
}

