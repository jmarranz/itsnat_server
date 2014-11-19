package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.content.Context;
import android.view.View;

import org.itsnat.droid.impl.dom.AttrParsed;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescViewReflecFieldSetInt extends AttrDescViewReflecFieldSet
{
    protected int defaultValue;

    public AttrDescViewReflecFieldSetInt(ClassDescViewBased parent, String name, String fieldName, int defaultValue)
    {
        super(parent,name,fieldName);
        this.defaultValue = defaultValue;
    }

    public void setAttribute(View view, AttrParsed attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int convertedValue = getInteger(attr.getValue(),ctx);

        setField(view,convertedValue);
    }

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        setField(view,defaultValue);
    }

}
