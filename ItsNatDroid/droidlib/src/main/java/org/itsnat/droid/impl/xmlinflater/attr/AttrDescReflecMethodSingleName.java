package org.itsnat.droid.impl.xmlinflater.attr;

import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.Map;

/**
 * Created by jmarranz on 1/05/14.
 */
public abstract class AttrDescReflecMethodSingleName<T> extends AttrDescReflecMethodNameBased<T>
{
    public AttrDescReflecMethodSingleName(ClassDescViewBased parent, String name, String methodName, Class classParam, Map<String, T> valueMap, String defaultName)
    {
        super(parent,name,methodName,classParam,valueMap,defaultName);
    }

    public AttrDescReflecMethodSingleName(ClassDescViewBased parent, String name, Class classParam, Map<String, T> valueMap, String defaultName)
    {
        super(parent, name,classParam,valueMap, defaultName);
    }

    @Override
    protected T parseNameBasedValue(String value)
    {
        return this.<T>parseSingleName(value, valueMap);
    }
}
