package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescReflecMethodLong extends AttrDescReflecMethod
{
    protected Long defaultValue;

    public AttrDescReflecMethodLong(ClassDescViewBased parent, String name, String methodName, Long defaultValue)
    {
        super(parent,name,methodName,getClassParam());
        this.defaultValue = defaultValue;
    }

    public AttrDescReflecMethodLong(ClassDescViewBased parent, String name, Long defaultValue)
    {
        super(parent,name,getClassParam());
        this.defaultValue = defaultValue;
    }

    protected static Class<?> getClassParam()
    {
        return long.class;
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int convValue = getInteger(value, view.getContext()); // No hay un Resources.getLong(), en Android aunque el atributo contenedor sea long el dato se suele manejar como int
        callMethod(view, convValue);
    }

    public void removeAttribute(View view)
    {
        // En el caso de defaultValue nulo es que no sabemos qué poner, es el caso por ejemplo de poner a cero el tamaño texto, no tiene sentido, se tendría que extraer el tamaño por defecto del Theme actual, un follón y total será muy raro
        if (defaultValue != null)
            callMethod(view, defaultValue);
    }
}
