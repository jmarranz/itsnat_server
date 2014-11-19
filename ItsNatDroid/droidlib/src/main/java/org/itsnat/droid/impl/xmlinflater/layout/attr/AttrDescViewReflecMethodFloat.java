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
public class AttrDescViewReflecMethodFloat extends AttrDescViewReflecMethod
{
    protected Float defaultValue;

    public AttrDescViewReflecMethodFloat(ClassDescViewBased parent, String name, String methodName, Float defaultValue)
    {
        super(parent,name,methodName,getClassParam());
        this.defaultValue = defaultValue;
    }

    public AttrDescViewReflecMethodFloat(ClassDescViewBased parent, String name, Float defaultValue)
    {
        super(parent,name,getClassParam());
        this.defaultValue = defaultValue;
    }

    protected static Class<?> getClassParam()
    {
        return float.class;
    }

    public void setAttribute(View view, AttrParsed attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        float convValue = getFloat(attr.getValue(),ctx);
        callMethod(view, convValue);
    }

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        // En el caso de defaultValue nulo es que no sabemos qué poner, es el caso por ejemplo de poner a cero el tamaño texto, no tiene sentido, se tendría que extraer el tamaño por defecto del Theme actual, un follón y total será muy raro
        if (defaultValue != null)
            callMethod(view, defaultValue);
    }
}
