package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.widget.LinearLayout;

import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodMultipleName;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_LinearLayout_showDividers extends AttrDescViewReflecMethodMultipleName
{
    static Map<String, Integer> valueMap = new HashMap<String, Integer>( 4 );
    static
    {
        valueMap.put("none", LinearLayout.SHOW_DIVIDER_NONE);
        valueMap.put("beginning",LinearLayout.SHOW_DIVIDER_BEGINNING);
        valueMap.put("middle",LinearLayout.SHOW_DIVIDER_MIDDLE);
        valueMap.put("end",LinearLayout.SHOW_DIVIDER_END);
    }

    public AttrDescView_widget_LinearLayout_showDividers(ClassDescViewBased parent)
    {
        super(parent,"showDividers",valueMap,"none");
    }

}
