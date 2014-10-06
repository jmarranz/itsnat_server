package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescReflecFieldSetId extends AttrDescReflecFieldSet
{
    protected Integer defaultValue;

    public AttrDescReflecFieldSetId(ClassDescViewBased parent, String name, String fieldName, Integer defaultValue)
    {
        super(parent,name,fieldName);
        this.defaultValue = defaultValue;
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int id = getIdentifierAddIfNecessary(value, view.getContext());

        setField(view,id);
    }

    public void removeAttribute(View view)
    {
        if (defaultValue != null)
            setField(view,defaultValue);
    }

}
