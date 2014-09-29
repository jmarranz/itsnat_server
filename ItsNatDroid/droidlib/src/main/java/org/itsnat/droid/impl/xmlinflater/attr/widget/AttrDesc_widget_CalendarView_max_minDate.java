package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.text.TextUtils;
import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.attr.FieldContainer;
import org.itsnat.droid.impl.xmlinflater.attr.MethodContainer;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_CalendarView_max_minDate extends AttrDesc
{
    private static final String DEFAULT_MIN_DATE = "01/01/1900";
    private static final String DEFAULT_MAX_DATE = "01/01/2100";

    protected MethodContainer<Boolean> methodParseDate;
    protected FieldContainer<Locale> fieldCurrentLocale;
    protected FieldContainer<Calendar> fieldMaxMinDate;

    public AttrDesc_widget_CalendarView_max_minDate(ClassDescViewBased parent, String name)
    {
        super(parent,name);
        this.methodParseDate = new MethodContainer<Boolean>(parent,"parseDate",new Class[]{String.class,Calendar.class});
        this.fieldCurrentLocale = new FieldContainer<Locale>(parent,"mCurrentLocale");

        String fieldName = null;
        if ("maxDate".equals(name))
            fieldName = "mMaxDate";
        else if ("minDate".equals(name))
            fieldName = "mMinDate";
        this.fieldMaxMinDate = new FieldContainer<Calendar>(parent,fieldName);
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        String date = AttrDesc.getString(value,view.getContext());

        Locale currentLocale = fieldCurrentLocale.get(view);
        Calendar outDate = Calendar.getInstance(currentLocale);
        if (TextUtils.isEmpty(date) || !parseDate(view,date, outDate))
        {
            String defaultMaxMin = null;
            if ("maxDate".equals(name))
                defaultMaxMin = DEFAULT_MAX_DATE;
            else if ("minDate".equals(name))
                defaultMaxMin = DEFAULT_MIN_DATE;

            parseDate(view,defaultMaxMin, outDate);
        }

        fieldMaxMinDate.set(view,outDate);
    }

    public void removeAttribute(View view)
    {
        String value = null;
        if ("maxDate".equals(name))
            value = DEFAULT_MAX_DATE;
        else if ("minDate".equals(name))
            value = DEFAULT_MIN_DATE;
        setAttribute(view,value,null,null);
    }

    private boolean parseDate(View view,String date, Calendar outDate)
    {
        return methodParseDate.invoke(view,date,outDate);
    }
}
