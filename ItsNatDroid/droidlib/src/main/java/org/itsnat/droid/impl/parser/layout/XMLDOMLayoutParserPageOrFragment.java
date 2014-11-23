package org.itsnat.droid.impl.parser.layout;

import org.itsnat.droid.impl.dom.XMLDOM;
import org.itsnat.droid.impl.dom.layout.DOMScriptInline;
import org.itsnat.droid.impl.dom.layout.XMLDOMLayout;
import org.itsnat.droid.impl.dom.layout.DOMScriptRemote;
import org.itsnat.droid.impl.dom.layout.DOMView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by jmarranz on 27/10/14.
 */
public abstract class XMLDOMLayoutParserPageOrFragment extends XMLDOMLayoutParser
{
    public XMLDOMLayoutParserPageOrFragment()
    {
    }

    @Override
    protected void parseScriptElement(XmlPullParser parser, DOMView viewParent,XMLDOM xmlDOM) throws IOException, XmlPullParserException
    {
        XMLDOMLayout domLayout = (XMLDOMLayout) xmlDOM;

        String src = findAttributeFromParser(null, "src", parser);
        if (src != null)
        {
            addDOMScriptRemote(src, domLayout);
        }
        else
        {
            addDOMScriptInline(parser, domLayout);
        }

        while (parser.next() != XmlPullParser.END_TAG) /*nop*/ ;
    }

    protected void addDOMScriptRemote(String src, XMLDOMLayout domLayout)
    {
        // Si loadingPage es true es el caso de carga de página, pero si serverVersion es null dicha página
        // NO es servida por ItsNat, tenemos que cargar asíncronamente el archivo script pues este es el hilo UI :(
        // Si loadScript es null estamos en un evento (inserción de un fragment)

        DOMScriptRemote script = new DOMScriptRemote(src);
        domLayout.addDOMScript(script);
    }

    protected void addDOMScriptInline(XmlPullParser parser, XMLDOMLayout domLayout) throws IOException, XmlPullParserException
    {
        while (parser.next() != XmlPullParser.TEXT) /*nop*/ ;

        String code = parser.getText();

        DOMScriptInline script = new DOMScriptInline(code);
        domLayout.addDOMScript(script);
    }
}
