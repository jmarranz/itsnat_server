package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_layerType extends AttrDesc
{
    public AttrDesc_view_View_layerType(ClassDescViewBased parent)
    {
        super(parent,"layerType");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess)
    {
        int intValue;
        if ("none".equals(value))
            intValue = View.LAYER_TYPE_NONE;
        else if ("software".equals(value))
            intValue = View.LAYER_TYPE_SOFTWARE;
        else if ("hardware".equals(value))
            intValue = View.LAYER_TYPE_HARDWARE;
        else
            throw new ItsNatDroidException("Unrecognized value " + value + " for attribute " + name);

        view.setLayerType(intValue,null);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"none",null);
    }
}
