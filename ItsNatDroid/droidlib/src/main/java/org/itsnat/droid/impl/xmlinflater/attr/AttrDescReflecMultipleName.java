package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingAttrTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.Map;

/**
 * Created by jmarranz on 1/05/14.
 */
public class AttrDescReflecMultipleName extends AttrDescReflection
{
    protected Map<String, Integer> valueMap;
    protected String defaultName;

    public AttrDescReflecMultipleName(ClassDescViewBased parent, String name, String methodName,Map<String, Integer> valueMap,String defaultName)
    {
        super(parent,name,methodName);
        this.valueMap = valueMap;
        this.defaultName = defaultName;
    }

    public AttrDescReflecMultipleName(ClassDescViewBased parent, String name,Map<String, Integer> valueMap,String defaultName)
    {
        super(parent, name);
        this.valueMap = valueMap;
        this.defaultName = defaultName;
    }

    protected Class<?> getClassParam()
    {
        return int.class;
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingAttrTasks pending)
    {
        int valueInt = parseMultipleName(value, valueMap);
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
}
