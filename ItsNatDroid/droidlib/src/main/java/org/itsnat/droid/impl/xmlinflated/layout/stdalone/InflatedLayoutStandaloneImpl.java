package org.itsnat.droid.impl.xmlinflated.layout.stdalone;

import android.content.Context;

import org.itsnat.droid.AttrLayoutInflaterListener;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.dom.layout.XMLDOMLayout;
import org.itsnat.droid.impl.xmlinflated.layout.InflatedLayoutImpl;

/**
 * Created by jmarranz on 20/08/14.
 */
public class InflatedLayoutStandaloneImpl extends InflatedLayoutImpl
{
    protected AttrLayoutInflaterListener inflateLayoutListener;

    public InflatedLayoutStandaloneImpl(ItsNatDroidImpl itsNatDroid,XMLDOMLayout domLayout, AttrLayoutInflaterListener inflateLayoutListener, Context ctx)
    {
        super(itsNatDroid, domLayout,ctx);

        this.inflateLayoutListener = inflateLayoutListener;
    }

    public AttrLayoutInflaterListener getAttrLayoutInflaterListener()
    {
        return inflateLayoutListener;
    }
}
