package org.itsnat.droid.impl.xmlinflater.attr;

import android.graphics.drawable.Drawable;
import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescReflecFieldSetDrawable extends AttrDescReflecFieldSet
{
    public AttrDescReflecFieldSetDrawable(ClassDescViewBased parent,String name,String fieldName)
    {
        super(parent,name,fieldName);
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        Drawable convertedValue = getDrawable(value, view.getContext());

        setField(view,convertedValue);
    }

    public void removeAttribute(View view)
    {
        // No tengo claro de poner a null
    }
}
