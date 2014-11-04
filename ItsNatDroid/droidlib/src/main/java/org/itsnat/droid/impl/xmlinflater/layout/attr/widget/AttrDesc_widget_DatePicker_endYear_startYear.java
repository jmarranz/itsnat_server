package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.text.TextUtils;
import android.view.View;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.layout.attr.FieldContainer;
import org.itsnat.droid.impl.xmlinflater.layout.attr.MethodContainer;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_DatePicker_endYear_startYear extends AttrDesc
{
    private static final int DEFAULT_START_YEAR = 1900;
    private static final int DEFAULT_END_YEAR = 2100;

    protected MethodContainer<Boolean> methodParseDate;
    protected FieldContainer<Locale> fieldCurrentLocale;
    protected MethodContainer<Calendar> methodMaxMinDate;

    public AttrDesc_widget_DatePicker_endYear_startYear(ClassDescViewBased parent, String name)
    {
        super(parent,name);
        this.methodParseDate = new MethodContainer<Boolean>(parent,"parseDate",new Class[]{String.class,Calendar.class});
        this.fieldCurrentLocale = new FieldContainer<Locale>(parent,"mCurrentLocale");

        String methodName = null;
        String fieldName = null;
        if ("endYear".equals(name))
            methodName = "setMaxDate";
        else if ("startYear".equals(name))
            methodName = "setMinDate";

        this.methodMaxMinDate = new MethodContainer<Calendar>(parent,methodName,new Class[]{long.class});
    }

    public void setAttribute(final View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        final String year = getString(value,view.getContext());

        // Delegamos al final porque los atributos maxDate y minDate tienen prioridad (ganan si están definidos)
        // sobre startYear y endYear
        Locale currentLocale = fieldCurrentLocale.get(view);
        Calendar tempDate = Calendar.getInstance(currentLocale);
        tempDate.clear();

        if (!TextUtils.isEmpty(year))
        {
            int yearInt = Integer.parseInt(year);
            if ("endYear".equals(name))
                tempDate.set(yearInt, Calendar.DECEMBER /*11*/, 31);
            else if ("startYear".equals(name))
                tempDate.set(yearInt,Calendar.JANUARY /*0*/, 1);
        }
        else
        {
            // Caso de eliminación de atributo, intrepretamos como el deseo de poner los valores por defecto (más o menos es así en el código fuente)
            // hay que tener en cuenta que es un valor explícito por lo que ignoramos/reemplazamos los posibles valores anteriores
            if ("endYear".equals(name))
                tempDate.set(DEFAULT_END_YEAR, Calendar.DECEMBER /*11*/, 31);
            else if ("startYear".equals(name))
                tempDate.set(DEFAULT_START_YEAR,Calendar.JANUARY /*0*/, 1);
        }

        methodMaxMinDate.invoke(view,tempDate.getTimeInMillis());
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"",null,null);
    }

    private boolean parseDate(View view,String date, Calendar outDate)
    {
        return methodParseDate.invoke(view,date,outDate);
    }
}
