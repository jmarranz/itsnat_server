package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.view.View;
import android.widget.ListPopupWindow;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecFieldMethod;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_Spinner_dropDownHorizontalOffset extends AttrDescReflecFieldMethod
{
    public AttrDesc_widget_Spinner_dropDownHorizontalOffset(ClassDescViewBased parent)
    {
        super(parent,"dropDownHorizontalOffset","mPopup","setHorizontalOffset",ListPopupWindow.class,int.class);
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int convertedValue = getDimensionInt(value, view.getContext());

        try
        {
            callFieldMethod(view, convertedValue);
        }
        catch(ItsNatDroidException ex)
        {
            throw new ItsNatDroidException("Setting the attribute dropDownHorizontalOffset is only valid in dropdown mode (not in dialog mode)",ex);
        }
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"0dp",null,null);
    }
}
