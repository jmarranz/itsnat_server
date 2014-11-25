package org.itsnat.droid.impl.domparser.drawable;

import android.content.res.AssetManager;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.dom.DOMElement;
import org.itsnat.droid.impl.dom.DOMElementDefault;
import org.itsnat.droid.impl.dom.drawable.XMLDOMDrawable;
import org.itsnat.droid.impl.domparser.XMLDOMParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by jmarranz on 31/10/14.
 */
public class XMLDOMDrawableParser extends XMLDOMParser
{
    public XMLDOMDrawableParser(AssetManager assetManager)
    {
        super(assetManager);
    }

    public static XMLDOMDrawable parse(String markup,AssetManager assetManager)
    {
        StringReader input = new StringReader(markup);
        return parse(input,assetManager);
    }

    private static XMLDOMDrawable parse(Reader input,AssetManager assetManager)
    {
        try
        {
            XmlPullParser parser = newPullParser(input);
            return parse(parser,assetManager);
        }
        catch (IOException ex) { throw new ItsNatDroidException(ex); }
        catch (XmlPullParserException ex) { throw new ItsNatDroidException(ex); }
        finally
        {
            try { input.close(); }
            catch (IOException ex) { throw new ItsNatDroidException(ex); }
        }
    }

    private static XMLDOMDrawable parse(XmlPullParser parser,AssetManager assetManager) throws IOException, XmlPullParserException
    {
        String rootElemName = getRootElementName(parser);
        XMLDOMDrawableParser drawableParser = new XMLDOMDrawableParser(assetManager);
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
