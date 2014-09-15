package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_Spinner_dropDownWidth extends AttrDescReflecFieldSet
{
    public AttrDesc_widget_Spinner_dropDownWidth(ClassDescViewBased parent)
    {
        super(parent,"dropDownWidth","mDropDownWidth");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int convertedValue = getDimensionWithName(view, value);

        setField(view, convertedValue);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"wrap_content",null,null);
    }

}
