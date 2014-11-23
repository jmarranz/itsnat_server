package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class AttrDescViewTypeface extends AttrDescView
{
    static Map<String,Integer> valueMap = new HashMap<String,Integer>( 4 );
    static
    {
        valueMap.put("normal",    0 );
        valueMap.put("sans",      1 );
        valueMap.put("serif",     2 );
        valueMap.put("monospace", 3 );
    }

    public AttrDescViewTypeface(ClassDescViewBased parent, String name)
    {
        super(parent,name);
    }

    public void setAttribute(View view, DOMAttr attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        Typeface tf = null; // El caso null
        int convValue = AttrDescView.<Integer>parseSingleName(attr.getValue(), valueMap);
        switch(convValue)
        {
            case 0: tf = null;
                    break; // Es el caso "normal"
            case 1: tf = Typeface.SANS_SERIF;
                    break;
            case 2: tf = Typeface.SERIF;
                    break;
            case 3: tf = Typeface.MONOSPACE;
                    break;
        }

        TextView textView = (TextView)view;
        Typeface currTf = textView.getTypeface();
        int style = currTf != null? currTf.getStyle() : 0;

        textView.setTypeface(tf,style);
    }

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        setAttribute(view, "normal",xmlInflaterLayout,ctx, null,null);
    }


    protected abstract Typeface getCurrentTypeface(View view);
    protected abstract void setCurrentTypeface(View view,Typeface tf,int style);
}
