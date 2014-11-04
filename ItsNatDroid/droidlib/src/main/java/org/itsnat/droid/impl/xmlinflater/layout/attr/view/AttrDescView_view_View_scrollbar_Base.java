package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.graphics.drawable.Drawable;
import android.view.View;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecFieldFieldMethod;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class AttrDescView_view_View_scrollbar_Base extends AttrDescViewReflecFieldFieldMethod
{
    public AttrDescView_view_View_scrollbar_Base(ClassDescViewBased parent, String name, String fieldName1, String fieldName2, String methodName, Class field2Class, Class methodClass, Class paramClass)
    {
        super(parent,name,fieldName1,fieldName2,methodName,field2Class,methodClass,paramClass);
    }

    public void setAttribute(final View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        final Drawable convertedValue = getDrawable(value, view.getContext());

        if (oneTimeAttrProcess != null)
        {
            // Delegamos al final para que esté totalmente claro si hay o no scrollbars
            oneTimeAttrProcess.addLastTask(new Runnable()
            {
                @Override
                public void run()
                {
                    callFieldFieldMethod(view, convertedValue);
                }
            });
        }
        else
        {
            callFieldFieldMethod(view, convertedValue);
        }
    }

    public void removeAttribute(View view)
    {
        // No se que hacer, el null no es el valor por defecto
    }

}
