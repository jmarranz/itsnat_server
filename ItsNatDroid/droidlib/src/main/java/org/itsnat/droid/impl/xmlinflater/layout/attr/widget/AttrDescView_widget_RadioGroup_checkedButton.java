package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.view.View;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodId;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_RadioGroup_checkedButton extends AttrDescViewReflecMethodId
{
    public AttrDescView_widget_RadioGroup_checkedButton(ClassDescViewBased parent)
    {
        super(parent, "checkedButton", "check",-1);
    }

    public void setAttribute(final View view, final DOMAttr attr,final XMLInflaterLayout xmlInflaterLayout, final Context ctx, final OneTimeAttrProcess oneTimeAttrProcess, final PendingPostInsertChildrenTasks pending)
    {
        if (pending != null)
        {
            pending.addTask(new Runnable(){
                @Override
                public void run()
                {
                    // El addTask es porque referenciamos un View hijo antes de insertarse
                    // Pero NO es porque el id se referencia antes de insertar el hijo pues ese problema se resuelve con @+id
                    // en el propio checkedButton
                    AttrDescView_widget_RadioGroup_checkedButton.super.setAttribute(view,attr,xmlInflaterLayout, ctx, oneTimeAttrProcess, pending);
                }
            });
        }
        else super.setAttribute(view,attr, xmlInflaterLayout, ctx, oneTimeAttrProcess,pending);
    }
}
