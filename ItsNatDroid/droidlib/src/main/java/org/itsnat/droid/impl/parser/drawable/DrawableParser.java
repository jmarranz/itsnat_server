package org.itsnat.droid.impl.parser.drawable;

import android.util.Xml;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.model.ElementParsed;
import org.itsnat.droid.impl.model.drawable.DrawableParsed;
import org.itsnat.droid.impl.parser.XMLParserBase;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by jmarranz on 31/10/14.
 */
public abstract class DrawableParser extends XMLParserBase
{
    public static DrawableParsed inflate(String markup)
    {
        StringReader input = new StringReader(markup);
        return (DrawableParsed)inflate(input);
    }

    private static ElementParsed inflate(Reader input)
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(input);
            return inflate(parser);
        }
        catch (IOException ex) { throw new ItsNatDroidException(ex); }
        catch (XmlPullParserException ex) { throw new ItsNatDroidException(ex); }
        finally
        {
            try { input.close(); }
            catch (IOException ex) { throw new ItsNatDroidException(ex); }
        }
    }

    private static ElementParsed inflate(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        String rootElemName = getRootElementName(parser);

        XMLParserBase xmlParser = null;
        if ("nine-patch".equals(rootElemName))
        {
            xmlParser = new NinePatchDrawableParser();
        }
        return xmlParser.parseRootElement(rootElemName,parser);
    }

}
