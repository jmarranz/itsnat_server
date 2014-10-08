package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodCharSequence;
import org.itsnat.droid.impl.xmlinflater.attr.MethodContainer;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_ToggleButton_textOffandOn extends AttrDescReflecMethodCharSequence
{
    protected MethodContainer methodContainer;

    public AttrDesc_widget_ToggleButton_textOffandOn(ClassDescViewBased parent,String name)
    {
        super(parent,name,null); // Android tiene un texto por defecto

        this.methodContainer = new MethodContainer(parent,"syncTextState",null);
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        super.setAttribute(view,value,oneTimeAttrProcess,pending);

        methodContainer.invoke(view); // Hay que llamar a este método syncTextState sino no se entera del cambio, ni siquiera en creación via parse dinámico
    }
}
