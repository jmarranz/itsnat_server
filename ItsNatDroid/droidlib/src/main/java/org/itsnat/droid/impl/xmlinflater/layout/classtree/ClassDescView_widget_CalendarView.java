package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecFieldSetColor;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecFieldSetDrawable;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecFieldSetInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_CalendarView_dateTextAppearance;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_CalendarView_maxDate_minDate;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_CalendarView_weekDayTextAppearance;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_CalendarView extends ClassDescViewBased
{
    public ClassDescView_widget_CalendarView(ClassDescViewMgr classMgr,ClassDescView_widget_FrameLayout parentClass)
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

        addAttrDesc(new AttrDescView_widget_CalendarView_dateTextAppearance(this));
        addAttrDesc(new AttrDescViewReflecMethodInt(this,"firstDayOfWeek",Calendar.getInstance(Locale.getDefault()).getFirstDayOfWeek())); // El valor por defecto no es exactamente así pero es razonable
        addAttrDesc(new AttrDescViewReflecFieldSetColor(this,"focusedMonthDateColor","mFocusedMonthDateColor","#000000"));
        addAttrDesc(new AttrDescView_widget_CalendarView_maxDate_minDate(this,"maxDate"));
        addAttrDesc(new AttrDescView_widget_CalendarView_maxDate_minDate(this,"minDate"));
        addAttrDesc(new AttrDescViewReflecFieldSetDrawable(this,"selectedDateVerticalBar","mSelectedDateVerticalBar",null)); // Hay un Drawable por defecto
        addAttrDesc(new AttrDescViewReflecFieldSetColor(this,"selectedWeekBackgroundColor","mSelectedWeekBackgroundColor","#000000"));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"showWeekNumber",true));
        addAttrDesc(new AttrDescViewReflecFieldSetInt(this,"shownWeekCount","mShownWeekCount",6));
        addAttrDesc(new AttrDescViewReflecFieldSetColor(this,"unfocusedMonthDateColor","mUnfocusedMonthDateColor","#000000"));
        addAttrDesc(new AttrDescView_widget_CalendarView_weekDayTextAppearance(this));
        addAttrDesc(new AttrDescViewReflecFieldSetColor(this,"weekNumberColor","mWeekNumberColor","#000000"));
        addAttrDesc(new AttrDescViewReflecFieldSetColor(this,"weekSeparatorLineColor","mWeekSeparatorLineColor","#000000"));

    }
}

