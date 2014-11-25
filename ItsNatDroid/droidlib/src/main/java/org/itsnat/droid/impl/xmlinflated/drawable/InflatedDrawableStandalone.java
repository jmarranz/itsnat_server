package org.itsnat.droid.impl.xmlinflated.drawable;

import android.content.Context;

import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.dom.drawable.XMLDOMDrawable;

/**
 * Created by jmarranz on 20/08/14.
 */
public class InflatedDrawableStandalone extends InflatedDrawable
{
    public InflatedDrawableStandalone(ItsNatDroidImpl itsNatDroid,XMLDOMDrawable xmlDOMDrawable,Context ctx)
    {
        super(itsNatDroid, xmlDOMDrawable,ctx);
    }
}
