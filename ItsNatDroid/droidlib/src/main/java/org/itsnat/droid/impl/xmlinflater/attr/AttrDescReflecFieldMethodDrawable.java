package org.itsnat.droid.impl.xmlinflater.attr;

import android.graphics.drawable.Drawable;
import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescReflecFieldMethodDrawable extends AttrDescReflecFieldMethod
{
    protected String defaultValue;

    public AttrDescReflecFieldMethodDrawable(ClassDescViewBased parent,String name, String fieldName, String methodName, Class methodClass,String defaultValue)
    {
        super(parent,name,fieldName,methodName,methodClass,Drawable.class);
        this.defaultValue = defaultValue;
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        Drawable convertedValue = getDrawable(value, view.getContext());

        callFieldMethod(view, convertedValue);
    }

    public void removeAttribute(View view)
    {
        if (defaultValue != null) // Para especificar null se ha de usar "@null"
            setAttribute(view,defaultValue,null,null); // defaultValue puede ser null (ej attr background), también valdría "@null" en el atributo
    }
}
