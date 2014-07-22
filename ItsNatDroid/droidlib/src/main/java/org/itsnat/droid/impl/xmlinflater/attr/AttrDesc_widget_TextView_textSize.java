package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_TextView_textSize extends AttrDesc
{
    public AttrDesc_widget_TextView_textSize(ClassDescViewBased parent)
    {
        super(parent,"textSize");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess)
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
