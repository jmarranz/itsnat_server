package org.itsnat.droid.impl.xmlinflater.attr.view;

import android.graphics.drawable.Drawable;

import org.itsnat.droid.impl.util.MiscUtil;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_scrollbarThumbVertical extends AttrDesc_view_View_scrollbar_Base
{
    public AttrDesc_view_View_scrollbarThumbVertical(ClassDescViewBased parent)
    {
        super(parent,"scrollbarThumbVertical","mScrollCache","scrollBar","setVerticalThumbDrawable",
                MiscUtil.resolveClass("android.view.View$ScrollabilityCache"), MiscUtil.resolveClass("android.widget.ScrollBarDrawable"),
                Drawable.class);
    }
}
