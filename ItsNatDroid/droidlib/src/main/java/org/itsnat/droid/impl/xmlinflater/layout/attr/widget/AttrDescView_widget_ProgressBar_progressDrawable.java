package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.graphics.drawable.Drawable;
import android.view.View;

import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.MethodContainer;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 17/09/14.
 */
public class AttrDescView_widget_ProgressBar_progressDrawable extends AttrDescViewReflecMethodDrawable
{
    protected MethodContainer methodTileify;

    public AttrDescView_widget_ProgressBar_progressDrawable(ClassDescViewBased parent)
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
