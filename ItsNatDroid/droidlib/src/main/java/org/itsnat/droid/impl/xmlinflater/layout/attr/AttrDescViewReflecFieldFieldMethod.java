package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.FieldContainer;
import org.itsnat.droid.impl.xmlinflater.MethodContainer;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class AttrDescViewReflecFieldFieldMethod extends AttrDescView
{
    protected FieldContainer field1;
    protected FieldContainer field2;
    protected MethodContainer method;

    public AttrDescViewReflecFieldFieldMethod(ClassDescViewBased parent, String name, String fieldName1, String fieldName2, String methodName, Class field2Class, Class methodClass, Class paramClass)
    {
        super(parent,name);

        this.field1 = new FieldContainer(parent,fieldName1);
        this.field2 = new FieldContainer(field2Class,fieldName2);
        this.method = new MethodContainer(methodClass,methodName,new Class[]{paramClass});
    }

    protected void callFieldFieldMethod(View view,Object convertedValue)
    {
        Object fieldValue1 = field1.get(view);
        Object fieldValue2 = field2.get(fieldValue1);
        method.invoke(fieldValue2, convertedValue);
    }

}
