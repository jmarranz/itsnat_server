package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.view.View;

import org.itsnat.droid.impl.dom.AttrParsed;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecFieldSetInt;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 17/09/14.
 */
public class AttrDescView_widget_ProgressBar_indeterminateBehavior extends AttrDescViewReflecFieldSetInt
{
    public AttrDescView_widget_ProgressBar_indeterminateBehavior(ClassDescViewBased parent)
    {
        super(parent,"indeterminateBehavior","mBehavior",1 /*repeat*/); // Valor por default: en el código fuente se ve un AlphaAnimation.RESTART que afortunadamente tiene el valor 1 (idem repeat) y probando a no poner el atributo es un repeat
    }

    @Override
    public void setAttribute(View view, AttrParsed attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        // Redefinimos setAttribute porque el dato dado es una cadena no un entero
        String value = attr.getValue();
        int convertedValue;
        if ("repeat".equals(value))  // Siempre en el mismo sentido
            convertedValue = 1;
        else if ("cycle".equals(value)) // Da como una vuelta y media y cambia de sentido
            convertedValue = 2;
        else
            convertedValue = 1; // En el código fuente se ve un AlphaAnimation.RESTART que afortunadamente tiene el valor 1 (idem repeat)

        setField(view,convertedValue);
    }

}
