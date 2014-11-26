package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.CalendarView;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.util.MiscUtil;
import org.itsnat.droid.impl.xmlinflater.FieldContainer;
import org.itsnat.droid.impl.xmlinflater.MethodContainer;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_CalendarView_maxDate_minDate extends AttrDescView
{
    private static final String DEFAULT_MIN_DATE = "01/01/1900";
    private static final String DEFAULT_MAX_DATE = "01/01/2100";

    protected FieldContainer<Object> fieldDelegate;
    protected FieldContainer<Locale> fieldCurrentLocale;
    protected MethodContainer<Boolean> methodParseDate;
    protected FieldContainer<Calendar> fieldMaxMinDate;

    public AttrDescView_widget_CalendarView_maxDate_minDate(ClassDescViewBased parent, String name)
    {
        super(parent,name);

        Class calendarClass1,calendarClass2;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            calendarClass1 = parent.getDeclaredClass();
            calendarClass2 = calendarClass1;
        }
        else // Lollipop
        {
            this.fieldDelegate = new FieldContainer<Object>(parent.getDeclaredClass(), "mDelegate");
            calendarClass1 = MiscUtil.resolveClass(CalendarView.class.getName() + "$AbstractCalendarViewDelegate");
            calendarClass2 = MiscUtil.resolveClass(CalendarView.class.getName() + "$LegacyCalendarViewDelegate");
        }

        this.fieldCurrentLocale = new FieldContainer<Locale>(calendarClass1, "mCurrentLocale");
        this.methodParseDate = new MethodContainer<Boolean>(calendarClass2,"parseDate",new Class[]{String.class,Calendar.class});

        String fieldName = null;
        if ("maxDate".equals(name))
            fieldName = "mMaxDate";
        else if ("minDate".equals(name))
            fieldName = "mMinDate";
        this.fieldMaxMinDate = new FieldContainer<Calendar>(calendarClass2,fieldName);
    }

    public void setAttribute(View view, DOMAttr attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        String date = getString(attr.getValue(),ctx);

        Object calendarObject = getCalendarObject((CalendarView)view);

        Locale currentLocale = fieldCurrentLocale.get(calendarObject);
        Calendar outDate = Calendar.getInstance(currentLocale);
        // No es necesario: outDate.clear();

        if (!TextUtils.isEmpty(date))
        {
            if (!parseDate(calendarObject,date, outDate)) // El código fuente de Android tolera un mal formato, nosotros no pues no hace más que complicarlo todo
                throw new ItsNatDroidException("Date: " + date + " not in format: " + "MM/dd/yyyy");
        }
        else // Caso de eliminación de atributo, interpretamos el "" como el deseo de poner los valores por defecto (más o menos es así en el código fuente)
        {
            String defaultMaxMin = null;
            if ("maxDate".equals(name))
                defaultMaxMin = DEFAULT_MAX_DATE;
            else if ("minDate".equals(name))
                defaultMaxMin = DEFAULT_MIN_DATE;

            parseDate(calendarObject,defaultMaxMin, outDate);
        }

        fieldMaxMinDate.set(calendarObject,outDate);
    }

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        String value = null;
        if ("maxDate".equals(name))
            value = DEFAULT_MAX_DATE;
        else if ("minDate".equals(name))
            value = DEFAULT_MIN_DATE;
        setAttribute(view,value,xmlInflaterLayout,ctx,null,null);
    }

    private Object getCalendarObject(CalendarView view)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return view;
        else
            return fieldDelegate.get(view);
    }

    private boolean parseDate(Object calendarObject,String date, Calendar outDate)
    {
        return methodParseDate.invoke(calendarObject,date,outDate);
    }
}
