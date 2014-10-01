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
                    // Como referenciamos un View hijo con el id, es necesario que haya sido antes insertado
                    AttrDesc_widget_RadioGroup_checkedButton.super.setAttribute(view,value,oneTimeAttrProcess,pending);
                }
            });
        }
        else super.setAttribute(view,value,oneTimeAttrProcess,pending);
    }
}
