package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class AttrDescTypeface extends AttrDesc
{
    static Map<String,Integer> valueMap = new HashMap<String,Integer>( 4 );
    static
    {
        valueMap.put("normal",    0 );
        valueMap.put("sans",      1 );
        valueMap.put("serif",     2 );
        valueMap.put("monospace", 3 );
    }

    public AttrDescTypeface(ClassDescViewBased parent,String name)
    {
        super(parent,name);
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        Typeface tf = null; // El caso null
        int convValue = AttrDesc.<Integer>parseSingleName(value,valueMap);
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

    public void removeAttribute(View view)
    {
        setAttribute(view, "normal", null,null);
    }


    protected abstract Typeface getCurrentTypeface(View view);
    protected abstract void setCurrentTypeface(View view,Typeface tf,int style);
}
