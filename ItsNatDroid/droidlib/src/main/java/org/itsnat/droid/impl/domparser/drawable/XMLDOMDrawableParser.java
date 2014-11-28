package org.itsnat.droid.impl.domparser.drawable;

import android.content.res.AssetManager;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.dom.DOMElement;
import org.itsnat.droid.impl.dom.DOMElementDefault;
import org.itsnat.droid.impl.dom.drawable.XMLDOMDrawable;
import org.itsnat.droid.impl.domparser.XMLDOMParser;
import org.itsnat.droid.impl.xmlinflater.XMLInflateRegistry;
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
    public XMLDOMDrawableParser(XMLInflateRegistry xmlInflateRegistry,AssetManager assetManager)
    {
        super(xmlInflateRegistry,assetManager);
    }

    public static XMLDOMDrawable parse(String markup,XMLInflateRegistry xmlInflateRegistry,AssetManager assetManager)
    {
        StringReader input = new StringReader(markup);
        return parse(input,xmlInflateRegistry,assetManager);
    }

    private static XMLDOMDrawable parse(Reader input,XMLInflateRegistry xmlInflateRegistry,AssetManager assetManager)
    {
        try
        {
            XmlPullParser parser = newPullParser(input);
            return parse(parser,xmlInflateRegistry,assetManager);
        }
        catch (IOException ex) { throw new ItsNatDroidException(ex); }
        catch (XmlPullParserException ex) { throw new ItsNatDroidException(ex); }
        finally
        {
            try { input.close(); }
            catch (IOException ex) { throw new ItsNatDroidException(ex); }
        }
    }

    private static XMLDOMDrawable parse(XmlPullParser parser,XMLInflateRegistry xmlInflateRegistry,AssetManager assetManager) throws IOException, XmlPullParserException
    {
        String rootElemName = getRootElementName(parser);
        XMLDOMDrawableParser drawableParser = new XMLDOMDrawableParser(xmlInflateRegistry,assetManager);
        XMLDOMDrawable xmlDOMDrawable = new XMLDOMDrawable();
        drawableParser.parseRootElement(rootElemName, parser, xmlDOMDrawable);
        return xmlDOMDrawable;
    }

    @Override
    protected DOMElement createElement(String name,DOMElement parent)
    {
        return new DOMElementDefault(name,parent);
    }
}
