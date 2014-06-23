package org.itsnat.droid.impl.xmlinflater.attr;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.ParsePhase;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBase;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescWidgetTextViewTextAppearance extends AttrDesc
{
    public AttrDescWidgetTextViewTextAppearance(ClassDescViewBase parent)
    {
        super(parent,"textAppearance");
    }

    public void setAttribute(View view, String value, ParsePhase parsePhase)
    {
        Context ctx = view.getContext();

        ((TextView)view).setTextAppearance(ctx,getIdentifier(value,ctx));
    }

    public void removeAttribute(View view)
    {
        // No se que hacer
    }
}
