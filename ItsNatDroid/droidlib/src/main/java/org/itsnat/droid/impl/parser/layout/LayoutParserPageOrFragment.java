package org.itsnat.droid.impl.parser.layout;

import org.itsnat.droid.impl.dom.XMLParsed;
import org.itsnat.droid.impl.dom.layout.LayoutParsed;
import org.itsnat.droid.impl.dom.layout.ScriptInlineParsed;
import org.itsnat.droid.impl.dom.layout.ScriptRemoteParsed;
import org.itsnat.droid.impl.dom.layout.ViewParsed;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by jmarranz on 27/10/14.
 */
public abstract class LayoutParserPageOrFragment extends LayoutParser
{
    public LayoutParserPageOrFragment()
    {
    }

    @Override
    protected void parseScriptElement(XmlPullParser parser, ViewParsed viewParent,XMLParsed xmlParsed) throws IOException, XmlPullParserException
    {
        LayoutParsed layoutParsed = (LayoutParsed)xmlParsed;

        String src = findAttributeFromParser(null, "src", parser);
        if (src != null)
        {
            addScriptRemoteParsed(src,layoutParsed);
        }
        else
        {
            addScriptInlineParsed(parser,layoutParsed);
        }

        while (parser.next() != XmlPullParser.END_TAG) /*nop*/ ;
    }

    protected void addScriptRemoteParsed(String src,LayoutParsed layoutParsed)
    {
        // Si loadingPage es true es el caso de carga de página, pero si serverVersion es null dicha página
        // NO es servida por ItsNat, tenemos que cargar asíncronamente el archivo script pues este es el hilo UI :(
        // Si loadScript es null estamos en un evento (inserción de un fragment)

        ScriptRemoteParsed script = new ScriptRemoteParsed(src);
        layoutParsed.addScript(script);
    }

    protected void addScriptInlineParsed(XmlPullParser parser,LayoutParsed layoutParsed) throws IOException, XmlPullParserException
    {
        while (parser.next() != XmlPullParser.TEXT) /*nop*/ ;

        String code = parser.getText();

        ScriptInlineParsed script = new ScriptInlineParsed(code);
        layoutParsed.addScript(script);
    }
}
