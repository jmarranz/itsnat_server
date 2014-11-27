package org.itsnat.droid.impl.xmlinflated;

import android.content.Context;

import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.dom.XMLDOM;
import org.itsnat.droid.impl.xmlinflater.XMLInflateRegistry;
import org.itsnat.droid.impl.xmlinflater.XMLInflater;

/**
 * Created by jmarranz on 7/11/14.
 */
public abstract class InflatedXML
{
    public static final String XMLNS_ANDROID = "http://schemas.android.com/apk/res/android";

    protected XMLInflater xmlInflater;
    protected ItsNatDroidImpl itsNatDroid;
    protected XMLDOM xmlDOM;
    protected Context ctx;

    protected InflatedXML(ItsNatDroidImpl itsNatDroid, XMLDOM xmlDOM, Context ctx)
    {
        // Este constructor puede llegar a ejecutarse en un hilo NO UI, no hacer nada más
        this.itsNatDroid = itsNatDroid;
        this.xmlDOM = xmlDOM;
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

    public XMLInflater getXMLInflater()
    {
        return xmlInflater;
    }

    public Context getContext()
    {
        return ctx;
    }
}
