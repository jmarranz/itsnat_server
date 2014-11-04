package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.graphics.drawable.Drawable;

import org.itsnat.droid.impl.util.MiscUtil;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_view_View_scrollbarThumbHorizontal extends AttrDescView_view_View_scrollbar_Base
{
    public AttrDescView_view_View_scrollbarThumbHorizontal(ClassDescViewBased parent)
    {
        super(parent,"scrollbarThumbHorizontal","mScrollCache","scrollBar","setHorizontalThumbDrawable",
                MiscUtil.resolveClass("android.view.View$ScrollabilityCache"), MiscUtil.resolveClass("android.widget.ScrollBarDrawable"),
                Drawable.class);
    }
}
