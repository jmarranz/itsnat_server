package org.itsnat.droid.impl.xmlinflater.attr;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_TextView_textAppearance extends AttrDesc
{
    public AttrDesc_widget_TextView_textAppearance(ClassDescViewBased parent)
    {
        super(parent,"textAppearance");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess)
    {
        Context ctx = view.getContext();

        ((TextView)view).setTextAppearance(ctx,getIdentifier(value,ctx));
    }

    public void removeAttribute(View view)
    {
        // No se que hacer
    }
}
