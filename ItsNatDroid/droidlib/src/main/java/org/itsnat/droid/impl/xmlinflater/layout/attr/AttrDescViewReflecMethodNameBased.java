package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.content.Context;
import android.view.View;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.Map;

/**
 * Created by jmarranz on 1/05/14.
 */
public abstract class AttrDescViewReflecMethodNameBased<Treturn> extends AttrDescViewReflecMethod
{
    protected Map<String, Treturn> valueMap;
    protected String defaultName;

    public AttrDescViewReflecMethodNameBased(ClassDescViewBased parent, String name, String methodName, Class classParam, Map<String, Treturn> valueMap, String defaultName)
    {
        super(parent,name,methodName,classParam);
        this.valueMap = valueMap;
        this.defaultName = defaultName;
    }

    public AttrDescViewReflecMethodNameBased(ClassDescViewBased parent, String name, Class classParam, Map<String, Treturn> valueMap, String defaultName)
    {
        super(parent, name,classParam);
        this.valueMap = valueMap;
        this.defaultName = defaultName;
    }

    public void setAttribute(View view, DOMAttr attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        Treturn valueRes = parseNameBasedValue(attr.getValue());
        callMethod(view, valueRes);
    }

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        if (defaultName != null)
        {
            if (defaultName.equals("")) callMethod(view, -1); // Android utiliza el -1 de vez en cuando como valor por defecto
            else setAttribute(view, defaultName,xmlInflaterLayout,ctx, null,null);
        }
    }

    protected abstract Treturn parseNameBasedValue(String value);
}
