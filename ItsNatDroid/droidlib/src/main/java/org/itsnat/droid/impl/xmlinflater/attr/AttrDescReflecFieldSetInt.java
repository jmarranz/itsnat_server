package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescReflecFieldSetInt extends AttrDescReflecFieldSet
{
    protected int defaultValue;

    public AttrDescReflecFieldSetInt(ClassDescViewBased parent, String name, String fieldName, int defaultValue)
    {
        super(parent,name,fieldName);
        this.defaultValue = defaultValue;
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int convertedValue = getInteger(value,view.getContext());

        setField(view,convertedValue);
    }

    public void removeAttribute(View view)
    {
        setField(view,defaultValue);
    }

}
