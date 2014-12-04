package org.itsnat.droid.impl.xmlinflater;

import android.content.Context;

import org.itsnat.droid.AttrDrawableInflaterListener;
import org.itsnat.droid.AttrLayoutInflaterListener;
import org.itsnat.droid.impl.xmlinflated.InflatedXML;

/**
 * Created by jmarranz on 4/11/14.
 */
public abstract class XMLInflater
{
    protected InflatedXML inflatedXML;
    protected int bitmapDensityReference;
    protected AttrLayoutInflaterListener attrLayoutInflaterListener;
    protected AttrDrawableInflaterListener attrDrawableInflaterListener;
    protected Context ctx;

    protected XMLInflater(InflatedXML inflatedXML,int bitmapDensityReference,AttrLayoutInflaterListener attrLayoutInflaterListener,AttrDrawableInflaterListener attrDrawableInflaterListener,Context ctx)
    {
        this.inflatedXML = inflatedXML;
        this.bitmapDensityReference = bitmapDensityReference;
        this.attrLayoutInflaterListener = attrLayoutInflaterListener;
        this.attrDrawableInflaterListener = attrDrawableInflaterListener;
        this.ctx = ctx;
    }

    public InflatedXML getInflatedXML()
    {
        return inflatedXML;
    }

    public int getBitmapDensityReference()
    {
        return bitmapDensityReference;
    }

    public AttrLayoutInflaterListener getAttrLayoutInflaterListener()
    {
        return attrLayoutInflaterListener;
    }

    public AttrDrawableInflaterListener getAttrDrawableInflaterListener()
    {
        return attrDrawableInflaterListener;
    }

    public Context getContext()
    {
        return ctx;
    }
}
