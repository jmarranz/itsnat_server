package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.view.View;

import org.itsnat.droid.impl.model.AttrParsed;
import org.itsnat.droid.impl.util.MiscUtil;
import org.itsnat.droid.impl.xmlinflater.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class AttrDescView extends AttrDesc
{
    private static Class class_R_styleable;

    public AttrDescView(ClassDescViewBased parent, String name)
    {
        super(parent,name);

        /* Para ver si nos hemos equivocado y name no se corresponde con el nombre de la clase específica
        String className = getClass().getName();
        if (!className.contains(name) &&
            !className.contains("AttrDescReflecView") &&
            !className.contains("AttrDescView_view_View_layout_rellayout_byId") &&
            !className.contains("AttrDescView_view_View_layout_rellayout_byBoolean")  )
            System.out.println("ERROR: " + className);
        */
    }

    protected static Class getClass_R_styleable()
    {
        if (class_R_styleable == null)
            class_R_styleable = MiscUtil.resolveClass("com.android.internal.R$styleable");
        return class_R_styleable;
    }


    public void setAttribute(View view, AttrParsed attr, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        setAttribute(view,attr.getValue(), oneTimeAttrProcess,pending);
    }

    protected abstract void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending);

    public abstract void removeAttribute(View view);
}


