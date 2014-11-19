package org.itsnat.droid.impl.parser.layout;

import org.itsnat.droid.impl.dom.XMLParsed;
import org.itsnat.droid.impl.dom.layout.ViewParsed;
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
    protected void parseScriptElement(XmlPullParser parser, ViewParsed viewParent,XMLParsed xmlParsed) throws IOException, XmlPullParserException
    {
        android.util.Log.v("LayoutParserStandalone","<script> elements are ignored in standalone layouts");

        while (parser.next() != XmlPullParser.END_TAG) /*nop*/ ;
    }
}
