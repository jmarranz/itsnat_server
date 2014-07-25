package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescDrawable;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_alignParentTop;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_id;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_below;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_height;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_layout_width;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_padding;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_visibility;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_view_View extends ClassDescViewBased
{
    public ClassDesc_view_View()
    {
        super("android.view.View",null);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescDrawable(this,"background","setBackgroundDrawable"));  // setBackground() es desde Android 4.1
        addAttrDesc(new AttrDesc_view_View_id(this)); // "id"
        addAttrDesc(new AttrDesc_view_View_padding(this,"paddingLeft"));
        addAttrDesc(new AttrDesc_view_View_padding(this,"paddingTop"));
        addAttrDesc(new AttrDesc_view_View_padding(this,"paddingRight"));
        addAttrDesc(new AttrDesc_view_View_padding(this,"paddingBottom"));
        addAttrDesc(new AttrDesc_view_View_visibility(this));

        // Debidos a ViewGroup
        addAttrDesc(new AttrDesc_view_View_layout_width(this));
        addAttrDesc(new AttrDesc_view_View_layout_height(this));

        // Debidos a RelativeLayout
        addAttrDesc(new AttrDesc_view_View_layout_alignParentTop(this));
        addAttrDesc(new AttrDesc_view_View_layout_below(this));
    }
}
