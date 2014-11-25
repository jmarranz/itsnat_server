package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;

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
public class AttrDescView_widget_DatePicker_endYear_startYear extends AttrDescView
{
    private static final int DEFAULT_START_YEAR = 1900;
    private static final int DEFAULT_END_YEAR = 2100;

    protected FieldContainer<Object> fieldDelegate;
    protected FieldContainer<Locale> fieldCurrentLocale;
    protected MethodContainer<Void> methodMaxMinDate;

    public AttrDescView_widget_DatePicker_endYear_startYear(ClassDescViewBased parent, String name)
    {
        super(parent,name);

        Class datePickerClass1,datePickerClass2;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            datePickerClass1 = parent.getDeclaredClass();
            datePickerClass2 = datePickerClass1;
        }
        else // Lollipop
        {
            this.fieldDelegate = new FieldContainer<Object>(parent.getDeclaredClass(), "mDelegate");
            datePickerClass1 = MiscUtil.resolveClass(DatePicker.class.getName() + "$AbstractDatePickerDelegate");
            datePickerClass2 = MiscUtil.resolveClass(DatePicker.class.getName() + "$DatePickerSpinnerDelegate");
        }

        this.fieldCurrentLocale = new FieldContainer<Locale>(datePickerClass1, "mCurrentLocale");

        String methodName = null;
        if ("endYear".equals(name))
            methodName = "setMaxDate";
        else if ("startYear".equals(name))
            methodName = "setMinDate";

        this.methodMaxMinDate = new MethodContainer<Void>(datePickerClass2,methodName,new Class[]{long.class});
    }

    public void setAttribute(final View view, DOMAttr attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        final String year = getString(attr.getValue(),ctx);

        Object datePickerObject = getDatePickerObject(view);

        // Delegamos al final porque los atributos maxDate y minDate tienen prioridad (ganan si están definidos)
        // sobre startYear y endYear
        Locale currentLocale = fieldCurrentLocale.get(datePickerObject);
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

        methodMaxMinDate.invoke(datePickerObject,tempDate.getTimeInMillis());
    }

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        setAttribute(view, "", xmlInflaterLayout, ctx, null, null);
    }

    private Object getDatePickerObject(View view)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return (DatePicker)view;
        else
            return fieldDelegate.get(view);
    }

}
