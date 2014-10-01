package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.widget.LinearLayout;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodSingleName;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_LinearLayout_orientation extends AttrDescReflecMethodSingleName
{
    static Map<String, Integer> valueMap = new HashMap<String, Integer>( 2 );
    static
    {
        valueMap.put("horizontal", LinearLayout.HORIZONTAL);
        valueMap.put("vertical", LinearLayout.VERTICAL);
    }

    public AttrDesc_widget_LinearLayout_orientation(ClassDescViewBased parent)
    {
        super(parent,"orientation",int.class,valueMap,"horizontal");
    }

    protected AttrDesc_widget_LinearLayout_orientation(ClassDescViewBased parent,String defaultValue)
    {
        super(parent,"orientation",int.class,valueMap,defaultValue); // Este constructor se llama desde una clase derivada
    }
}
