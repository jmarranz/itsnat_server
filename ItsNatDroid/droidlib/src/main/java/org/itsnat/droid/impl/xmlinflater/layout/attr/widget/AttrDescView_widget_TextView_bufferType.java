package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.dom.AttrParsed;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_TextView_bufferType extends AttrDescView
{
    static Map<String,TextView.BufferType> valueMap = new HashMap<String,TextView.BufferType>( 3 );
    static
    {
        valueMap.put("normal", TextView.BufferType.NORMAL);
        valueMap.put("spannable", TextView.BufferType.SPANNABLE);
        valueMap.put("editable", TextView.BufferType.EDITABLE);
    }

    public AttrDescView_widget_TextView_bufferType(ClassDescViewBased parent)
    {
        super(parent,"bufferType");
    }

    public void setAttribute(View view, AttrParsed attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        TextView.BufferType convertedValue = AttrDescView.<TextView.BufferType>parseSingleName(attr.getValue(), valueMap);

        TextView textView = (TextView)view;
        textView.setText(textView.getText(),(TextView.BufferType)convertedValue);
    }

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        setAttribute(view, "normal",xmlInflaterLayout,ctx, null,null);
    }

}
