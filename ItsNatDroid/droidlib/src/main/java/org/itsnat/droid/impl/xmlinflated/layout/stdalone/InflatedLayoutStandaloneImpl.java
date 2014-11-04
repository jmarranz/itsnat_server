package org.itsnat.droid.impl.xmlinflated.layout.stdalone;

import android.content.Context;

import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.model.layout.LayoutParsed;
import org.itsnat.droid.impl.xmlinflated.layout.InflatedLayoutImpl;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.stdalone.XMLInflaterLayoutStandalone;

/**
 * Created by jmarranz on 20/08/14.
 */
public class InflatedLayoutStandaloneImpl extends InflatedLayoutImpl
{
    public InflatedLayoutStandaloneImpl(ItsNatDroidImpl itsNatDroid,LayoutParsed layoutParsed, AttrCustomInflaterListener inflateListener, Context ctx)
    {
        super(itsNatDroid, layoutParsed,inflateListener, ctx);
    }

    @Override
    public XMLInflaterLayout createXMLLayoutInflater()
    {
        return new XMLInflaterLayoutStandalone(this);
    }
}
