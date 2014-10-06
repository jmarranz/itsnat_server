package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecFieldSetBoolean;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_SlidingDrawer_orientation extends AttrDescReflecFieldSetBoolean
{
    public AttrDesc_widget_SlidingDrawer_orientation(ClassDescViewBased parent)
    {
        super(parent,"orientation","mVertical",true);
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        boolean vertical = true;
        if (value.equals("horizontal"))
            vertical = false;
        else if (value.equals("vertical"))
            vertical = true;

        setField(view,vertical);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view, "vertical", null,null);
    }


}
