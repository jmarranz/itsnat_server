package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_scroll_X_Y extends AttrDesc
{
    public AttrDesc_view_View_scroll_X_Y(ClassDescViewBased parent,String name)
    {
        super(parent,name);
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess)
    {
        // El uso de setScrollX/Y está sacado del código fuente: http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/4.0.3_r1/android/view/View.java?av=f
        // en tiempo de construcción se usa scrollTo(x,y) pero setScrollX y setScrollY acaban usando scrollTo
        int scroll = getDimensionInt(value, view.getContext());

        if (name.equals("scrollX"))
            view.setScrollX(scroll);
        else if (name.equals("scrollY"))
            view.setScrollY(scroll);
        else
            throw new ItsNatDroidException("INTERNAL ERROR");
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"0dp",null);
    }
}
