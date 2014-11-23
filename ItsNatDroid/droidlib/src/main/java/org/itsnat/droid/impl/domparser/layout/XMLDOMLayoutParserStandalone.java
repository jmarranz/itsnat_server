package org.itsnat.droid.impl.domparser.layout;

import org.itsnat.droid.impl.dom.XMLDOM;
import org.itsnat.droid.impl.dom.layout.DOMView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by jmarranz on 27/10/14.
 */
public class XMLDOMLayoutParserStandalone extends XMLDOMLayoutParser
{
    public XMLDOMLayoutParserStandalone()
    {
    }

    @Override
    protected void parseScriptElement(XmlPullParser parser, DOMView viewParent,XMLDOM xmlDOM) throws IOException, XmlPullParserException
    {
        android.util.Log.v("LayoutParserStandalone","<script> elements are ignored in standalone layouts");

        while (parser.next() != XmlPullParser.END_TAG) /*nop*/ ;
    }
}
