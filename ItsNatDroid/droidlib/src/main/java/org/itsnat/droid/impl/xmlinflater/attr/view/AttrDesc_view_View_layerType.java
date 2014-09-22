package org.itsnat.droid.impl.xmlinflater.attr.view;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_layerType extends AttrDesc
{
    static Map<String, Integer> valueMap = new HashMap<String, Integer>( 3 );
    static
    {
        valueMap.put("none", View.LAYER_TYPE_NONE);
        valueMap.put("software",View.LAYER_TYPE_SOFTWARE);
        valueMap.put("hardware",View.LAYER_TYPE_HARDWARE);
    }

    public AttrDesc_view_View_layerType(ClassDescViewBased parent)
    {
        super(parent,"layerType");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int convertedValue = AttrDesc.<Integer>parseSingleName(value, valueMap);

        int layerType = (Integer)convertedValue;
        view.setLayerType(layerType,null);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view, "none", null,null);
    }


}
