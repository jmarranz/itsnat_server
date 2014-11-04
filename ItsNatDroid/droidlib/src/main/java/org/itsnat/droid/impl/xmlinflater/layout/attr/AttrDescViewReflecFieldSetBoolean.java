package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescViewReflecFieldSetBoolean extends AttrDescViewReflecFieldSet
{
    protected boolean defaultValue;

    public AttrDescViewReflecFieldSetBoolean(ClassDescViewBased parent, String name, String fieldName, boolean defaultValue)
    {
        super(parent,name,fieldName);
        this.defaultValue = defaultValue;
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        boolean convertedValue = getBoolean(value,view.getContext());

        setField(view,convertedValue);
    }

    public void removeAttribute(View view)
    {
        setField(view,defaultValue);
    }

}
