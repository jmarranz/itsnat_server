package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.attr.FieldContainer;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_TextView_shadowLayer_base extends AttrDesc
{
    protected FieldContainer<Integer> fieldShadowColor;
    protected FieldContainer<Float> fieldShadowRadius;
    protected FieldContainer<Float> fieldShadowDx;
    protected FieldContainer<Float> fieldShadowDy;


    public AttrDesc_widget_TextView_shadowLayer_base(ClassDescViewBased parent, String name)
    {
        super(parent,name);
        this.fieldShadowColor = new FieldContainer<Integer>(Paint.class,"shadowColor");
        this.fieldShadowRadius = new FieldContainer<Float>(parent,"mShadowRadius");
        this.fieldShadowDx = new FieldContainer<Float>(parent,"mShadowDx");
        this.fieldShadowDy = new FieldContainer<Float>(parent,"mShadowDy");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        TextView textView = (TextView)view;

        float radius = -1;
        float dx = -1;
        float dy = -1;
        int color = -1;

        if (name.equals("shadowColor"))
        {
            int convValue = getColor(value, textView.getContext());

            radius = fieldShadowRadius.getValue(textView);
            dx = fieldShadowDx.getValue(textView);
            dy = fieldShadowDy.getValue(textView);
            color = convValue;
        }
        else if (name.equals("shadowDx"))
        {
            float convValue = getFloat(value, textView.getContext());

            radius = fieldShadowRadius.getValue(textView);
            dx = convValue;
            dy = fieldShadowDy.getValue(textView);
            color = fieldShadowColor.getValue(textView.getPaint());
        }
        else if (name.equals("shadowDy"))
        {
            float convValue = getFloat(value, textView.getContext());

            radius = fieldShadowRadius.getValue(textView);
            dx = fieldShadowDx.getValue(textView);
            dy = convValue;
            color = fieldShadowColor.getValue(textView.getPaint());
        }
        else if (name.equals("shadowRadius"))
        {
            float convValue = getFloat(value, view.getContext());

            radius = convValue;
            dx = fieldShadowDx.getValue(textView);
            dy = fieldShadowDy.getValue(textView);
            color = fieldShadowColor.getValue(textView.getPaint());
        }

        textView.setShadowLayer(radius,dx,dy,color);
    }

    public void removeAttribute(View view)
    {
        String defaultValue = null;
        if (name.equals("shadowColor"))   defaultValue = "0";
        else if (name.equals("shadowDx")) defaultValue = "0";
        else if (name.equals("shadowDy")) defaultValue = "0";
        else if (name.equals("shadowRadius")) defaultValue = "0";

        setAttribute(view,defaultValue,null,null);
    }


}
