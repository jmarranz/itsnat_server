package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_TextView_imeActionLabel extends AttrDescView
{
    public AttrDescView_widget_TextView_imeActionLabel(ClassDescViewBased parent)
    {
        super(parent,"imeActionLabel");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        String convertedValue = getString(value,view.getContext());

        TextView textView = (TextView)view;
        textView.setImeActionLabel(convertedValue,textView.getImeActionId());
    }

    public void removeAttribute(View view)
    {
        setAttribute(view, "", null,null); // No estoy seguro del valor por defecto
    }

}
