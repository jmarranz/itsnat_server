package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodSingleName;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_view_View_drawingCacheQuality extends AttrDescViewReflecMethodSingleName
{
    static Map<String, Integer> valueMap = new HashMap<String, Integer>( 3 );
    static
    {
        valueMap.put("auto", View.DRAWING_CACHE_QUALITY_AUTO);
        valueMap.put("low",View.DRAWING_CACHE_QUALITY_LOW);
        valueMap.put("high",View.DRAWING_CACHE_QUALITY_HIGH);
    }

    public AttrDescView_view_View_drawingCacheQuality(ClassDescViewBased parent)
    {
        super(parent,"drawingCacheQuality",int.class,valueMap,"auto");
    }

}
