package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_TextView_textAllCaps extends AttrDescReflecMethodBoolean
{
    public AttrDesc_widget_TextView_textAllCaps(ClassDescViewBased parent)
    {
        super(parent,"textAllCaps","setAllCaps",false);
    }

    public void setAttribute(final View view,final String value, final OneTimeAttrProcess oneTimeAttrProcess,final PendingPostInsertChildrenTasks pending)
    {
        if (oneTimeAttrProcess != null)
        {
            // Delegamos al final porque es necesario que antes se ejecute textIsSelectable="false"
            oneTimeAttrProcess.addLastTask(new Runnable()
            {
                @Override
                public void run()
                {
                    AttrDesc_widget_TextView_textAllCaps.super.setAttribute(view,value,oneTimeAttrProcess,pending);
                }
            });
        }
        else
        {
            super.setAttribute(view,value,oneTimeAttrProcess,pending);
        }
    }

}
