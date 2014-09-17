package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.widget.LinearLayout;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMultipleName;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_LinearLayout_showDividers extends AttrDescReflecMultipleName
{
    static Map<String, Integer> valueMap = new HashMap<String, Integer>( 4 );
    static
    {
        valueMap.put("none", LinearLayout.SHOW_DIVIDER_NONE);
        valueMap.put("beginning",LinearLayout.SHOW_DIVIDER_BEGINNING);
        valueMap.put("middle",LinearLayout.SHOW_DIVIDER_MIDDLE);
        valueMap.put("end",LinearLayout.SHOW_DIVIDER_END);
    }

    public AttrDesc_widget_LinearLayout_showDividers(ClassDescViewBased parent)
    {
        super(parent,"showDividers",valueMap,"none");
    }

}
