package org.itsnat.droid.impl.parser;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.HttpUtil;
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

    @Override
    protected void parseScriptElement(XmlPullParser parser, ViewParsed viewParent, TreeViewParsed treeView) throws IOException, XmlPullParserException
    {
        String src = findAttributeFromParser(null, "src", parser);
        if (src != null)
        {
            if (loadingPage && itsNatServerVersion != null)
            {
                // Los <script src="localfile"> se procesan en el servidor ItsNat cargando el archivo de forma síncrona y metiendo
                // el script como nodo de texto dentro de un "nuevo" <script> que desde luego NO tiene el atributo src
                throw new ItsNatDroidException("Internal Error");
            }

            // Si loadingPage es true es el caso de carga de página, pero si serverVersion es null dicha página
            // NO es servida por ItsNat, tenemos que cargar asíncronamente el archivo script pues este es el hilo UI :(
            // Si loadScript es null estamos en un evento (inserción de un fragment)

            treeView.addScript("itsNatDoc.downloadFile(\"" + src + "\",\"" + HttpUtil.MIME_BEANSHELL + "\");");

            while (parser.next() != XmlPullParser.END_TAG) /*nop*/ ;
        }
        else
        {
            boolean isLoadScript = loadingPage && parser.getAttributeCount() == 1 &&
                    "id".equals(parser.getAttributeName(0)) &&
                    "itsnat_load_script".equals(parser.getAttributeValue(0));

            while (parser.next() != XmlPullParser.TEXT) /*nop*/ ;

            String code = parser.getText();
            if (isLoadScript) treeView.setLoadScript(code);
            else treeView.addScript(code);

            while (parser.next() != XmlPullParser.END_TAG) /*nop*/ ;
        }
    }
}
