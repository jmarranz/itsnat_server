package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescDrawable;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescViewViewId;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescViewViewLayoutAlignParentTop;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescViewViewLayoutBelow;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescViewViewLayoutHeight;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescViewViewLayoutWidth;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescViewViewPadding;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescViewView extends ClassDescViewBase
{
    public ClassDescViewView()
    {
        super("android.view.View",null);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescDrawable(this,"background","setBackgroundDrawable"));  // setBackground() es desde Android 4.1
        addAttrDesc(new AttrDescViewViewId(this)); // "id"
        addAttrDesc(new AttrDescViewViewPadding(this,"paddingLeft"));
        addAttrDesc(new AttrDescViewViewPadding(this,"paddingTop"));
        addAttrDesc(new AttrDescViewViewPadding(this,"paddingRight"));
        addAttrDesc(new AttrDescViewViewPadding(this,"paddingBottom"));
        //addAttrDesc(new AttrDescViewViewStyle(this)); // "style"

        // Debidos a ViewGroup
        addAttrDesc(new AttrDescViewViewLayoutWidth(this));
        addAttrDesc(new AttrDescViewViewLayoutHeight(this));

        // Debidos a RelativeLayout
        addAttrDesc(new AttrDescViewViewLayoutAlignParentTop(this));
        addAttrDesc(new AttrDescViewViewLayoutBelow(this));
    }
}
