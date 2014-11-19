package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.os.Build;
import android.view.View;

import org.itsnat.droid.impl.dom.AttrParsed;
import org.itsnat.droid.impl.xmlinflater.MethodContainer;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_CalendarView_weekDayTextAppearance extends AttrDescView
{
    protected MethodContainer<Boolean> method;

    public AttrDescView_widget_CalendarView_weekDayTextAppearance(ClassDescViewBased parent)
    {
        super(parent,"weekDayTextAppearance");

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) // 4.0.3 Level 15
            this.method = new MethodContainer<Boolean>(parent.getDeclaredClass(),"setUpHeader",new Class[]{int.class});
        else
            this.method = new MethodContainer<Boolean>(parent.getDeclaredClass(),"setWeekDayTextAppearance",new Class[]{int.class});
    }

    public void setAttribute(View view, AttrParsed attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int id = getIdentifier(attr.getValue(),ctx);

        method.invoke(view, id);
    }

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        // Android tiene un estilo por defecto
    }

}
