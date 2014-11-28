package org.itsnat.droid.impl.xmlinflater.drawable.attr;

import android.content.Context;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescDrawable;

import java.util.Map;

/**
 * Created by jmarranz on 1/05/14.
 */
public abstract class AttrDescDrawableReflecMethodNameBased<Treturn,Tdrawable> extends AttrDescDrawableReflecMethod<Tdrawable>
{
    protected Map<String, Treturn> valueMap;

    public AttrDescDrawableReflecMethodNameBased(ClassDescDrawable parent, String name, String methodName, Class classParam, Map<String, Treturn> valueMap)
    {
        super(parent,name,methodName,classParam);
        this.valueMap = valueMap;
    }

    public AttrDescDrawableReflecMethodNameBased(ClassDescDrawable parent, String name, Class classParam, Map<String, Treturn> valueMap)
    {
        super(parent, name,classParam);
        this.valueMap = valueMap;
    }

    public void setAttribute(Tdrawable draw, DOMAttr attr, XMLInflaterDrawable xmlInflater, Context ctx)
    {
        Treturn valueRes = parseNameBasedValue(attr.getValue());
        callMethod(draw, valueRes);
    }

    protected abstract Treturn parseNameBasedValue(String value);
}
