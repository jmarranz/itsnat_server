package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecFieldSetColor;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecFieldSetDrawable;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecFieldSetInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodInt;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_CalendarView_dateTextAppearance;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_CalendarView_maxDate_minDate;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_CalendarView_weekDayTextAppearance;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_CalendarView extends ClassDescViewBased
{
    public ClassDesc_widget_CalendarView(ClassDescViewMgr classMgr,ClassDesc_widget_FrameLayout parentClass)
    {
        super(classMgr,"android.widget.CalendarView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDesc_widget_CalendarView_dateTextAppearance(this));
        addAttrDesc(new AttrDescReflecMethodInt(this,"firstDayOfWeek",Calendar.getInstance(Locale.getDefault()).getFirstDayOfWeek())); // El valor por defecto no es exactamente así pero es razonable
        addAttrDesc(new AttrDescReflecFieldSetColor(this,"focusedMonthDateColor","mFocusedMonthDateColor","#000000"));
        addAttrDesc(new AttrDesc_widget_CalendarView_maxDate_minDate(this,"maxDate"));
        addAttrDesc(new AttrDesc_widget_CalendarView_maxDate_minDate(this,"minDate"));
        addAttrDesc(new AttrDescReflecFieldSetDrawable(this,"selectedDateVerticalBar","mSelectedDateVerticalBar",null)); // Hay un Drawable por defecto
        addAttrDesc(new AttrDescReflecFieldSetColor(this,"selectedWeekBackgroundColor","mSelectedWeekBackgroundColor","#000000"));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"showWeekNumber",true));
        addAttrDesc(new AttrDescReflecFieldSetInt(this,"shownWeekCount","mShownWeekCount",6));
        addAttrDesc(new AttrDescReflecFieldSetColor(this,"unfocusedMonthDateColor","mUnfocusedMonthDateColor","#000000"));
        addAttrDesc(new AttrDesc_widget_CalendarView_weekDayTextAppearance(this));
        addAttrDesc(new AttrDescReflecFieldSetColor(this,"weekNumberColor","mWeekNumberColor","#000000"));
        addAttrDesc(new AttrDescReflecFieldSetColor(this,"weekSeparatorLineColor","mWeekSeparatorLineColor","#000000"));

    }
}

