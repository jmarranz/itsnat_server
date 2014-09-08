package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;
import android.widget.RelativeLayout;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_layout_rellayout_byId extends AttrDesc
{
    protected int selector;

    public AttrDesc_view_View_layout_rellayout_byId(ClassDescViewBased parent,String name,int selector)
    {
        super(parent,name);
        this.selector = selector;
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
        if (!value.isEmpty())
        {
            int viewId = getIdentifier(value, view.getContext());
            params.addRule(selector, viewId);
        }
        else
        {
            // El valor vacío del atributo es un caso específico de ItsNatDroid, útil en actualización del Layout via DOM para
            // indicar que quitamos la regla. removeRule es el método adecuado pero es posterior a 4.0.3
            params.addRule(selector, 0);
        }

        if (oneTimeAttrProcess != null) oneTimeAttrProcess.setNeededSetLayoutParams();
        else view.setLayoutParams(view.getLayoutParams());
    }

    public void removeAttribute(View view)
    {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
        params.addRule(selector, 0);

        view.setLayoutParams(view.getLayoutParams());
    }
}
