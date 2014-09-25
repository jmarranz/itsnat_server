package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class AttrDescReflecFieldMethod extends AttrDesc
{
    protected FieldContainer field;
    protected MethodContainer method;

    public AttrDescReflecFieldMethod(ClassDescViewBased parent, String name, String fieldName, String methodName, Class methodClass, Class paramClass)
    {
        super(parent,name);
        this.field = new FieldContainer(parent,fieldName);
        this.method = new MethodContainer(methodClass,methodName,new Class[]{paramClass});
    }

    protected void callFieldMethod(View view, Object convertedValue)
    {
        Object fieldValue = field.get(view);
        method.invoke(fieldValue,convertedValue);
    }
}
