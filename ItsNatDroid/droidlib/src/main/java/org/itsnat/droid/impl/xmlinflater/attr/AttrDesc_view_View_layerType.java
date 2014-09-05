package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_layerType extends AttrDescReflecSingleName
{
    static Map<String, Integer> valueMap = new HashMap<String, Integer>();
    static
    {
        valueMap.put("none", View.LAYER_TYPE_NONE);
        valueMap.put("software",View.LAYER_TYPE_SOFTWARE);
        valueMap.put("hardware",View.LAYER_TYPE_HARDWARE);
    }

    public AttrDesc_view_View_layerType(ClassDescViewBased parent)
    {
        super(parent,"layerType",valueMap,"none");
    }

    @Override
    protected void callMethod(View view, Object convertedValue)
    {
        int layerType = (Integer)convertedValue;
        view.setLayerType(layerType,null);
    }

}
