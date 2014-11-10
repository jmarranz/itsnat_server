package org.itsnat.droid.impl.xmlinflater;

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
    public static XMLInflater createXMLInflater(InflatedXML inflatedXML)
    {
        if (inflatedXML instanceof InflatedLayoutImpl)
        {
            return XMLInflaterLayout.createXMLInflatedLayout((InflatedLayoutImpl) inflatedXML);
        }
        else if (inflatedXML instanceof InflatedDrawable)
        {
            return XMLInflaterDrawable.createXMLInflaterDrawable((InflatedDrawable) inflatedXML);
        }
        return null;
    }
}
