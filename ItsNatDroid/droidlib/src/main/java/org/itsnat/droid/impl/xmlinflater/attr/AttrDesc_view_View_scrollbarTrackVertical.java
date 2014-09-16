package org.itsnat.droid.impl.xmlinflater.attr;

import android.graphics.drawable.Drawable;

import org.itsnat.droid.impl.util.MiscUtil;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_scrollbarTrackVertical extends AttrDesc_view_View_scrollbar_Base
{
    public AttrDesc_view_View_scrollbarTrackVertical(ClassDescViewBased parent)
    {
        super(parent,"scrollbarTrackVertical","mScrollCache","scrollBar","setVerticalTrackDrawable",
                MiscUtil.resolveClass("android.view.View$ScrollabilityCache"), MiscUtil.resolveClass("android.widget.ScrollBarDrawable"),
                Drawable.class);
    }
}
