package org.itsnat.droid.impl.xmlinflater;

import android.content.Context;

import org.itsnat.droid.impl.xmlinflated.InflatedXML;
import org.itsnat.droid.impl.xmlinflated.drawable.InflatedDrawable;
import org.itsnat.droid.impl.xmlinflated.layout.InflatedLayoutImpl;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;

/**
 * Created by jmarranz on 4/11/14.
 */
public abstract class XMLInflater
{
    protected Context ctx;

    protected XMLInflater(Context ctx)
    {
        this.ctx = ctx;
    }

    public static XMLInflater createXMLInflater(InflatedXML inflatedXML,Context ctx)
    {
        if (inflatedXML instanceof InflatedLayoutImpl)
        {
            return XMLInflaterLayout.createXMLInflatedLayout((InflatedLayoutImpl) inflatedXML,ctx);
        }
        else if (inflatedXML instanceof InflatedDrawable)
        {
            return XMLInflaterDrawable.createXMLInflaterDrawable((InflatedDrawable) inflatedXML,ctx);
        }
        return null;
    }
}
