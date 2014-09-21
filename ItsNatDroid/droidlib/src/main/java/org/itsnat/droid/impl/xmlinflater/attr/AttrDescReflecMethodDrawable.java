package org.itsnat.droid.impl.xmlinflater.attr;

import android.graphics.drawable.Drawable;
import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 1/05/14.
 */
public class AttrDescReflecMethodDrawable extends AttrDescReflecMethod
{
    protected String defaultValue;

    public AttrDescReflecMethodDrawable(ClassDescViewBased parent, String name, String methodName, String defaultValue)
    {
        super(parent,name,methodName);
        this.defaultValue = defaultValue;
    }

    public AttrDescReflecMethodDrawable(ClassDescViewBased parent, String name, String defaultValue)
    {
        super(parent, name);
        this.defaultValue = defaultValue;
    }

    protected Class<?> getClassParam()
    {
        return Drawable.class;
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        Drawable convValue = getDrawable(value,view.getContext());
        callMethod(view, convValue);
    }

    public void removeAttribute(View view)
    {
        if (defaultValue != null) // Para especificar null se ha de usar "@null"
            setAttribute(view,defaultValue,null,null); // defaultValue puede ser null (ej attr background), también valdría "@null" en el atributo
    }
}
