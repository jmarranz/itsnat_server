package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_ViewGroup_descendantFocusability extends AttrDesc
{
    public AttrDesc_view_ViewGroup_descendantFocusability(ClassDescViewBased parent)
    {
        super(parent,"descendantFocusability");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess)
    {
        int intValue;
        if ("beforeDescendants".equals(value))
            intValue = ViewGroup.FOCUS_BEFORE_DESCENDANTS;
        else if ("afterDescendants".equals(value))
            intValue = ViewGroup.FOCUS_AFTER_DESCENDANTS;
        else if ("blocksDescendants".equals(value))
            intValue = ViewGroup.FOCUS_BLOCK_DESCENDANTS;
        else
            throw new ItsNatDroidException("Unrecognized value " + value + " for attribute " + name);

        ((ViewGroup)view).setDescendantFocusability(intValue);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"beforeDescendants",null);
    }
}
