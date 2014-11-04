package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.animation.LayoutTransition;
import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_ViewGroup_animateLayoutChanges extends AttrDesc
{
    public AttrDesc_view_ViewGroup_animateLayoutChanges(ClassDescViewBased parent)
    {
        super(parent,"animateLayoutChanges");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        // Tomado del código fuente de ViewGroup
        // http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/4.0.3_r1/android/view/ViewGroup.java?av=f
        LayoutTransition trn = "true".equals(value) ? new LayoutTransition() : null; // null es el valor por defecto

        ((ViewGroup)view).setLayoutTransition(trn);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"false",null,null);
    }
}
