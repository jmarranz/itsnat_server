package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class AttrDescReflecFieldSet extends AttrDesc
{
    protected FieldContainer field;

    public AttrDescReflecFieldSet(ClassDescViewBased parent, String name, String fieldName)
    {
        super(parent,name);
        this.field = new FieldContainer(parent,fieldName);
    }

    protected void setField(View view,Object convertedValue)
    {
        field.set(view, convertedValue);
    }
}
