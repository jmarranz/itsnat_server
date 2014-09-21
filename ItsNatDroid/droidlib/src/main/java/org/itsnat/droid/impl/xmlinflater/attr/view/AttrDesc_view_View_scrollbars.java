package org.itsnat.droid.impl.xmlinflater.attr.view;

import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    protected Method methodSetFlags;

    public AttrDesc_view_View_scrollbars(ClassDescViewBased parent)
    {
        super(parent,"scrollbars");
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
        try
        {
            if (methodSetFlags == null)
            {
                this.methodSetFlags = parent.getViewClass().getDeclaredMethod("setFlags", new Class[]{int.class, int.class});
                methodSetFlags.setAccessible(true);
            }

            methodSetFlags.invoke(view, scrollbars, scrollbarsMask);
        }
        catch (NoSuchMethodException ex) { throw new ItsNatDroidException(ex); }
        catch (InvocationTargetException ex) { throw new ItsNatDroidException(ex); }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
    }
}
