package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;
import android.widget.LinearLayout;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_layout_weight extends AttrDesc
{
    public AttrDesc_view_View_layout_weight(ClassDescViewBased parent)
    {
        super(parent,"layout_weight");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess)
    {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)view.getLayoutParams();

        float weight = getFloat(value,view.getContext());

        params.weight = weight;

        if (oneTimeAttrProcess != null) oneTimeAttrProcess.neededSetLayoutParams = true;
        else view.setLayoutParams(view.getLayoutParams());
    }

    public void removeAttribute(View view)
    {
        // No se que hacer
    }
}
