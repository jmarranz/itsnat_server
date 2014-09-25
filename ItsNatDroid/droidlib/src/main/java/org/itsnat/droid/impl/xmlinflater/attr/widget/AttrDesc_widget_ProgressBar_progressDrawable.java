package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.graphics.drawable.Drawable;
import android.view.View;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.attr.MethodContainer;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 17/09/14.
 */
public class AttrDesc_widget_ProgressBar_progressDrawable extends AttrDescReflecMethodDrawable
{
    protected MethodContainer methodTileify;

    public AttrDesc_widget_ProgressBar_progressDrawable(ClassDescViewBased parent)
    {
        super(parent,"progressDrawable",null); // Valor por default: Android tiene un Drawable por defecto
        this.methodTileify = new MethodContainer(parent,"tileify",new Class[]{Drawable.class, boolean.class});
    }

    protected void callMethod(View view, Object convertedValue)
    {
        Drawable drawable = (Drawable)convertedValue;

        // En el código fuente del constructor se ve que hay un proceso previo (tileify) antes de llamar a setProgressDrawable()

        methodTileify.invoke(view, drawable, false);

        super.callMethod(view,drawable);
    }

}
