package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodId;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_RadioGroup_checkedButton extends AttrDescReflecMethodId
{
    public AttrDesc_widget_RadioGroup_checkedButton(ClassDescViewBased parent)
    {
        super(parent, "checkedButton", "check");
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
