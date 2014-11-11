package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.view.View;

import org.itsnat.droid.impl.model.AttrParsed;
import org.itsnat.droid.impl.xmlinflater.MethodContainer;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodString;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_Chronometer_format extends AttrDescViewReflecMethodString
{
    protected MethodContainer methodContainer;

    public AttrDescView_widget_Chronometer_format(ClassDescViewBased parent)
    {
        super(parent,"format","%s"); // Android tiene un texto por defecto

        this.methodContainer = new MethodContainer(parent,"init",null);
    }

    public void setAttribute(View view, AttrParsed attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        super.setAttribute(view,attr, xmlInflaterLayout, ctx, oneTimeAttrProcess,pending);

        methodContainer.invoke(view); // Hay que llamar a este método init() sino no se entera del cambio, ni siquiera en creación via parse dinámico
    }
}
