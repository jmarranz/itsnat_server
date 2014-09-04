package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;
import android.widget.RelativeLayout;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingAttrTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_layout_below extends AttrDesc
{
    public AttrDesc_view_View_layout_below(ClassDescViewBased parent)
    {
        super(parent,"layout_below");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingAttrTasks pending)
    {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
        if (value.isEmpty())
        {
            // El valor vacío del atributo es un caso específico de ItsNatDroid, útil en actualización del Layout via DOM para
            // indicar que quitamos la regla. removeRule es el método adecuado pero es posterior a 4.0.3
            params.addRule(RelativeLayout.BELOW, 0);
        }
        else
        {
            int viewId = getIdentifier(value, view.getContext());
            params.addRule(RelativeLayout.BELOW, viewId);
        }

        if (oneTimeAttrProcess != null) oneTimeAttrProcess.neededSetLayoutParams = true;
        else view.setLayoutParams(view.getLayoutParams());
    }

    public void removeAttribute(View view)
    {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, 0);

        view.setLayoutParams(view.getLayoutParams());
    }
}
