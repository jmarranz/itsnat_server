package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.view.View;
import android.widget.RelativeLayout;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

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

    public void setAttribute(final View view,final String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        Runnable task = new Runnable(){
            @Override
            public void run()
            {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
                if (!value.isEmpty())
                {
                    int viewId = getIdentifierAddIfNecessary(value, view.getContext());
                    params.addRule(selector, viewId);
                }
                else
                {
                    // El valor vacío del atributo es un caso específico de ItsNatDroid, útil en actualización del Layout via DOM para
                    // indicar que quitamos la regla. removeRule es el método adecuado pero es posterior a 4.0.3
                    params.addRule(selector, 0);
                }
            }};

        if (oneTimeAttrProcess != null)
        {
            oneTimeAttrProcess.addLayoutParamsTask(task);
        }
        else
        {
            task.run();
            view.setLayoutParams(view.getLayoutParams());
        }
    }

    public void removeAttribute(View view)
    {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
        params.addRule(selector, 0);

        view.setLayoutParams(view.getLayoutParams());
    }
}
