package org.itsnat.droid.impl.xmlinflater.attr;

import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.util.ValueUtil;
import org.itsnat.droid.impl.xmlinflater.ParsePhase;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBase;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescWidgetTextViewTextSize extends AttrDesc
{
    public AttrDescWidgetTextViewTextSize(ClassDescViewBase parent)
    {
        super(parent,"textSize");
    }

    public void setAttribute(View view, String value, ParsePhase parsePhase)
    {
        // Existe un setTextSize(int) pero vale la pena, así es perfecto
        TextView textView = (TextView)view;

        String valueTrim = value.trim();
        String suffix = getDimensionSuffix(valueTrim);
        int complexUnit = getDimensionSuffixAsInt(suffix);

        float num = extractFloat(valueTrim, suffix);
        textView.setTextSize(complexUnit,num);
    }

    public void removeAttribute(View view)
    {
        // No se que hacer, poner a cero el texto no tiene sentido
    }
}
