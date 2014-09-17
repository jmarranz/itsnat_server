package org.itsnat.droid.impl.xmlinflater.attr.view;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecSingleName;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_scrollbarStyle extends AttrDescReflecSingleName
{
    static Map<String, Integer> valueMap = new HashMap<String, Integer>( 4 );
    static
    {
        valueMap.put("insideOverlay", View.SCROLLBARS_INSIDE_OVERLAY);
        valueMap.put("insideInset",   View.SCROLLBARS_INSIDE_INSET);
        valueMap.put("outsideOverlay",View.SCROLLBARS_OUTSIDE_OVERLAY);
        valueMap.put("outsideInset",View.SCROLLBARS_OUTSIDE_INSET);
    }

    public AttrDesc_view_View_scrollbarStyle(ClassDescViewBased parent)
    {
        super(parent,"scrollbarStyle","setScrollBarStyle",valueMap,"insideOverlay");
    }

}
