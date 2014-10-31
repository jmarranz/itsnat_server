package org.itsnat.droid.impl.parser.layout;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.model.layout.LayoutParsed;
import org.itsnat.droid.impl.model.layout.ScriptInlineParsed;
import org.itsnat.droid.impl.model.layout.ScriptRemoteParsed;
import org.itsnat.droid.impl.model.layout.ViewParsed;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by jmarranz on 27/10/14.
 */
public class LayoutParserPage extends LayoutParser
{
    protected final String itsNatServerVersion; // Puede ser null, layout no servido por ItsNat
    protected final boolean loadingPage;

    public LayoutParserPage(String itsNatServerVersion,boolean loadingPage)
    {
        this.itsNatServerVersion = itsNatServerVersion;
        this.loadingPage = loadingPage;
    }

    private boolean isPageServedByItsNat()
    {
        return (itsNatServerVersion != null); // Si es null es que no se ha servidor a través de ItsNat framework server
    }

    @Override
    protected void parseScriptElement(XmlPullParser parser, ViewParsed viewParent, LayoutParsed layoutParsed) throws IOException, XmlPullParserException
    {
        String src = findAttributeFromParser(null, "src", parser);
        if (src != null)
        {
            if (loadingPage && isPageServedByItsNat())
            {
                // Los <script src="localfile"> se procesan en el servidor ItsNat cargando el archivo de forma síncrona y metiendo
                // el script como nodo de texto dentro de un "nuevo" <script> que desde luego NO tiene el atributo src
                throw new ItsNatDroidException("Internal Error");
            }

            // Si loadingPage es true es el caso de carga de página, pero si serverVersion es null dicha página
            // NO es servida por ItsNat, tenemos que cargar asíncronamente el archivo script pues este es el hilo UI :(
            // Si loadScript es null estamos en un evento (inserción de un fragment)

            ScriptRemoteParsed script = new ScriptRemoteParsed(src);
            layoutParsed.addScript(script);

            while (parser.next() != XmlPullParser.END_TAG) /*nop*/ ;
        }
        else
        {
            boolean isLoadScript = loadingPage && parser.getAttributeCount() == 1 &&
                    "id".equals(parser.getAttributeName(0)) &&
                    "itsnat_load_script".equals(parser.getAttributeValue(0));

            while (parser.next() != XmlPullParser.TEXT) /*nop*/ ;

            String code = parser.getText();
            if (isLoadScript) layoutParsed.setLoadScript(code);
            else
            {
                ScriptInlineParsed script = new ScriptInlineParsed(code);
                layoutParsed.addScript(script);
            }

            while (parser.next() != XmlPullParser.END_TAG) /*nop*/ ;
        }
    }
}
