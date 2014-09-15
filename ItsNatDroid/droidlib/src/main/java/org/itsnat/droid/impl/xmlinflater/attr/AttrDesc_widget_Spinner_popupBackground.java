package org.itsnat.droid.impl.xmlinflater.attr;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ListPopupWindow;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_Spinner_popupBackground extends AttrDescReflecFieldMethod
{
    public AttrDesc_widget_Spinner_popupBackground(ClassDescViewBased parent)
    {
        super(parent,"popupBackground","mPopup","setBackgroundDrawable",ListPopupWindow.class,Drawable.class);
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        Drawable convertedValue = getDrawable(value, view.getContext());

        try
        {
            callFieldMethod(view, convertedValue);
        }
        catch(ItsNatDroidException ex)
        {
            throw new ItsNatDroidException("Setting the attribute popupBackground only is valid in dropdown mode (not in dialog mode)",ex);
        }
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"wrap_content",null,null);
    }
}
