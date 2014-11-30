package org.itsnat.droid.impl.xmlinflater.drawable.attr;

import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescDrawable;

import java.util.Map;

/**
 * Created by jmarranz on 1/05/14.
 */
public class AttrDescDrawableReflecMethodSingleName<Treturn,Tdrawable> extends AttrDescDrawableReflecMethodNameBased<Treturn,Tdrawable>
{
    public AttrDescDrawableReflecMethodSingleName(ClassDescDrawable parent, String name, String methodName, Class classParam, Map<String,Treturn> valueMap)
    {
        super(parent,name,methodName,classParam,valueMap);
    }

    public AttrDescDrawableReflecMethodSingleName(ClassDescDrawable parent, String name, Class classParam, Map<String,Treturn> valueMap)
    {
        super(parent, name,classParam,valueMap);
    }

    @Override
    protected Treturn parseNameBasedValue(String value)
    {
        return this.<Treturn>parseSingleName(value, valueMap);
    }
}
