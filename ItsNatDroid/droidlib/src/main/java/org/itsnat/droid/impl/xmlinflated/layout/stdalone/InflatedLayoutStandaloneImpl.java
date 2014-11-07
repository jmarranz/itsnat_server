package org.itsnat.droid.impl.xmlinflated.layout.stdalone;

import android.content.Context;

import org.itsnat.droid.AttrLayoutInflaterListener;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.model.layout.LayoutParsed;
import org.itsnat.droid.impl.xmlinflated.layout.InflatedLayoutImpl;
import org.itsnat.droid.impl.xmlinflater.XMLInflater;
import org.itsnat.droid.impl.xmlinflater.layout.stdalone.XMLInflaterLayoutStandalone;

/**
 * Created by jmarranz on 20/08/14.
 */
public class InflatedLayoutStandaloneImpl extends InflatedLayoutImpl
{
    public InflatedLayoutStandaloneImpl(ItsNatDroidImpl itsNatDroid,LayoutParsed layoutParsed, AttrLayoutInflaterListener inflateLayoutListener, Context ctx)
    {
        super(itsNatDroid, layoutParsed,inflateLayoutListener, ctx);
    }

    @Override
    public XMLInflater createXMLInflater()
    {
        return new XMLInflaterLayoutStandalone(this);
    }
}
