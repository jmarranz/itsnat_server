package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodColor;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodInt;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_CalendarView_dateTextAppearance;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_CalendarView_max_minDate;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_CalendarView extends ClassDescViewBased
{
    public ClassDesc_widget_CalendarView(ClassDesc_widget_FrameLayout parentClass)
    {
        super("android.widget.CalendarView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDesc_widget_CalendarView_dateTextAppearance(this));
        addAttrDesc(new AttrDescReflecMethodInt(this,"firstDayOfWeek",Calendar.getInstance(Locale.getDefault()).getFirstDayOfWeek())); // El valor por defecto no es exactamente así pero es razonable
        addAttrDesc(new AttrDescReflecMethodColor(this,"focusedMonthDateColor","#000000"));
        addAttrDesc(new AttrDesc_widget_CalendarView_max_minDate(this,"maxDate"));
        addAttrDesc(new AttrDesc_widget_CalendarView_max_minDate(this,"minDate"));
        addAttrDesc(new AttrDescReflecMethodDrawable(this,"selectedDateVerticalBar",null)); // Hay un Drawable por defecto
        addAttrDesc(new AttrDescReflecMethodColor(this,"selectedWeekBackgroundColor","#000000"));

    }
}

