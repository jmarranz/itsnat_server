package org.itsnat.droid.impl.xmlinflater.attr;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.ParsePhase;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBase;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescWidgetLinearLayoutOrientation extends AttrDesc
{
    public AttrDescWidgetLinearLayoutOrientation(ClassDescViewBase parent)
    {
        super(parent,"orientation");
    }

    public void setAttribute(View view, String value, ParsePhase parsePhase)
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
