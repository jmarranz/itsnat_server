package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodId;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_RadioGroup_checkedButton extends AttrDescReflecMethodId
{
    public AttrDesc_widget_RadioGroup_checkedButton(ClassDescViewBased parent)
    {
        super(parent, "checkedButton", "check",-1);
    }

    public void setAttribute(final View view, final String value, final OneTimeAttrProcess oneTimeAttrProcess,final PendingPostInsertChildrenTasks pending)
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
                    AttrDesc_widget_RadioGroup_checkedButton.super.setAttribute(view,value,oneTimeAttrProcess,pending);
                }
            });
        }
        else super.setAttribute(view,value,oneTimeAttrProcess,pending);
    }
}
