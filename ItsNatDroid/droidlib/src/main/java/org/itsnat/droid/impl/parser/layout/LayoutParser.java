package org.itsnat.droid.impl.parser.layout;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.dom.ElementParsed;
import org.itsnat.droid.impl.dom.XMLParsed;
import org.itsnat.droid.impl.dom.layout.LayoutParsed;
import org.itsnat.droid.impl.dom.layout.ViewParsed;
import org.itsnat.droid.impl.parser.XMLParserBase;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;


/**
 * Created by jmarranz on 27/10/14.
 */
public abstract class LayoutParser extends XMLParserBase
{
    public LayoutParsed parse(String markup)
    {
        StringReader input = new StringReader(markup);
        return parse(input);
    }

    private LayoutParsed parse(Reader input)
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

    private LayoutParsed parse(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        String rootElemName = getRootElementName(parser);
        LayoutParsed layoutParsed = new LayoutParsed();
        parseRootElement(rootElemName,parser,layoutParsed);
        return layoutParsed;
    }

    @Override
    protected ElementParsed createRootElement(String name)
    {
        return new ViewParsed(name,null);
    }


    @Override
    protected ElementParsed processElement(String name, ElementParsed parentElement, XmlPullParser parser,XMLParsed xmlParsed) throws IOException, XmlPullParserException
    {
        if (name.equals("script"))
        {
            parseScriptElement(parser,(ViewParsed)parentElement,xmlParsed);
            return null; // Ignorar porque "desaparece"
        }
        else return super.processElement(name,parentElement,parser,xmlParsed);
    }

    @Override
    protected void addAttribute(ElementParsed element,String namespaceURI,String name,String value,XMLParsed xmlParsed)
    {
        ViewParsed viewParsed = (ViewParsed)element;
        if (viewParsed.getStyleAttr() == null && (namespaceURI == null) && "style".equals(name))
            viewParsed.setStyleAttr(value);
        else
            super.addAttribute(element, namespaceURI, name, value, xmlParsed);
    }

    protected abstract void parseScriptElement(XmlPullParser parser,ViewParsed viewParent,XMLParsed xmlParsed) throws IOException, XmlPullParserException;
}
