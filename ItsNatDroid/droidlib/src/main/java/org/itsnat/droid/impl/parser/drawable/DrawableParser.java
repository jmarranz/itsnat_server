package org.itsnat.droid.impl.parser.drawable;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.dom.DOMElement;
import org.itsnat.droid.impl.dom.DOMElementDefault;
import org.itsnat.droid.impl.dom.drawable.XMLDOMDrawable;
import org.itsnat.droid.impl.parser.XMLParserBase;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by jmarranz on 31/10/14.
 */
public class DrawableParser extends XMLParserBase
{
    public static XMLDOMDrawable parse(String markup)
    {
        StringReader input = new StringReader(markup);
        return parse(input);
    }

    private static XMLDOMDrawable parse(Reader input)
    {
        try
        {
            XmlPullParser parser = newPullParser(input);
            return parse(parser);
        }
        catch (IOException ex) { throw new ItsNatDroidException(ex); }
        catch (XmlPullParserException ex) { throw new ItsNatDroidException(ex); }
        finally
        {
            try { input.close(); }
            catch (IOException ex) { throw new ItsNatDroidException(ex); }
        }
    }

    private static XMLDOMDrawable parse(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        String rootElemName = getRootElementName(parser);
        DrawableParser drawableParser = new DrawableParser();
        XMLDOMDrawable xmlDOMDrawable = new XMLDOMDrawable();
        drawableParser.parseRootElement(rootElemName, parser, xmlDOMDrawable);
        return xmlDOMDrawable;
    }

    @Override
    protected DOMElement createRootElement(String name)
    {
        return new DOMElementDefault(name,null);
    }
}
