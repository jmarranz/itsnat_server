package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.widget.GridLayout;

import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodSingleName;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_GridLayout_alignmentMode extends AttrDescViewReflecMethodSingleName
{
    static Map<String, Integer> valueMap = new HashMap<String, Integer>( 2 );
    static
    {
        valueMap.put("alignBounds", GridLayout.ALIGN_BOUNDS);
        valueMap.put("alignMargins",GridLayout.ALIGN_MARGINS);
    }

    public AttrDescView_widget_GridLayout_alignmentMode(ClassDescViewBased parent)
    {
        super(parent,"alignmentMode",int.class,valueMap,"alignMargins");
    }

}
