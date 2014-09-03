package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_ViewGroup_persistentDrawingCache extends AttrDesc
{
    public AttrDesc_view_ViewGroup_persistentDrawingCache(ClassDescViewBased parent)
    {
        super(parent,"persistentDrawingCache");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess)
    {
        int intValue;
        if ("none".equals(value))
            intValue = ViewGroup.PERSISTENT_NO_CACHE;
        else if ("animation".equals(value))
            intValue = ViewGroup.PERSISTENT_ANIMATION_CACHE;
        else if ("scrolling".equals(value))
            intValue = ViewGroup.PERSISTENT_SCROLLING_CACHE;
        else if ("all".equals(value))
            intValue = ViewGroup.PERSISTENT_ALL_CACHES;
        else
            throw new ItsNatDroidException("Unrecognized value " + value + " for attribute " + name);

        ((ViewGroup)view).setPersistentDrawingCache(intValue);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"scrolling",null);
    }
}
