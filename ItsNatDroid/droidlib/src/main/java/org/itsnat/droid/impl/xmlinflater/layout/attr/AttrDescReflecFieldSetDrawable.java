package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.graphics.drawable.Drawable;
import android.view.View;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescReflecFieldSetDrawable extends AttrDescReflecFieldSet
{
    protected String defaultValue;

    public AttrDescReflecFieldSetDrawable(ClassDescViewBased parent,String name,String fieldName,String defaultValue)
    {
        super(parent,name,fieldName);
        this.defaultValue = defaultValue;
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        Drawable convertedValue = getDrawable(value, view.getContext());

        setField(view,convertedValue);
    }

    public void removeAttribute(View view)
    {
        if (defaultValue != null)
            setAttribute(view,defaultValue,null,null);
    }
}
