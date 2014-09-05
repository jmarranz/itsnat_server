package org.itsnat.droid.impl.xmlinflater.attr;

import android.widget.LinearLayout;

import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_LinearLayout_orientation extends AttrDescReflecSingleName
{
    static Map<String, Integer> valueMap = new HashMap<String, Integer>();
    static
    {
        valueMap.put("horizontal", LinearLayout.HORIZONTAL);
        valueMap.put("vertical",LinearLayout.VERTICAL);
    }

    public AttrDesc_widget_LinearLayout_orientation(ClassDescViewBased parent)
    {
        super(parent,"orientation",valueMap,"horizontal");
    }

}
