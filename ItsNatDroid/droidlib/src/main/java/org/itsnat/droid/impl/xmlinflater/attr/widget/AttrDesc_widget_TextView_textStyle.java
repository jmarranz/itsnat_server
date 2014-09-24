package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_TextView_textStyle extends AttrDesc
{
    static Map<String,Integer> valueMap = new HashMap<String,Integer>( 3 );
    static
    {
        valueMap.put("normal", Typeface.NORMAL); // 0
        valueMap.put("bold",   Typeface.BOLD);   // 1
        valueMap.put("italic", Typeface.ITALIC); // 2
    }

    public AttrDesc_widget_TextView_textStyle(ClassDescViewBased parent)
    {
        super(parent,"textStyle");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        TextView textView = (TextView)view;

        int style = parseMultipleName(value, valueMap);
        Typeface tf = textView.getTypeface();

        textView.setTypeface(tf,style);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view, "normal", null,null);
    }

}
