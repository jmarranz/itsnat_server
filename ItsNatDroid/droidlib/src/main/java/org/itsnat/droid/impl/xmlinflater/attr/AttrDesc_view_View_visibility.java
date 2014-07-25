package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_visibility extends AttrDesc
{
    public AttrDesc_view_View_visibility(ClassDescViewBased parent)
    {
        super(parent,"visibility");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess)
    {
        int intValue;
        if ("visible".equals(value))
            intValue = View.VISIBLE;
        else if ("invisible".equals(value))
            intValue = View.INVISIBLE;
        else if ("gone".equals(value))
            intValue = View.GONE;
        else
            throw new ItsNatDroidException("Unrecognized value " + value + " for attribute " + name);

        view.setVisibility(intValue);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"visible",null);
    }
}
