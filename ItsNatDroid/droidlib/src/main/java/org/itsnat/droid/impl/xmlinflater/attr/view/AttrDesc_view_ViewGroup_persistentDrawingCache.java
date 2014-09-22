package org.itsnat.droid.impl.xmlinflater.attr.view;

import android.view.ViewGroup;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodMultipleName;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_ViewGroup_persistentDrawingCache extends AttrDescReflecMethodMultipleName
{
    static Map<String, Integer> valueMap = new HashMap<String, Integer>( 4 );
    static
    {
        valueMap.put("none",ViewGroup.PERSISTENT_NO_CACHE);
        valueMap.put("animation",ViewGroup.PERSISTENT_ANIMATION_CACHE);
        valueMap.put("scrolling",ViewGroup.PERSISTENT_SCROLLING_CACHE);
        valueMap.put("all",ViewGroup.PERSISTENT_ALL_CACHES);
    }

    public AttrDesc_view_ViewGroup_persistentDrawingCache(ClassDescViewBased parent)
    {
        super(parent,"persistentDrawingCache",valueMap,"scrolling");
    }

}
