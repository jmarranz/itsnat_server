package org.itsnat.droid.impl.xmlinflater;

import android.content.Context;
import android.view.View;

import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

/**
 * Created by jmarranz on 20/08/14.
 */
public class InflatedLayoutStandaloneImpl extends InflatedLayoutImpl
{
    public InflatedLayoutStandaloneImpl(ItsNatDroidImpl parent, AttrCustomInflaterListener inflateListener, Context ctx)
    {
        super(parent, inflateListener, ctx);
    }

    protected void parseScriptElement(XmlPullParser parser,View viewParent, String[] loadScript,List<String> scriptList) throws IOException, XmlPullParserException
    {
        android.util.Log.v("InflatedLayoutStandaloneImpl","<script> elements are ignored in standalone layouts");
    }
}
