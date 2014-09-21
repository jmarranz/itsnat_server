package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.Map;

/**
 * Created by jmarranz on 1/05/14.
 */
public abstract class AttrDescReflecMethodNameBased<T> extends AttrDescReflecMethod
{
    protected Map<String, T> valueMap;
    protected String defaultName;
    protected Class classParam;

    public AttrDescReflecMethodNameBased(ClassDescViewBased parent, String name, String methodName, Class classParam, Map<String, T> valueMap, String defaultName)
    {
        super(parent,name,methodName);
        this.classParam = classParam;
        this.valueMap = valueMap;
        this.defaultName = defaultName;
    }

    public AttrDescReflecMethodNameBased(ClassDescViewBased parent, String name, Class classParam, Map<String, T> valueMap, String defaultName)
    {
        super(parent, name);
        this.classParam = classParam;
        this.valueMap = valueMap;
        this.defaultName = defaultName;
    }

    protected Class<?> getClassParam()
    {
        return classParam;
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        T valueRes = parseNameBasedValue(value);
        callMethod(view, valueRes);
    }

    public void removeAttribute(View view)
    {
        if (defaultName != null)
        {
            if (defaultName.equals("")) callMethod(view, -1); // Android utiliza el -1 de vez en cuando como valor por defecto
            else setAttribute(view, defaultName, null,null);
        }
    }

    protected abstract <T> T parseNameBasedValue(String value);
}
