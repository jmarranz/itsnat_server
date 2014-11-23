package org.itsnat.droid.impl.domparser.layout;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.dom.DOMElement;
import org.itsnat.droid.impl.dom.XMLDOM;
import org.itsnat.droid.impl.dom.layout.XMLDOMLayout;
import org.itsnat.droid.impl.dom.layout.DOMView;
import org.itsnat.droid.impl.domparser.XMLDOMParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;


/**
 * Created by jmarranz on 27/10/14.
 */
public abstract class XMLDOMLayoutParser extends XMLDOMParser
{
    public XMLDOMLayout parse(String markup)
    {
        StringReader input = new StringReader(markup);
        return parse(input);
    }

    private XMLDOMLayout parse(Reader input)
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

    private XMLDOMLayout parse(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        String rootElemName = getRootElementName(parser);
        XMLDOMLayout domLayout = new XMLDOMLayout();
        parseRootElement(rootElemName,parser,domLayout);
        return domLayout;
    }

    @Override
    protected DOMElement createRootElement(String name)
    {
        return new DOMView(name,null);
    }


    @Override
    protected DOMElement processElement(String name, DOMElement parentElement, XmlPullParser parser,XMLDOM xmlDOM) throws IOException, XmlPullParserException
    {
        if (name.equals("script"))
        {
            parseScriptElement(parser,(DOMView)parentElement, xmlDOM);
            return null; // Ignorar porque "desaparece"
        }
        else return super.processElement(name,parentElement,parser, xmlDOM);
    }

    @Override
    protected void addDOMAttr(DOMElement element, String namespaceURI, String name, String value, XMLDOM xmlDOM)
    {
        DOMView domView = (DOMView)element;
        if (domView.getStyleAttr() == null && (namespaceURI == null) && "style".equals(name))
            domView.setStyleAttr(value);
        else
            super.addDOMAttr(element, namespaceURI, name, value, xmlDOM);
    }

    protected abstract void parseScriptElement(XmlPullParser parser,DOMView viewParent,XMLDOM xmlDOM) throws IOException, XmlPullParserException;
}
