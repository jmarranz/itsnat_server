package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.attr.FieldContainer;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_CalendarView_dateTextAppearance extends AttrDesc
{
    protected FieldContainer<int[]> fieldTextAppearance;
    protected FieldContainer<Integer> fieldTextAppearance_textSize;
    protected FieldContainer<Integer> fieldTextAppearance_Small;
    protected FieldContainer<Integer> fieldDateTextSize;

    public AttrDesc_widget_CalendarView_dateTextAppearance(ClassDescViewBased parent)
    {
        super(parent,"dateTextAppearance");

        Class class_R_styleable = getClass_R_styleable(); // com.android.internal.R$styleable
        this.fieldTextAppearance = new FieldContainer<int[]>(class_R_styleable,"TextAppearance");
        this.fieldTextAppearance_textSize = new FieldContainer<Integer>(class_R_styleable,"TextAppearance_textSize");
        this.fieldTextAppearance_Small = new FieldContainer<Integer>(class_R_styleable,"TextAppearance_Small");
        this.fieldDateTextSize = new FieldContainer<Integer>(parent,"mDateTextSize");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int dateTextAppearanceResId = getIdentifier(value,view.getContext());

        if (dateTextAppearanceResId <= 0) dateTextAppearanceResId = fieldTextAppearance_Small.get(null); // Valor por defecto

        Context context = view.getContext();

        int[] textAppearanceStyleArr = fieldTextAppearance.get(null);

        int textSizeStyle = fieldTextAppearance_textSize.get(null);

        int dateTextSize;
        TypedArray dateTextAppearance = context.obtainStyledAttributes(dateTextAppearanceResId,textAppearanceStyleArr);
        try
        {
            dateTextSize = dateTextAppearance.getDimensionPixelSize(textSizeStyle, 14); // DEFAULT_DATE_TEXT_SIZE = 14
        }
        finally
        {
            dateTextAppearance.recycle();
        }

        fieldDateTextSize.set(view,dateTextSize);
    }

    public void removeAttribute(View view)
    {
        // Se usa el valor por defecto de Android
        setAttribute(view,"0",null,null);

    }
}
