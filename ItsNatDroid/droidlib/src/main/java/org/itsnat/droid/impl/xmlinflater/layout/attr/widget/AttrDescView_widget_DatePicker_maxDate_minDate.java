package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.model.AttrParsed;
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
public class AttrDescView_widget_DatePicker_maxDate_minDate extends AttrDescView
{
    private static final int DEFAULT_START_YEAR = 1900;
    private static final int DEFAULT_END_YEAR = 2100;

    protected MethodContainer<Boolean> methodParseDate;
    protected FieldContainer<Locale> fieldCurrentLocale;
    protected MethodContainer<Calendar> methodMaxMinDate;

    public AttrDescView_widget_DatePicker_maxDate_minDate(ClassDescViewBased parent, String name)
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

    public void setAttribute(final View view, AttrParsed attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        final String date = getString(attr.getValue(),ctx);

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

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        setAttribute(view,"",xmlInflaterLayout,ctx,null,null);
    }

    private boolean parseDate(View view,String date, Calendar outDate)
    {
        return methodParseDate.invoke(view,date,outDate);
    }
}
