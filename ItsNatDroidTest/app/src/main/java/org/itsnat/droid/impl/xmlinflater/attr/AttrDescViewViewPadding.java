package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBase;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescViewViewPadding extends AttrDesc
{
    public AttrDescViewViewPadding(ClassDescViewBase parent,String name)
    {
        super(parent,name);
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess)
    {
        int convValue = (int)getDimPixel(value,view.getContext());

        String name = getName();
        if ("paddingLeft".equals(name))
            view.setPadding(convValue,view.getPaddingTop(),view.getPaddingRight(),view.getPaddingBottom());
        else if ("paddingTop".equals(name))
            view.setPadding(view.getPaddingLeft(),convValue,view.getPaddingRight(),view.getPaddingBottom());
        else if ("paddingRight".equals(name))
            view.setPadding(view.getPaddingLeft(),view.getPaddingTop(),convValue,view.getPaddingBottom());
        else if ("paddingBottom".equals(name))
            view.setPadding(view.getPaddingLeft(),view.getPaddingTop(),view.getPaddingRight(),convValue);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"0",null);
    }
}
