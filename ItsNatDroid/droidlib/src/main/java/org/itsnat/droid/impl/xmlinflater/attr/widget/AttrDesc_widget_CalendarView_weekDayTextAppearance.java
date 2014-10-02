package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.os.Build;
import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.attr.MethodContainer;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_CalendarView_weekDayTextAppearance extends AttrDesc
{
    protected MethodContainer<Boolean> method;

    public AttrDesc_widget_CalendarView_weekDayTextAppearance(ClassDescViewBased parent)
    {
        super(parent,"weekDayTextAppearance");

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) // 4.0.3 Level 15
            this.method = new MethodContainer<Boolean>(parent,"setUpHeader",new Class[]{int.class});
        else
            this.method = new MethodContainer<Boolean>(parent,"setWeekDayTextAppearance",new Class[]{int.class});
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int id = getIdentifier(value,view.getContext());

        method.invoke(view, id);
    }

    public void removeAttribute(View view)
    {
        // Android tiene un estilo por defecto
    }

}
