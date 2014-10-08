package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodCharSequence;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_AutoCompleteTextView_completionHint extends AttrDescReflecMethodCharSequence
{

    public AttrDesc_widget_AutoCompleteTextView_completionHint(ClassDescViewBased parent)
    {
        super(parent,"completionHint","");
    }

    public void setAttribute(final View view,final String value,final OneTimeAttrProcess oneTimeAttrProcess,final PendingPostInsertChildrenTasks pending)
    {
        if (oneTimeAttrProcess != null)
        {
            // Necesitamos definir antes de completionHint el atributo completionHintView, pues en setCompletionHint(CharSequence) es cuando
            // carga el layout definido en completionHintView

            oneTimeAttrProcess.addLastTask(new Runnable()
            {
                @Override
                public void run()
                {
                    AttrDesc_widget_AutoCompleteTextView_completionHint.super.setAttribute(view,value,oneTimeAttrProcess,pending);
                }
            });
        }
        else super.setAttribute(view,value,oneTimeAttrProcess,pending);
    }
}
