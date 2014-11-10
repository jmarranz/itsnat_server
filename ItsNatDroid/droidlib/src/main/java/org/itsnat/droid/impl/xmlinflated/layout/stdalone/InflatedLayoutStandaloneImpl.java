package org.itsnat.droid.impl.xmlinflated.layout.stdalone;

import android.content.Context;

import org.itsnat.droid.AttrLayoutInflaterListener;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.model.layout.LayoutParsed;
import org.itsnat.droid.impl.xmlinflated.layout.InflatedLayoutImpl;

/**
 * Created by jmarranz on 20/08/14.
 */
public class InflatedLayoutStandaloneImpl extends InflatedLayoutImpl
{
    protected AttrLayoutInflaterListener inflateLayoutListener;

    public InflatedLayoutStandaloneImpl(ItsNatDroidImpl itsNatDroid,LayoutParsed layoutParsed, AttrLayoutInflaterListener inflateLayoutListener, Context ctx)
    {
        super(itsNatDroid, layoutParsed,ctx);

        this.inflateLayoutListener = inflateLayoutListener;
    }

    public AttrLayoutInflaterListener getAttrLayoutInflaterListener()
    {
        return inflateLayoutListener;
    }
}
