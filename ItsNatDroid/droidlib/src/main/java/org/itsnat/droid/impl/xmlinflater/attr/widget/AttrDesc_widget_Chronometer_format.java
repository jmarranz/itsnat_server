package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodString;
import org.itsnat.droid.impl.xmlinflater.attr.MethodContainer;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_Chronometer_format extends AttrDescReflecMethodString
{
    protected MethodContainer methodContainer;

    public AttrDesc_widget_Chronometer_format(ClassDescViewBased parent)
    {
        super(parent,"format","%s"); // Android tiene un texto por defecto

        this.methodContainer = new MethodContainer(parent,"init",null);
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        super.setAttribute(view,value,oneTimeAttrProcess,pending);

        methodContainer.invoke(view); // Hay que llamar a este método init() sino no se entera del cambio, ni siquiera en creación via parse dinámico
    }
}
