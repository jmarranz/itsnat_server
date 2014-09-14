package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.Map;

/**
 * Created by jmarranz on 1/05/14.
 */
public abstract class AttrDescReflecNameBased extends AttrDescMethodReflection
{
    protected Map<String, Integer> valueMap;
    protected String defaultName;

    public AttrDescReflecNameBased(ClassDescViewBased parent, String name, String methodName, Map<String, Integer> valueMap, String defaultName)
    {
        super(parent,name,methodName);
        this.valueMap = valueMap;
        this.defaultName = defaultName;
    }

    public AttrDescReflecNameBased(ClassDescViewBased parent, String name, Map<String, Integer> valueMap, String defaultName)
    {
        super(parent, name);
        this.valueMap = valueMap;
        this.defaultName = defaultName;
    }

    protected Class<?> getClassParam()
    {
        return int.class;
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int valueInt = parseNameBasedValue(value);
        callMethod(view, valueInt);
    }

    public void removeAttribute(View view)
    {
        if (defaultName != null)
        {
            if (defaultName.equals("")) callMethod(view, -1); // Android utiliza el -1 de vez en cuando como valor por defecto
            else setAttribute(view, defaultName, null,null);
        }
    }

    protected abstract int parseNameBasedValue(String value);
}
