package org.itsnat.droid.impl.parser.layout;

import android.util.Xml;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.model.ElementParsed;
import org.itsnat.droid.impl.model.layout.LayoutParsed;
import org.itsnat.droid.impl.model.layout.ViewParsed;
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
    protected LayoutParsed layoutParsed;

    public LayoutParsed inflate(String markup)
    {
        this.layoutParsed = new LayoutParsed();
        StringReader input = new StringReader(markup);
        inflate(input);
        return layoutParsed;
    }

    private void inflate(Reader input)
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(input);
            inflate(parser);
        }
        catch (IOException ex) { throw new ItsNatDroidException(ex); }
        catch (XmlPullParserException ex) { throw new ItsNatDroidException(ex); }
        finally
        {
            try { input.close(); }
            catch (IOException ex) { throw new ItsNatDroidException(ex); }
        }
    }

    private void inflate(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        String rootElemName = getRootElementName(parser);
        parseRootElement(rootElemName,parser);
    }

    @Override
    public ElementParsed parseRootElement(String rootElemName, XmlPullParser parser) throws IOException, XmlPullParserException
    {
        int nsStart = parser.getNamespaceCount(parser.getDepth()-1);
        int nsEnd = parser.getNamespaceCount(parser.getDepth());
        for (int i = nsStart; i < nsEnd; i++)
        {
            String prefix = parser.getNamespacePrefix(i);
            String ns = parser.getNamespaceUri(i);
            layoutParsed.addNamespace(prefix, ns);
        }

        if (layoutParsed.getAndroidNSPrefix() == null)
            throw new ItsNatDroidException("Missing android namespace declaration in root element");

        return super.parseRootElement(rootElemName,parser);
    }

    @Override
    protected ElementParsed createRootElement(String name)
    {
        return new ViewParsed(name,null);
    }

    @Override
    protected void setRootElement(ElementParsed rootElement)
    {
        super.setRootElement(rootElement);

        layoutParsed.setRootView((ViewParsed)rootElement);
    }

    @Override
    protected ElementParsed processElement(String name, ElementParsed parentElement, XmlPullParser parser) throws IOException, XmlPullParserException
    {
        if (name.equals("script"))
        {
            parseScriptElement(parser,(ViewParsed)parentElement);
            return null; // Ignorar porque "desaparece"
        }
        else return super.processElement(name,parentElement,parser);
    }

    @Override
    protected void addAttribute(ElementParsed element,String namespaceURI,String name,String value)
    {
        ViewParsed viewParsed = (ViewParsed)element;
        if (viewParsed.getStyleAttr() == null && (namespaceURI == null) && "style".equals(name))
            viewParsed.setStyleAttr(value);
        else
            super.addAttribute(element, namespaceURI, name, value);
    }

    protected abstract void parseScriptElement(XmlPullParser parser,ViewParsed viewParent) throws IOException, XmlPullParserException;
}
