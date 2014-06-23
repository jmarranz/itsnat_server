package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.ParsePhase;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBase;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescCharSequence extends AttrDescReflection
{
    public AttrDescCharSequence(ClassDescViewBase parent, String name, String methodName)
    {
        super(parent,name,methodName);
    }

    public AttrDescCharSequence(ClassDescViewBase parent, String name)
    {
        super(parent,name);
    }

    protected Class<?> getClassParam()
    {
        return CharSequence.class;
    }

    public void setAttribute(View view, String value, ParsePhase parsePhase)
    {
        String convValue = getString(value, view.getContext());
        setAttribute(view,convValue);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"");
    }

}
