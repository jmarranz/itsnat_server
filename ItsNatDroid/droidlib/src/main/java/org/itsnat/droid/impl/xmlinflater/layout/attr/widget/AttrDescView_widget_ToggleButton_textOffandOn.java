package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.view.View;

import org.itsnat.droid.impl.dom.AttrParsed;
import org.itsnat.droid.impl.xmlinflater.MethodContainer;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodCharSequence;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_ToggleButton_textOffandOn extends AttrDescViewReflecMethodCharSequence
{
    protected MethodContainer methodContainer;

    public AttrDescView_widget_ToggleButton_textOffandOn(ClassDescViewBased parent, String name)
    {
        super(parent,name,null); // Android tiene un texto por defecto

        this.methodContainer = new MethodContainer(parent.getDeclaredClass(),"syncTextState",null);
    }

    public void setAttribute(View view, AttrParsed attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        super.setAttribute(view,attr, xmlInflaterLayout, ctx, oneTimeAttrProcess,pending);

        methodContainer.invoke(view); // Hay que llamar a este método syncTextState sino no se entera del cambio, ni siquiera en creación via parse dinámico
    }
}
