package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.widget.GridLayout;

import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodSingleName;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_GridLayout_alignmentMode extends AttrDescReflecMethodSingleName
{
    static Map<String, Integer> valueMap = new HashMap<String, Integer>( 2 );
    static
    {
        valueMap.put("alignBounds", GridLayout.ALIGN_BOUNDS);
        valueMap.put("alignMargins",GridLayout.ALIGN_MARGINS);
    }

    public AttrDesc_widget_GridLayout_alignmentMode(ClassDescViewBased parent)
    {
        super(parent,"alignmentMode",int.class,valueMap,"alignMargins");
    }

}
