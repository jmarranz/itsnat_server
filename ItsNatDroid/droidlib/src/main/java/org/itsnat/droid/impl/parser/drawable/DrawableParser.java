package org.itsnat.droid.impl.parser.drawable;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.model.drawable.DrawableParsed;
import org.itsnat.droid.impl.model.drawable.NinePatchDrawableParsed;
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
    public static DrawableParsed parse(String markup)
    {
        StringReader input = new StringReader(markup);
        return (DrawableParsed) parse(input);
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
        DrawableParser drawableParser = null;
        DrawableParsed drawableParsed = null;
        if ("nine-patch".equals(rootElemName))
        {
            drawableParser = new NinePatchDrawableParser();
            drawableParsed = new NinePatchDrawableParsed();
        }
        drawableParser.parseRootElement(rootElemName, parser, drawableParsed);
        return drawableParsed;
    }
}
