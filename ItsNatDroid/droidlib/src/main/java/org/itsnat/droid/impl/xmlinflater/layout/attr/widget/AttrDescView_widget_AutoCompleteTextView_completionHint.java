package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.view.View;

import org.itsnat.droid.impl.dom.AttrParsed;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodCharSequence;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_AutoCompleteTextView_completionHint extends AttrDescViewReflecMethodCharSequence
{

    public AttrDescView_widget_AutoCompleteTextView_completionHint(ClassDescViewBased parent)
    {
        super(parent,"completionHint","");
    }

    public void setAttribute(final View view, final AttrParsed attr,final XMLInflaterLayout xmlInflaterLayout, final Context ctx, final OneTimeAttrProcess oneTimeAttrProcess, final PendingPostInsertChildrenTasks pending)
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
                    AttrDescView_widget_AutoCompleteTextView_completionHint.super.setAttribute(view,attr,xmlInflaterLayout, ctx, oneTimeAttrProcess, pending);
                }
            });
        }
        else super.setAttribute(view,attr, xmlInflaterLayout, ctx, oneTimeAttrProcess,pending);
    }
}
