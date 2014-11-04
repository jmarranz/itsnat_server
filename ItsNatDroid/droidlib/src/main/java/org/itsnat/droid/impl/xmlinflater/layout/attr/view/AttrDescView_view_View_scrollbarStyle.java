package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodSingleName;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_view_View_scrollbarStyle extends AttrDescViewReflecMethodSingleName
{
    static Map<String, Integer> valueMap = new HashMap<String, Integer>( 4 );
    static
    {
        valueMap.put("insideOverlay", View.SCROLLBARS_INSIDE_OVERLAY);
        valueMap.put("insideInset",   View.SCROLLBARS_INSIDE_INSET);
        valueMap.put("outsideOverlay",View.SCROLLBARS_OUTSIDE_OVERLAY);
        valueMap.put("outsideInset",View.SCROLLBARS_OUTSIDE_INSET);
    }

    public AttrDescView_view_View_scrollbarStyle(ClassDescViewBased parent)
    {
        super(parent,"scrollbarStyle","setScrollBarStyle",int.class,valueMap,"insideOverlay");
    }

}
