package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDesc_widget_ExpandableListView_childIndicatorLeft;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDesc_widget_ExpandableListView_childIndicatorRight;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDesc_widget_ExpandableListView_indicatorLeft;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDesc_widget_ExpandableListView_indicatorRight;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_ExpandableListView extends ClassDescViewBased
{
    public ClassDesc_widget_ExpandableListView(ClassDescViewMgr classMgr,ClassDesc_widget_ListView parentClass)
    {
        super(classMgr,"android.widget.ExpandableListView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecMethodDrawable(this,"childDivider",null)); // Android tiene un Drawable por defecto
        addAttrDesc(new AttrDescReflecMethodDrawable(this,"childIndicator","@null"));
        // android:childIndicatorEnd es Level 18
        addAttrDesc(new AttrDesc_widget_ExpandableListView_childIndicatorLeft(this));
        addAttrDesc(new AttrDesc_widget_ExpandableListView_childIndicatorRight(this));
        // android:childIndicatorStart es Level 18
        addAttrDesc(new AttrDescReflecMethodDrawable(this,"groupIndicator",null)); // Android tiene un Drawable por defecto
        // android:indicatorEnd es Level 18
        addAttrDesc(new AttrDesc_widget_ExpandableListView_indicatorLeft(this));
        addAttrDesc(new AttrDesc_widget_ExpandableListView_indicatorRight(this));
        // android:indicatorStart es Level 18
    }
}

