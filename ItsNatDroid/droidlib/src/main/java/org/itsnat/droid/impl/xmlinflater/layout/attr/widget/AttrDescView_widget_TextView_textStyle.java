package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewTextStyle;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_TextView_textStyle extends AttrDescViewTextStyle
{
    public AttrDescView_widget_TextView_textStyle(ClassDescViewBased parent)
    {
        super(parent,"textStyle");
    }

    @Override
    protected void setTextStyle(View view, int style)
    {
        TextView textView = (TextView)view;
        Typeface tf = textView.getTypeface();
        textView.setTypeface(tf,style);
    }
}
