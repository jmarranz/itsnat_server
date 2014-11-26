package org.itsnat.droid.impl.domparser;

import android.content.res.AssetManager;
import android.util.Xml;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.dom.DOMAttrAsset;
import org.itsnat.droid.impl.dom.DOMAttrDynamic;
import org.itsnat.droid.impl.dom.DOMAttrRemote;
import org.itsnat.droid.impl.dom.DOMElement;
import org.itsnat.droid.impl.dom.XMLDOM;
import org.itsnat.droid.impl.util.IOUtil;
import org.itsnat.droid.impl.util.MimeUtil;
import org.itsnat.droid.impl.util.ValueUtil;
import org.itsnat.droid.impl.xmlinflater.XMLInflateRegistry;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.LinkedList;

/**
 * Created by jmarranz on 31/10/14.
 */
public abstract class XMLDOMParser
{
    protected XMLInflateRegistry xmlInflateRegistry;
    protected AssetManager assetManager;

    public XMLDOMParser(XMLInflateRegistry xmlInflateRegistry,AssetManager assetManager)
    {
        this.xmlInflateRegistry = xmlInflateRegistry;
        this.assetManager = assetManager;
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

    protected void setRootElement(DOMElement rootElement,XMLDOM xmlDOM)
    {
        xmlDOM.setRootElement(rootElement);
    }

    public DOMElement parseRootElement(String rootElemName,XmlPullParser parser,XMLDOM xmlDOM) throws IOException, XmlPullParserException
    {
        int nsStart = parser.getNamespaceCount(parser.getDepth()-1);
        int nsEnd = parser.getNamespaceCount(parser.getDepth());
        for (int i = nsStart; i < nsEnd; i++)
        {
            String prefix = parser.getNamespacePrefix(i);
            String ns = parser.getNamespaceUri(i);
            xmlDOM.addNamespace(prefix, ns);
        }

        if (xmlDOM.getAndroidNSPrefix() == null)
            throw new ItsNatDroidException("Missing android namespace declaration in root element");


        DOMElement rootElement = createRootElementAndFillAttributes(rootElemName, parser, xmlDOM);

        processChildElements(rootElement,parser, xmlDOM);

        return rootElement;
    }

    protected DOMElement createRootElementAndFillAttributes(String name,XmlPullParser parser,XMLDOM xmlDOM)
    {
        DOMElement rootElement = createRootElement(name);

        setRootElement(rootElement, xmlDOM); // Cuanto antes

        fillAttributesAndAddElement(null, rootElement,parser, xmlDOM);

        return rootElement;
    }

    protected DOMElement createElementAndFillAttributesAndAdd(String name, DOMElement parentElement, XmlPullParser parser,XMLDOM xmlDOM)
    {
        // parentElement es null en el caso de parseo de fragment
        DOMElement element = createRootElement(name);

        fillAttributesAndAddElement(parentElement, element,parser, xmlDOM);

        return element;
    }

    protected void fillAttributesAndAddElement(DOMElement parentElement, DOMElement element,XmlPullParser parser,XMLDOM xmlDOM)
    {
        fillElementAttributes(element,parser, xmlDOM);
        if (parentElement != null) parentElement.addChildDOMElement(element);
    }

    protected void fillElementAttributes(DOMElement element,XmlPullParser parser,XMLDOM xmlDOM)
    {
        int len = parser.getAttributeCount();
        element.initDOMAttribList(len);
        for (int i = 0; i < len; i++)
        {
            String namespaceURI = parser.getAttributeNamespace(i);
            if ("".equals(namespaceURI)) namespaceURI = null; // Por estandarizar
            String name = parser.getAttributeName(i); // El nombre devuelto no contiene el namespace
            String value = parser.getAttributeValue(i);
            addDOMAttr(element, namespaceURI, name, value, xmlDOM);
        }
    }

    protected void processChildElements(DOMElement parentElement,XmlPullParser parser,XMLDOM xmlDOM) throws IOException, XmlPullParserException
    {
        DOMElement childView = parseNextChild(parentElement,parser, xmlDOM);
        while (childView != null)
        {
            childView = parseNextChild(parentElement,parser, xmlDOM);
        }
    }

    private DOMElement parseNextChild(DOMElement parentElement,XmlPullParser parser,XMLDOM xmlDOM) throws IOException, XmlPullParserException
    {
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG) // Nodo de texto etc
                continue;

            String name = parser.getName(); // viewName lo normal es que sea un nombre corto por ej RelativeLayout

            DOMElement element = processElement(name, parentElement, parser, xmlDOM);
            if (element == null) continue; // Se ignora
            return element;
        }
        return null;
    }

    protected DOMElement processElement(String name, DOMElement parentElement, XmlPullParser parser,XMLDOM xmlDOM) throws IOException, XmlPullParserException
    {
        DOMElement element = createElementAndFillAttributesAndAdd(name, parentElement, parser, xmlDOM);
        processChildElements(element,parser, xmlDOM);
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


    protected void addDOMAttr(DOMElement element, String namespaceURI, String name, String value, XMLDOM xmlDOMParent)
    {
        DOMAttr attrib = DOMAttr.create(namespaceURI, name, value);
        element.addDOMAttribute(attrib);

        if (attrib instanceof DOMAttrRemote)
        {
            xmlDOMParent.addDOMAttrRemote((DOMAttrRemote) attrib);
        }
        else if (attrib instanceof DOMAttrAsset)
        {
            DOMAttrAsset assetAttr = (DOMAttrAsset)attrib;

            String location = assetAttr.getLocation();
            InputStream ims = null;
            byte[] res;
            try
            {
                // AssetManager.open es multihilo
                // http://www.netmite.com/android/mydroid/frameworks/base/libs/utils/AssetManager.cpp
                ims = assetManager.open(location);
                res = IOUtil.read(ims);
            }
            catch (IOException ex)
            {
                throw new ItsNatDroidException(ex);
            }
            finally
            {
                if (ims != null) try { ims.close(); } catch (IOException ex) { throw new ItsNatDroidException(ex); }
            }

            String resourceMime = assetAttr.getResourceMime();
            if (MimeUtil.MIME_XML.equals(resourceMime))
            {
                String markup = ValueUtil.toString(res,"UTF-8");

                XMLDOM xmlDOMChild = processDOMAttrDynamicXML(assetAttr,markup,xmlInflateRegistry,assetManager);

                LinkedList<DOMAttrRemote> attrRemoteList = xmlDOMChild.getDOMAttrRemoteList();
                if (attrRemoteList != null)
                    throw new ItsNatDroidException("Remote resources cannot be specified by a resource loaded as asset");
            }
            else if (MimeUtil.isMIMEImage(resourceMime))
            {
                assetAttr.setResource(res);
            }
            else throw new ItsNatDroidException("Unsupported resource mime: " + resourceMime);
        }
    }

    public static XMLDOM processDOMAttrDynamicXML(DOMAttrDynamic attr,String markup,XMLInflateRegistry xmlInflateRegistry,AssetManager assetManager)
    {
        String resourceType = attr.getResourceType();

        // Por ahora sólo drawable
        XMLDOM xmlDOM;
        if ("drawable".equals(resourceType))
        {
            xmlDOM = xmlInflateRegistry.getXMLDOMDrawableCache(markup, assetManager); // Es multihilo el método
        }
        else throw new ItsNatDroidException("Unsupported resource type as remote: " + resourceType);

        attr.setResource(xmlDOM);

        return xmlDOM;
    }

    protected abstract DOMElement createRootElement(String name);

}
