package org.itsnat.droid.impl.parser.layout;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.dom.layout.LayoutParsed;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by jmarranz on 14/11/14.
 */
public class LayoutParserPage extends LayoutParserPageOrFragment
{
    public static final boolean PRELOAD_SCRIPTS = false;

    protected final String itsNatServerVersion; // Puede ser null, layout no servido por ItsNat

    public LayoutParserPage(String itsNatServerVersion)
    {
        this.itsNatServerVersion = itsNatServerVersion;
    }

    private boolean isPageServedByItsNat()
    {
        return (itsNatServerVersion != null); // Si es null es que no se ha servidor a través de ItsNat framework server
    }

    @Override
    protected void addScriptRemoteParsed(String src,LayoutParsed layoutParsed)
    {
        if (PRELOAD_SCRIPTS && isPageServedByItsNat())
        {
            // En este caso los <script src="localfile"> se procesan en el servidor ItsNat cargando el archivo de forma síncrona y metiendo
            // el script como nodo de texto dentro de un "nuevo" <script> que desde luego NO tiene el atributo src
            // incluso en el <script> de inicialización
            throw new ItsNatDroidException("Internal Error");
        }

        super.addScriptRemoteParsed(src,layoutParsed);
    }

    @Override
    protected void addScriptInlineParsed(XmlPullParser parser,LayoutParsed layoutParsed) throws IOException, XmlPullParserException
    {
        boolean isLoadScript = isPageServedByItsNat() &&
                parser.getAttributeCount() == 1 &&
                "id".equals(parser.getAttributeName(0)) &&
                "itsnat_load_script".equals(parser.getAttributeValue(0));

        if (isLoadScript)
        {
            // Tratamiento especial
            while (parser.next() != XmlPullParser.TEXT) /*nop*/ ;
            String code = parser.getText();

            layoutParsed.setLoadScript(code);
        }
        else
            super.addScriptInlineParsed(parser,layoutParsed);
    }

}
