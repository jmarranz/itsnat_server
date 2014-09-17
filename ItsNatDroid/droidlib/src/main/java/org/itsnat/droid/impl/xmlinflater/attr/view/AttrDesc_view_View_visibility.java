package org.itsnat.droid.impl.xmlinflater.attr.view;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecSingleName;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_visibility extends AttrDescReflecSingleName
{
    static Map<String, Integer> valueMap = new HashMap<String, Integer>( 3 );
    static
    {
        valueMap.put("visible", View.VISIBLE);
        valueMap.put("invisible",View.INVISIBLE);
        valueMap.put("gone",View.GONE);
    }

    public AttrDesc_view_View_visibility(ClassDescViewBased parent)
    {
        super(parent,"visibility",int.class,valueMap,"visible");
    }
}
