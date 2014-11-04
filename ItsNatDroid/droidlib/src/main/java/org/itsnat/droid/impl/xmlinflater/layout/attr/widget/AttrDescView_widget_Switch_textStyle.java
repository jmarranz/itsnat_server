package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.graphics.Typeface;
import android.view.View;
import android.widget.Switch;

import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewTextStyle;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_Switch_textStyle extends AttrDescViewTextStyle
{
    public AttrDescView_widget_Switch_textStyle(ClassDescViewBased parent)
    {
        super(parent, "textStyle");
    }

    @Override
    protected void setTextStyle(View view, int style)
    {
        Switch swView = (Switch)view;
        Typeface tf = swView.getTypeface();
        swView.setTypeface(tf, style);
    }

}
