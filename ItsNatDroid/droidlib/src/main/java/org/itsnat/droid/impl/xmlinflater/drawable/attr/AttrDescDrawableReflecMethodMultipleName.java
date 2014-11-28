package org.itsnat.droid.impl.xmlinflater.drawable.attr;

import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescDrawable;

import java.util.Map;

/**
 * Created by jmarranz on 1/05/14.
 */
public class AttrDescDrawableReflecMethodMultipleName<Tdrawable> extends AttrDescDrawableReflecMethodNameBased<Integer,Tdrawable>
{
    public AttrDescDrawableReflecMethodMultipleName(ClassDescDrawable parent, String name, String methodName, Map<String, Integer> valueMap)
    {
        super(parent,name,methodName,getClassParam(),valueMap);
    }

    public AttrDescDrawableReflecMethodMultipleName(ClassDescDrawable parent, String name, Map<String, Integer> valueMap)
    {
        super(parent, name,getClassParam(),valueMap);
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
