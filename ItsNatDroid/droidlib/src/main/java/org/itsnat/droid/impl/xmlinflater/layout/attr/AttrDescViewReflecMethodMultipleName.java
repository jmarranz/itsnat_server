package org.itsnat.droid.impl.xmlinflater.layout.attr;

import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.Map;

/**
 * Created by jmarranz on 1/05/14.
 */
public class AttrDescViewReflecMethodMultipleName extends AttrDescViewReflecMethodNameBased<Integer>
{
    public AttrDescViewReflecMethodMultipleName(ClassDescViewBased parent, String name, String methodName, Map<String, Integer> valueMap, String defaultName)
    {
        super(parent,name,methodName,getClassParam(),valueMap,defaultName);
    }

    public AttrDescViewReflecMethodMultipleName(ClassDescViewBased parent, String name, Map<String, Integer> valueMap, String defaultName)
    {
        super(parent, name,getClassParam(),valueMap,defaultName);
    }

    protected static Class<?> getClassParam()
    {
        return int.class;
    }

    @Override
    protected Integer parseNameBasedValue(String value)
    {
        return parseMultipleName(value, valueMap);
    }
}
