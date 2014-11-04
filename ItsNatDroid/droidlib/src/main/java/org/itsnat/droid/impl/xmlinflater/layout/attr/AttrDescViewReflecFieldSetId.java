package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescViewReflecFieldSetId extends AttrDescViewReflecFieldSet
{
    protected Integer defaultValue;

    public AttrDescViewReflecFieldSetId(ClassDescViewBased parent, String name, String fieldName, Integer defaultValue)
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
