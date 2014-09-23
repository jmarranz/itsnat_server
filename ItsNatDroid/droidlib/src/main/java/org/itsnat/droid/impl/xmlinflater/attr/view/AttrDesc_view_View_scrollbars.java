package org.itsnat.droid.impl.xmlinflater.attr.view;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.attr.MethodContainer;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_scrollbars extends AttrDesc
{
    public static final Map<String,Integer> valueMap = new HashMap<String,Integer>( 14 );

    static
    {
        valueMap.put("none",0); // SCROLLBARS_NONE
        valueMap.put("horizontal", 0x00000100); // SCROLLBARS_HORIZONTAL
        valueMap.put("vertical", 0x00000200);  // SCROLLBARS_VERTICAL
    }

    protected static final int SCROLLBARS_MASK = 0x00000300;

    protected MethodContainer methodSetFlags;

    public AttrDesc_view_View_scrollbars(ClassDescViewBased parent)
    {
        super(parent,"scrollbars");
        this.methodSetFlags = new MethodContainer(parent,"setFlags",new Class[]{int.class, int.class});
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int scrollbars = parseMultipleName(value, valueMap);

        setFlags(view, scrollbars, SCROLLBARS_MASK);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"none",null,null );
    }


    protected void setFlags(View view,int scrollbars,int scrollbarsMask)
    {
        methodSetFlags.call(view,scrollbars, scrollbarsMask);
    }
}
