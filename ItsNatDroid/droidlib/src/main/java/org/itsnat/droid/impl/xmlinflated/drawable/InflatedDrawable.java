package org.itsnat.droid.impl.xmlinflated.drawable;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.itsnat.droid.AttrDrawableInflaterListener;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.model.drawable.DrawableParsed;
import org.itsnat.droid.impl.xmlinflated.InflatedXML;

/**
 * Created by jmarranz on 7/11/14.
 */
public abstract class InflatedDrawable extends InflatedXML
{
    protected Drawable drawable;

    public InflatedDrawable(ItsNatDroidImpl itsNatDroid,DrawableParsed drawableParsed,Context ctx)
    {
        super(itsNatDroid,drawableParsed,ctx);
    }

    public DrawableParsed getDrawableParsed()
    {
        return (DrawableParsed)xmlParsed;
    }

    public Drawable getDrawable()
    {
        return drawable;
    }

    public void setDrawable(Drawable drawable)
    {
        this.drawable = drawable;
    }

    public abstract AttrDrawableInflaterListener getAttrDrawableInflaterListener();
}