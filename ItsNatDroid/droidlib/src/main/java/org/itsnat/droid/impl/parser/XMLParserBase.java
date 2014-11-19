package org.itsnat.droid.impl.parser;

import android.util.Xml;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.dom.AttrParsed;
import org.itsnat.droid.impl.dom.AttrParsedRemote;
import org.itsnat.droid.impl.dom.ElementParsed;
import org.itsnat.droid.impl.dom.XMLParsed;
import org.itsnat.droid.impl.util.ValueUtil;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by jmarranz on 31/10/14.
 */
public abstract class XMLParserBase
{
    public XMLParserBase()
    {
    }

    public static XmlPullParser newPullParser(Reader input)
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(input);
            return parser;
        }
        catch (XmlPullParserException ex) { throw new ItsNatDroidException(ex); }
    }

    protected void setRootElement(ElementParsed rootElement,XMLParsed xmlParsed)
    {
        xmlParsed.setRootElement(rootElement);
    }

    public ElementParsed parseRootElement(String rootElemName,XmlPullParser parser,XMLParsed xmlParsed) throws IOException, XmlPullParserException
    {
        int nsStart = parser.getNamespaceCount(parser.getDepth()-1);
        int nsEnd = parser.getNamespaceCount(parser.getDepth());
        for (int i = nsStart; i < nsEnd; i++)
        {
            String prefix = parser.getNamespacePrefix(i);
            String ns = parser.getNamespaceUri(i);
            xmlParsed.addNamespace(prefix, ns);
        }

        if (xmlParsed.getAndroidNSPrefix() == null)
            throw new ItsNatDroidException("Missing android namespace declaration in root element");


        ElementParsed rootElement = createRootElementAndFillAttributes(rootElemName, parser,xmlParsed);

        processChildElements(rootElement,parser,xmlParsed);

        return rootElement;
    }

    protected ElementParsed createRootElementAndFillAttributes(String name,XmlPullParser parser,XMLParsed xmlParsed)
    {
        ElementParsed rootElement = createRootElement(name);

        setRootElement(rootElement,xmlParsed); // Cuanto antes

        fillAttributesAndAddElement(null, rootElement,parser,xmlParsed);

        return rootElement;
    }

    protected ElementParsed createElementAndFillAttributesAndAdd(String name, ElementParsed parentElement, XmlPullParser parser,XMLParsed xmlParsed)
    {
        // parentElement es null en el caso de parseo de fragment
        ElementParsed element = createRootElement(name);

        fillAttributesAndAddElement(parentElement, element,parser,xmlParsed);

        return element;
    }

    protected void fillAttributesAndAddElement(ElementParsed parentElement, ElementParsed element,XmlPullParser parser,XMLParsed xmlParsed)
    {
        fillElementAttributes(element,parser,xmlParsed);
        if (parentElement != null) parentElement.addChild(element);
    }

    protected void fillElementAttributes(ElementParsed element,XmlPullParser parser,XMLParsed xmlParsed)
    {
        int len = parser.getAttributeCount();
        element.initAttribList(len);
        for (int i = 0; i < len; i++)
        {
            String namespaceURI = parser.getAttributeNamespace(i);
            if ("".equals(namespaceURI)) namespaceURI = null; // Por estandarizar
            String name = parser.getAttributeName(i); // El nombre devuelto no contiene el namespace
            String value = parser.getAttributeValue(i);
            addAttribute(element, namespaceURI, name, value,xmlParsed);
        }
    }

    protected void processChildElements(ElementParsed parentElement,XmlPullParser parser,XMLParsed xmlParsed) throws IOException, XmlPullParserException
    {
        ElementParsed childView = parseNextChild(parentElement,parser,xmlParsed);
        while (childView != null)
        {
            childView = parseNextChild(parentElement,parser,xmlParsed);
        }
    }

    private ElementParsed parseNextChild(ElementParsed parentElement,XmlPullParser parser,XMLParsed xmlParsed) throws IOException, XmlPullParserException
    {
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG) // Nodo de texto etc
                continue;

            String name = parser.getName(); // viewName lo normal es que sea un nombre corto por ej RelativeLayout

            ElementParsed element = processElement(name, parentElement, parser,xmlParsed);
            if (element == null) continue; // Se ignora
            return element;
        }
        return null;
    }

    protected ElementParsed processElement(String name, ElementParsed parentElement, XmlPullParser parser,XMLParsed xmlParsed) throws IOException, XmlPullParserException
    {
        ElementParsed element = createElementAndFillAttributesAndAdd(name, parentElement, parser,xmlParsed);
        processChildElements(element,parser,xmlParsed);
        return element;
    }

    protected static String findAttributeFromParser(String namespaceURI, String name, XmlPullParser parser)
    {
        for(int i = 0; i < parser.getAttributeCount(); i++)
        {
            String currNamespaceURI = parser.getAttributeNamespace(i);
            if ("".equals(currNamespaceURI)) currNamespaceURI = null; // Por estandarizar
            if (!ValueUtil.equalsNullAllowed(currNamespaceURI, namespaceURI)) continue;
            String currName = parser.getAttributeName(i); // El nombre devuelto no contiene el namespace
            if (!name.equals(currName)) continue;
            String value = parser.getAttributeValue(i);
            return value;
        }
        return null;
    }

    protected static String getRootElementName(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG) // Nodo de texto etc
                continue;

            return parser.getName();
        }

        throw new ItsNatDroidException("INTERNAL ERROR: NO ROOT VIEW");
    }

    protected AttrParsed createAttribute(String namespaceURI,String name,String value)
    {
        return AttrParsed.create(namespaceURI, name, value);
    }

    protected void addAttribute(ElementParsed element,String namespaceURI,String name,String value,XMLParsed xmlParsed)
    {
        AttrParsed attrib = createAttribute(namespaceURI,name,value);
        element.addAttribute(attrib);
        if (attrib instanceof AttrParsedRemote)
        {
            xmlParsed.addAttributeRemote((AttrParsedRemote)attrib);
        }
    }

    protected abstract ElementParsed createRootElement(String name);

}
