package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.text.TextUtils;
import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
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
public class AttrDesc_widget_DatePicker_maxDate_minDate extends AttrDesc
{
    private static final int DEFAULT_START_YEAR = 1900;
    private static final int DEFAULT_END_YEAR = 2100;

    protected MethodContainer<Boolean> methodParseDate;
    protected FieldContainer<Locale> fieldCurrentLocale;
    protected MethodContainer<Calendar> methodMaxMinDate;

    public AttrDesc_widget_DatePicker_maxDate_minDate(ClassDescViewBased parent, String name)
    {
        super(parent,name);
        this.methodParseDate = new MethodContainer<Boolean>(parent,"parseDate",new Class[]{String.class,Calendar.class});
        this.fieldCurrentLocale = new FieldContainer<Locale>(parent,"mCurrentLocale");

        String methodName = null;
        if ("maxDate".equals(name))
            methodName = "setMaxDate";
        else if ("minDate".equals(name))
            methodName = "setMinDate";

        this.methodMaxMinDate = new MethodContainer<Calendar>(parent,methodName,new Class[]{long.class});
    }

    public void setAttribute(final View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        final String date = AttrDesc.getString(value,view.getContext());

        Runnable task = new Runnable()
        {
            @Override
            public void run()
            {
                // Delegamos al final porque los atributos maxDate y minDate tienen prioridad (ganan si están definidos)
                // sobre startYear y endYear
                Locale currentLocale = fieldCurrentLocale.get(view);
                Calendar tempDate = Calendar.getInstance(currentLocale);
                tempDate.clear();

                if (!TextUtils.isEmpty(date))
                {
                    if (!parseDate(view,date, tempDate)) // El código fuente de Android tolera un mal formato, nosotros no pues no hace más que complicarlo t_odo
                        throw new ItsNatDroidException("Date: " + date + " not in format: " + "MM/dd/yyyy");
                }
                else
                {
                    // Caso de eliminación de atributo, intrepretamos como el deseo de poner los valores por defecto (más o menos es así en el código fuente)
                    // hay que tener en cuenta que es un valor explícito por lo que ignoramos/reemplazamos los posibles valores puestos
                    // por los atributos endYear/startYear explícitamente

                    if ("maxDate".equals(name))
                        tempDate.set(DEFAULT_END_YEAR, Calendar.DECEMBER /*11*/, 31);
                    else if ("minDate".equals(name))
                        tempDate.set(DEFAULT_START_YEAR,Calendar.JANUARY /*0*/, 1);
                }

                methodMaxMinDate.invoke(view,tempDate.getTimeInMillis());
            }
        };

        if (oneTimeAttrProcess != null)
            oneTimeAttrProcess.addLayoutParamsTask(task);
        else
            task.run();
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
