package org.itsnat.droid.impl.parser.drawable;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.model.ElementParsed;
import org.itsnat.droid.impl.model.ElementParsedDefault;
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
public class DrawableParser extends XMLParserBase
{
    public static DrawableParsed parse(String markup)
    {
        StringReader input = new StringReader(markup);
        return parse(input);
    }

    private static DrawableParsed parse(Reader input)
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

    private static DrawableParsed parse(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        String rootElemName = getRootElementName(parser);
        DrawableParser drawableParser = new DrawableParser();
        DrawableParsed drawableParsed = new DrawableParsed();
        drawableParser.parseRootElement(rootElemName, parser, drawableParsed);
        return drawableParsed;
    }

    @Override
    protected ElementParsed createRootElement(String name)
    {
        return new ElementParsedDefault(name,null);
    }
}
