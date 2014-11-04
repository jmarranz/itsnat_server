package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_TextView_bufferType extends AttrDesc
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
        super(parent,"bufferType");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        TextView.BufferType convertedValue = AttrDesc.<TextView.BufferType>parseSingleName(value, valueMap);

        TextView textView = (TextView)view;
        textView.setText(textView.getText(),(TextView.BufferType)convertedValue);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view, "normal", null,null);
    }

}
