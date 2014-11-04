package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecFieldSetColor;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecFieldSetDrawable;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecFieldSetInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDesc_widget_CalendarView_dateTextAppearance;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDesc_widget_CalendarView_maxDate_minDate;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDesc_widget_CalendarView_weekDayTextAppearance;

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

    @Override
    public OneTimeAttrProcess createOneTimeAttrProcess(final View view, ViewGroup viewParent)
    {
        OneTimeAttrProcess oneTimeAttrProcess = super.createOneTimeAttrProcess(view, viewParent);

        oneTimeAttrProcess.addLastTask(new Runnable(){
            @Override
            public void run()
            {
                // Esto es raro de narices pero es un workaround de un buggy de CalendarView al crearse programáticamente
                // o bien es por nuestra "culpa" al aplicar los atributos en un orden incorrecto, el caso es que la semana
                // actual no se selecciona, llamando a calendarView.setDate(System.currentTimeMillis()) no reacciona
                // pero sí lo hace si cambiamos mucho la fecha
                CalendarView calView = (CalendarView)view;
                calView.setDate(calView.getDate() + 7*24*60*60*1000);
                calView.setDate(System.currentTimeMillis());
            }
        });

        return oneTimeAttrProcess;
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

