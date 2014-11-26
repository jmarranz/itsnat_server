package org.itsnat.droid.impl.domparser.layout;

import android.content.res.AssetManager;

import org.itsnat.droid.impl.dom.XMLDOM;
import org.itsnat.droid.impl.dom.layout.DOMView;
import org.itsnat.droid.impl.xmlinflater.XMLInflateRegistry;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by jmarranz on 27/10/14.
 */
public class XMLDOMLayoutParserStandalone extends XMLDOMLayoutParser
{
    public XMLDOMLayoutParserStandalone(XMLInflateRegistry xmlInflateRegistry,AssetManager assetManager)
    {
        super(xmlInflateRegistry,assetManager);
    }

    @Override
    protected void parseScriptElement(XmlPullParser parser, DOMView viewParent,XMLDOM xmlDOM) throws IOException, XmlPullParserException
    {
        android.util.Log.v("LayoutParserStandalone","<script> elements are ignored in standalone layouts");

        while (parser.next() != XmlPullParser.END_TAG) /*nop*/ ;
    }
}
