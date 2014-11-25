package org.itsnat.droid.impl.xmlinflated.drawable;

import android.content.Context;

import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.dom.drawable.XMLDOMDrawable;

/**
 * Created by jmarranz on 10/11/14.
 */
public class InflatedDrawablePage extends InflatedDrawable
{
    public InflatedDrawablePage(ItsNatDroidImpl itsNatDroid,XMLDOMDrawable xmlDOMDrawable,Context ctx)
    {
        // Este constructor puede llegar a ejecutarse en un hilo NO UI, no hacer nada más
        super(itsNatDroid, xmlDOMDrawable, ctx);
    }
}
