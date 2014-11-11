package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.model.AttrParsed;
import org.itsnat.droid.impl.xmlinflater.FieldContainer;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_TextView_shadowLayer_base extends AttrDescView
{
    protected FieldContainer<Integer> fieldShadowColor;
    protected FieldContainer<Float> fieldShadowRadius;
    protected FieldContainer<Float> fieldShadowDx;
    protected FieldContainer<Float> fieldShadowDy;


    public AttrDescView_widget_TextView_shadowLayer_base(ClassDescViewBased parent, String name)
    {
        super(parent,name);
        this.fieldShadowColor = new FieldContainer<Integer>(Paint.class,"shadowColor");
        this.fieldShadowRadius = new FieldContainer<Float>(parent,"mShadowRadius");
        this.fieldShadowDx = new FieldContainer<Float>(parent,"mShadowDx");
        this.fieldShadowDy = new FieldContainer<Float>(parent,"mShadowDy");
    }

    public void setAttribute(View view, AttrParsed attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        TextView textView = (TextView)view;

        float radius = -1;
        float dx = -1;
        float dy = -1;
        int color = -1;

        String value = attr.getValue();
        if (name.equals("shadowColor"))
        {
            int convValue = getColor(value, ctx);

            radius = fieldShadowRadius.get(textView);
            dx = fieldShadowDx.get(textView);
            dy = fieldShadowDy.get(textView);
            color = convValue;
        }
        else if (name.equals("shadowDx"))
        {
            float convValue = getFloat(value,ctx);

            radius = fieldShadowRadius.get(textView);
            dx = convValue;
            dy = fieldShadowDy.get(textView);
            color = fieldShadowColor.get(textView.getPaint());
        }
        else if (name.equals("shadowDy"))
        {
            float convValue = getFloat(value, ctx);

            radius = fieldShadowRadius.get(textView);
            dx = fieldShadowDx.get(textView);
            dy = convValue;
            color = fieldShadowColor.get(textView.getPaint());
        }
        else if (name.equals("shadowRadius"))
        {
            float convValue = getFloat(value, ctx);

            radius = convValue;
            dx = fieldShadowDx.get(textView);
            dy = fieldShadowDy.get(textView);
            color = fieldShadowColor.get(textView.getPaint());
        }

        textView.setShadowLayer(radius,dx,dy,color);
    }

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        String defaultValue = null;
        if (name.equals("shadowColor"))   defaultValue = "0";
        else if (name.equals("shadowDx")) defaultValue = "0";
        else if (name.equals("shadowDy")) defaultValue = "0";
        else if (name.equals("shadowRadius")) defaultValue = "0";

        setAttribute(view,defaultValue,xmlInflaterLayout,ctx,null,null);
    }


}
