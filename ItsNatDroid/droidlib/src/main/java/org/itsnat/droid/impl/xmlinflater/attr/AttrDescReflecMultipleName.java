package org.itsnat.droid.impl.xmlinflater.attr;

import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.Map;

/**
 * Created by jmarranz on 1/05/14.
 */
public class AttrDescReflecMultipleName extends AttrDescReflecNameBased<Integer>
{
    public AttrDescReflecMultipleName(ClassDescViewBased parent, String name, String methodName,Map<String, Integer> valueMap,String defaultName)
    {
        super(parent,name,methodName,int.class,valueMap,defaultName);
    }

    public AttrDescReflecMultipleName(ClassDescViewBased parent, String name,Map<String, Integer> valueMap,String defaultName)
    {
        super(parent, name,int.class,valueMap,defaultName);
    }

    @Override
    protected Integer parseNameBasedValue(String value)
    {
        return parseMultipleName(value, valueMap);
    }


}
