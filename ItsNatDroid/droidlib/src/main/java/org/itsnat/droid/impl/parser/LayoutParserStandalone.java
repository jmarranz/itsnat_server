package org.itsnat.droid.impl.parser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by jmarranz on 27/10/14.
 */
public class LayoutParserStandalone extends LayoutParser
{
    public LayoutParserStandalone()
    {
    }

    @Override
    protected void parseScriptElement(XmlPullParser parser, ViewParsed viewParent, TreeViewParsed treeView) throws IOException, XmlPullParserException
    {
        android.util.Log.v("LayoutParserStandalone","<script> elements are ignored in standalone layouts");
    }
}
