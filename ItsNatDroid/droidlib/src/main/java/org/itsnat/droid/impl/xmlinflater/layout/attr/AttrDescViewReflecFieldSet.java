package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.FieldContainer;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class AttrDescViewReflecFieldSet extends AttrDescView
{
    protected FieldContainer field;

    public AttrDescViewReflecFieldSet(ClassDescViewBased parent, String name, String fieldName)
    {
        super(parent,name);
        this.field = new FieldContainer(parent,fieldName);
    }

    protected void setField(View view,Object convertedValue)
    {
        field.set(view, convertedValue);
    }
}
