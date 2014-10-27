package org.itsnat.droid.impl.xmlinflater.stdalone;

import android.content.Context;

import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.parser.TreeViewParsed;
import org.itsnat.droid.impl.xmlinflater.InflatedLayoutImpl;

/**
 * Created by jmarranz on 20/08/14.
 */
public class InflatedLayoutStandaloneImpl extends InflatedLayoutImpl
{
    public InflatedLayoutStandaloneImpl(ItsNatDroidImpl itsNatDroid,TreeViewParsed treeViewParsed, AttrCustomInflaterListener inflateListener, Context ctx)
    {
        super(itsNatDroid,treeViewParsed,inflateListener, ctx);
    }
}
