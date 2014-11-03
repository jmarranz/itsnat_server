package org.itsnat.droid.impl.parser;

import android.util.Xml;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.model.AttrParsed;
import org.itsnat.droid.impl.model.AttrParsedDefault;
import org.itsnat.droid.impl.model.AttrParsedRemote;
import org.itsnat.droid.impl.model.ElementParsed;
import org.itsnat.droid.impl.model.XMLParsed;
import org.itsnat.droid.impl.util.ValueUtil;
import org.itsnat.droid.impl.xmlinflater.XMLLayoutInflateService;
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
        ElementParsed rootElement = createRootElementAndFillAttributes(rootElemName, parser,xmlParsed);

        processChildElements(rootElement,parser,xmlParsed);

        return rootElement;
    }

    protected ElementParsed createRootElementAndFillAttributes(String name,XmlPullParser parser,XMLParsed xmlParsed)
    {
        ElementParsed rootElement = createRootElement(name);

        setRootElement(rootElement,xmlParsed); // Cuanto antes

        fillAttributesAndAddElement(null, rootElement,parser);

        return rootElement;
    }

    protected ElementParsed createElementAndFillAttributesAndAdd(String name, ElementParsed parentElement, XmlPullParser parser)
    {
        // parentElement es null en el caso de parseo de fragment
        ElementParsed element = createRootElement(name);

        fillAttributesAndAddElement(parentElement, element,parser);

        return element;
    }

    protected void fillAttributesAndAddElement(ElementParsed parentElement, ElementParsed element,XmlPullParser parser)
    {
        fillElementAttributes(element,parser);
        if (parentElement != null) parentElement.addChild(element);
    }

    protected void fillElementAttributes(ElementParsed element,XmlPullParser parser)
    {
        int len = parser.getAttributeCount();
        element.initAttribList(len);
        for (int i = 0; i < len; i++)
        {
            String namespaceURI = parser.getAttributeNamespace(i);
            if ("".equals(namespaceURI)) namespaceURI = null; // Por estandarizar
            String name = parser.getAttributeName(i); // El nombre devuelto no contiene el namespace
            String value = parser.getAttributeValue(i);
            addAttribute(element, namespaceURI, name, value);
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
        ElementParsed element = createElementAndFillAttributesAndAdd(name, parentElement, parser);
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
        if (XMLLayoutInflateService.XMLNS_ANDROID.equals(namespaceURI) && value.startsWith("@remote:"))
            return new AttrParsedRemote(namespaceURI,name,value);
        else
            return new AttrParsedDefault(namespaceURI,name,value);
    }

    protected void addAttribute(ElementParsed element,String namespaceURI,String name,String value)
    {
        AttrParsed attrib = createAttribute(namespaceURI,name,value);
        element.addAttribute(attrib);
    }

    protected abstract ElementParsed createRootElement(String name);

}
