package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;
import android.widget.RelativeLayout;

import org.itsnat.droid.impl.xmlinflater.ParsePhase;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBase;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescViewViewLayoutBelow extends AttrDesc
{
    public AttrDescViewViewLayoutBelow(ClassDescViewBase parent)
    {
        super(parent,"layout_below");
    }

    public void setAttribute(View view, String value, ParsePhase parsePhase)
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

        if (parsePhase != null) parsePhase.neededSetLayoutParams = true;
        else view.setLayoutParams(view.getLayoutParams());
    }

    public void removeAttribute(View view)
    {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, 0);

        view.setLayoutParams(view.getLayoutParams());
    }
}
