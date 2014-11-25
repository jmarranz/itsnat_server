package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.view.View;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.xmlinflater.FieldContainer;
import org.itsnat.droid.impl.xmlinflater.MethodContainer;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_CalendarView_dateTextAppearance extends AttrDescView
{
    protected FieldContainer<int[]> fieldTextAppearance;
    protected FieldContainer<Integer> fieldTextAppearance_textSize;
    protected FieldContainer<Integer> fieldTextAppearance_Small;
    protected FieldContainer<Integer> fieldDateTextSize;
    protected MethodContainer<Void> methodDateTextAppearance;

    public AttrDescView_widget_CalendarView_dateTextAppearance(ClassDescViewBased parent)
    {
        super(parent,"dateTextAppearance");

        Class class_R_styleable = getClass_R_styleable(); // com.android.internal.R$styleable
        this.fieldTextAppearance_Small = new FieldContainer<Integer>(class_R_styleable, "TextAppearance_Small");

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
        {
            this.fieldTextAppearance = new FieldContainer<int[]>(class_R_styleable, "TextAppearance");
            this.fieldTextAppearance_textSize = new FieldContainer<Integer>(class_R_styleable, "TextAppearance_textSize");
            this.fieldDateTextSize = new FieldContainer<Integer>(parent.getDeclaredClass(), "mDateTextSize");
        }
        else // A partir de level 16 hay un método setDateTextAppearance (int resourceId). Podríamos usar lo de ICS pero en Lollipop cambia CalendarView a fondo usando un "delegate"
        {
            this.methodDateTextAppearance = new MethodContainer<Void>(parent.getDeclaredClass(),"setDateTextAppearance",new Class[]{int.class});
        }
    }

    public void setAttribute(View view, DOMAttr attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int dateTextAppearanceResId = getIdentifier(attr.getValue(),ctx);

        if (dateTextAppearanceResId <= 0) dateTextAppearanceResId = fieldTextAppearance_Small.get(null); // Valor por defecto

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
        {
            int[] textAppearanceStyleArr = fieldTextAppearance.get(null);

            int textSizeStyle = fieldTextAppearance_textSize.get(null);

            int dateTextSize;
            TypedArray dateTextAppearance = ctx.obtainStyledAttributes(dateTextAppearanceResId, textAppearanceStyleArr);
            try
            {
                dateTextSize = dateTextAppearance.getDimensionPixelSize(textSizeStyle, 14); // DEFAULT_DATE_TEXT_SIZE = 14
            }
            finally
            {
                dateTextAppearance.recycle();
            }

            fieldDateTextSize.set(view, dateTextSize);
        }
        else
        {
            methodDateTextAppearance.invoke(view,dateTextAppearanceResId);
        }
    }

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        // Se usa el valor por defecto de Android
        setAttribute(view,"0",xmlInflaterLayout,ctx,null,null);

    }
}
