package org.itsnat.droid.impl.xmlinflated;

import android.content.Context;

import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.model.XMLParsed;
import org.itsnat.droid.impl.xmlinflater.XMLInflateRegistry;

/**
 * Created by jmarranz on 7/11/14.
 */
public abstract class InflatedXML
{
    public static final String XMLNS_ANDROID = "http://schemas.android.com/apk/res/android";

    protected ItsNatDroidImpl itsNatDroid;
    protected XMLParsed xmlParsed;
    protected Context ctx;

    protected InflatedXML(ItsNatDroidImpl itsNatDroid, XMLParsed xmlParsed, Context ctx)
    {
        // Este constructor puede llegar a ejecutarse en un hilo NO UI, no hacer nada más
        this.itsNatDroid = itsNatDroid;
        this.xmlParsed = xmlParsed;
        this.ctx = ctx;
    }

    public ItsNatDroidImpl getItsNatDroidImpl()
    {
        return itsNatDroid;
    }

    public XMLInflateRegistry getXMLInflateRegistry()
    {
        return itsNatDroid.getXMLInflateRegistry();
    }
}
