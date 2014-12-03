package org.itsnat.droid.impl.xmlinflater.drawable.stdalone;

import android.content.Context;

import org.itsnat.droid.AttrDrawableInflaterListener;
import org.itsnat.droid.AttrLayoutInflaterListener;
import org.itsnat.droid.impl.xmlinflated.drawable.InflatedDrawableStandalone;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;

/**
 * Created by jmarranz on 24/11/14.
 */
public class XMLInflaterDrawableStandalone extends XMLInflaterDrawable
{
    public XMLInflaterDrawableStandalone(InflatedDrawableStandalone inflatedXML,int referenceDensity,AttrLayoutInflaterListener attrLayoutInflaterListener,AttrDrawableInflaterListener attrDrawableInflaterListener, Context ctx)
    {
        super(inflatedXML,referenceDensity,attrLayoutInflaterListener,attrDrawableInflaterListener, ctx);
    }
}
