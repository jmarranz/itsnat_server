package org.itsnat.droid.impl.xmlinflater.attr;

import android.graphics.drawable.Drawable;
import android.view.View;

import org.itsnat.droid.impl.util.MiscUtil;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_scrollbarThumbHorizontal extends AttrDescReflecFieldFieldMethod
{
    public AttrDesc_view_View_scrollbarThumbHorizontal(ClassDescViewBased parent)
    {
        super(parent,"scrollbarThumbHorizontal","mScrollCache","scrollBar","setHorizontalThumbDrawable",
                MiscUtil.resolveClass("android.view.View$ScrollabilityCache"), MiscUtil.resolveClass("android.widget.ScrollBarDrawable"),
                Drawable.class);
    }

    public void setAttribute(final View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        final Drawable convertedValue = getDrawable(value, view.getContext());

        // Delegamos al final para que esté totalmente claro si hay o no scrollbars
        pending.addTask(new Runnable()
        {
            @Override
            public void run()
            {
                callFieldFieldMethod(view, convertedValue);
            }
        });
    }

    public void removeAttribute(View view)
    {
        // No se que hacer, el null no es el valor por defecto
    }

}
