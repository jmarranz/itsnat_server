package org.itsnat.droid.impl.xmlinflated.drawable;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.itsnat.droid.AttrDrawableInflaterListener;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.model.drawable.DrawableParsed;
import org.itsnat.droid.impl.xmlinflated.InflatedXML;
import org.itsnat.droid.impl.xmlinflater.XMLInflater;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterNinePatchDrawable;

/**
 * Created by jmarranz on 7/11/14.
 */
public abstract class InflatedDrawable extends InflatedXML
{
    protected Drawable drawable;
    protected AttrDrawableInflaterListener inflateDrawableListener;

    public InflatedDrawable(ItsNatDroidImpl itsNatDroid,DrawableParsed drawableParsed,AttrDrawableInflaterListener inflateDrawableListener,Context ctx)
    {
        super(itsNatDroid,drawableParsed,ctx);

        this.inflateDrawableListener = inflateDrawableListener;
    }

    public DrawableParsed getDrawableParsed()
    {
        return (DrawableParsed)xmlParsed;
    }

    public AttrDrawableInflaterListener getAttrDrawableInflaterListener()
    {
        return inflateDrawableListener;
    }

    @Override
    public XMLInflater createXMLInflater()
    {
        return new XMLInflaterNinePatchDrawable(this);
    }
}
