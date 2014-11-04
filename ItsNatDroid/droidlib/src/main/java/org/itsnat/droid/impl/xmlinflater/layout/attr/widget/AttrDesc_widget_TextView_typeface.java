package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescTypeface;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_TextView_typeface extends AttrDescTypeface
{
    public AttrDesc_widget_TextView_typeface(ClassDescViewBased parent)
    {
        super(parent,"typeface");
    }

    protected Typeface getCurrentTypeface(View view)
    {
        TextView textView = (TextView)view;
        return textView.getTypeface();
    }

    protected void setCurrentTypeface(View view,Typeface tf,int style)
    {
        TextView textView = (TextView)view;
        textView.setTypeface(tf,style);
    }

}
