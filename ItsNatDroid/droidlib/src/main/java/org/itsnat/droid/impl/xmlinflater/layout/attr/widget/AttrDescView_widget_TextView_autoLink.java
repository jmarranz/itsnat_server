package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodMultipleName;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_TextView_autoLink extends AttrDescViewReflecMethodMultipleName
{
    static Map<String, Integer> valueMap = new HashMap<String, Integer>( 6 );
    static
    {
        valueMap.put("none",0x00);
        valueMap.put("web",0x01);
        valueMap.put("email",0x02);
        valueMap.put("phone",0x04);
        valueMap.put("map",0x08);
        valueMap.put("all",0x0f);
    }

    public AttrDescView_widget_TextView_autoLink(ClassDescViewBased parent)
    {
        super(parent,"autoLink","setAutoLinkMask",valueMap,"none");
    }

}
