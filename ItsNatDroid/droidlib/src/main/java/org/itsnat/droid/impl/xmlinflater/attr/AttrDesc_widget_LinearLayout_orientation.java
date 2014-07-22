package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;
import android.widget.LinearLayout;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_LinearLayout_orientation extends AttrDesc
{
    public AttrDesc_widget_LinearLayout_orientation(ClassDescViewBased parent)
    {
        super(parent,"orientation");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess)
    {
        if      ("horizontal".equals(value)) ((LinearLayout)view).setOrientation(LinearLayout.HORIZONTAL);
        else if ("vertical".equals(value))   ((LinearLayout)view).setOrientation(LinearLayout.VERTICAL);
        else throw new ItsNatDroidException("Unrecognized value: " + value);
    }

    public void removeAttribute(View view)
    {
        ((LinearLayout)view).setOrientation(LinearLayout.HORIZONTAL); // Por defecto es horizontal
    }
}
