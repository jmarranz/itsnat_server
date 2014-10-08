package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.graphics.Typeface;
import android.view.View;
import android.widget.Switch;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescTypeface;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_Switch_typeface extends AttrDescTypeface
{
    public AttrDesc_widget_Switch_typeface(ClassDescViewBased parent)
    {
        super(parent,"typeface");
    }

    protected Typeface getCurrentTypeface(View view)
    {
        Switch swView = (Switch)view;
        return swView.getTypeface();
    }

    protected void setCurrentTypeface(View view,Typeface tf,int style)
    {
        Switch swView = (Switch)view;
        swView.setTypeface(tf,style);
    }

}
