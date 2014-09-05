package org.itsnat.droid.impl.xmlinflater.attr;

import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.Map;

/**
 * Created by jmarranz on 1/05/14.
 */
public abstract class AttrDescReflecSingleName extends AttrDescReflecNameBased
{
    public AttrDescReflecSingleName(ClassDescViewBased parent, String name, String methodName,Map<String, Integer> valueMap,String defaultName)
    {
        super(parent,name,methodName,valueMap,defaultName);
    }

    public AttrDescReflecSingleName(ClassDescViewBased parent, String name,Map<String, Integer> valueMap,String defaultName)
    {
        super(parent, name, valueMap, defaultName);
    }

    @Override
    protected int parseNameBasedValue(String value)
    {
        return parseSingleName(value, valueMap);
    }
}
