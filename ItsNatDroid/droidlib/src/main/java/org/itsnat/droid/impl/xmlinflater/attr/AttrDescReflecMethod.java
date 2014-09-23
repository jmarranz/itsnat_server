package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class AttrDescReflecMethod extends AttrDesc
{
    protected MethodContainer method;

    public AttrDescReflecMethod(ClassDescViewBased parent, String name, String methodName,Class classParam)
    {
        super(parent,name);
        this.method = new MethodContainer(parent,methodName,classParam != null ? new Class[]{classParam} : null);
    }

    public AttrDescReflecMethod(ClassDescViewBased parent, String name,Class classParam)
    {
        this(parent,name,genMethodName(name),classParam);
    }

    public static String genMethodName(String name)
    {
        return "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    protected void callMethod(View view, Object convertedValue)
    {
        method.call(view,convertedValue);
    }

}
