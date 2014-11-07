package org.itsnat.droid.impl.xmlinflated;

import android.content.Context;

import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.model.XMLParsed;
import org.itsnat.droid.impl.xmlinflater.XMLInflater;

/**
 * Created by jmarranz on 7/11/14.
 */
public abstract class InflatedXML
{
    public static final String XMLNS_ANDROID = "http://schemas.android.com/apk/res/android";

    protected ItsNatDroidImpl itsNatDroid;
    protected XMLInflater xmlInflater;
    protected XMLParsed xmlParsed;
    protected Context ctx;

    protected InflatedXML(ItsNatDroidImpl itsNatDroid, XMLParsed xmlParsed, Context ctx)
    {
        this.itsNatDroid = itsNatDroid;
        this.xmlParsed = xmlParsed;
        this.ctx = ctx;
        this.xmlInflater = createXMLInflater();
    }

    public abstract XMLInflater createXMLInflater();
}
