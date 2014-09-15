package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.ViewGroup;

import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_ViewGroup_descendantFocusability extends AttrDescReflecSingleName
{
    static Map<String, Integer> valueMap = new HashMap<String, Integer>( 3 );
    static
    {
        valueMap.put("beforeDescendants", ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        valueMap.put("afterDescendants",  ViewGroup.FOCUS_AFTER_DESCENDANTS);
        valueMap.put("blocksDescendants", ViewGroup.FOCUS_BLOCK_DESCENDANTS);
    }

    public AttrDesc_view_ViewGroup_descendantFocusability(ClassDescViewBased parent)
    {
        super(parent,"descendantFocusability",valueMap,"beforeDescendants");
    }

}
