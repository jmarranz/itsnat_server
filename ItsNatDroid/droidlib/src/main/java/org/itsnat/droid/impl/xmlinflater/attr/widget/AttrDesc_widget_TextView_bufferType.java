package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodSingleName;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_TextView_bufferType extends AttrDescReflecMethodSingleName<TextView.BufferType>
{
    static Map<String,TextView.BufferType> valueMap = new HashMap<String,TextView.BufferType>( 3 );
    static
    {
        valueMap.put("normal", TextView.BufferType.NORMAL);
        valueMap.put("spannable", TextView.BufferType.SPANNABLE);
        valueMap.put("editable", TextView.BufferType.EDITABLE);
    }

    public AttrDesc_widget_TextView_bufferType(ClassDescViewBased parent)
    {
        super(parent,"bufferType",TextView.BufferType.class,valueMap,"normal");
    }

    protected void callMethod(View view, Object convertedValue)
    {
        // Redefinimos porque el método tiene dos parámetros y no vale el reflection por defecto
        TextView textView = (TextView)view;
        textView.setText(textView.getText(),(TextView.BufferType)convertedValue);
    }
}
