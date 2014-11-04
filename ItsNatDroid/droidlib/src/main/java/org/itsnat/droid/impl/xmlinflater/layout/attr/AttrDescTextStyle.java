package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.graphics.Typeface;
import android.view.View;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class AttrDescTextStyle extends AttrDesc
{
    static Map<String,Integer> valueMap = new HashMap<String,Integer>( 3 );
    static
    {
        valueMap.put("normal", Typeface.NORMAL); // 0
        valueMap.put("bold",   Typeface.BOLD);   // 1
        valueMap.put("italic", Typeface.ITALIC); // 2
    }

    public AttrDescTextStyle(ClassDescViewBased parent,String name)
    {
        super(parent,name);
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int style = parseMultipleName(value, valueMap);
        setTextStyle(view,style);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view, "normal", null,null);
    }

    protected abstract void setTextStyle(View view,int style);
}
