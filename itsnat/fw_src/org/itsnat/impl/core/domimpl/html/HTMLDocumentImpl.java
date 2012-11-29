/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2011 Jose Maria Arranz Santamaria, Spanish citizen

  This software is free software; you can redistribute it and/or modify it
  under the terms of the GNU Lesser General Public License as
  published by the Free Software Foundation; either version 3 of
  the License, or (at your option) any later version.
  This software is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details. You should have received
  a copy of the GNU Lesser General Public License along with this program.
  If not, see <http://www.gnu.org/licenses/>.
*/

package org.itsnat.impl.core.domimpl.html;

import org.itsnat.impl.core.domimpl.*;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLBodyElement;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFrameSetElement;
import org.w3c.dom.html.HTMLHeadElement;
import org.w3c.dom.html.HTMLTitleElement;

/**
 *
 * @author jmarranz
 */
public class HTMLDocumentImpl extends DocumentImpl implements HTMLDocument
{
    protected HTMLCollection anchors;
    protected HTMLCollection applets;
    protected HTMLCollection forms;
    protected HTMLCollection images;
    protected HTMLCollection links;

    public HTMLDocumentImpl()
    {
        // Es necesario este constructor vacío porque los parsers necesitan crear
        // el documento via reflection
    }

    public HTMLDocumentImpl(DocumentType dt,DOMImplementation impl)
    {
        super(dt, impl);
    }

    public Element createElementInternal(String localName)
    {
        return createHTMLElement(null,localName); // También creamos un elemento HTML
    }

    protected Node newNode()
    {
        return new HTMLDocumentImpl(null,getImplementation());
    }

    public void close()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void open()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getCookie()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getDomain()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getReferrer()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public HTMLHeadElement getHead()
    {
        return DOMUtilHTML.getHTMLHead(this);
    }

    public HTMLTitleElement getTitleElement()
    {
        HTMLHeadElement head = (HTMLHeadElement)getHead();
        if (head == null) return null;
        Node title = head.getFirstChild();
        while ( (title != null) && !(title instanceof HTMLTitleElement) )
            title = title.getNextSibling();
        return (HTMLTitleElement)title; // Puede ser nulo
    }

    public String getTitle()
    {
        HTMLTitleElement title = getTitleElement();
        if (title == null) return "";
        return title.getText();
    }

    public String getURL()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setCookie(String cookie)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setTitle(String title)
    {
        HTMLTitleElement titleElem = getTitleElement();
        if (titleElem == null) throw new DOMException(DOMException.NOT_FOUND_ERR,"<title> element is missing");
        titleElem.setText(title);
    }

    public void write(String text)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void writeln(String text)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public HTMLCollection getAnchors()
    {
        if (anchors == null)
            this.anchors = new HTMLCollectionImpl(getDocumentElement(),HTMLCollectionImpl.ANCHOR);
        return anchors;
    }

    public HTMLCollection getApplets()
    {
        if (applets == null)
            this.applets = new HTMLCollectionImpl(getDocumentElement(),HTMLCollectionImpl.APPLET);
        return applets;
    }

    public HTMLCollection getForms()
    {
        if (forms == null)
            this.forms = new HTMLCollectionImpl(getDocumentElement(),HTMLCollectionImpl.FORM);
        return forms;
    }

    public HTMLCollection getImages()
    {
        if (images == null)
            this.images = new HTMLCollectionImpl(getDocumentElement(),HTMLCollectionImpl.IMAGE);
        return images;
    }

    public HTMLCollection getLinks()
    {
        if (links == null)
            this.links = new HTMLCollectionImpl(getDocumentElement(),HTMLCollectionImpl.LINK);
        return links;
    }

    public HTMLElement getBody()
    {
        Element html = getDocumentElement();
        if (html == null) return null;
        Node body = html.getFirstChild();
        while ( (body != null) &&
                !(body instanceof HTMLBodyElement) &&
                !(body instanceof HTMLFrameSetElement))
            body = body.getNextSibling();
        return (HTMLElement)body; // Puede ser null
    }

    public void setBody(HTMLElement body)
    {
        Element html = getDocumentElement();
        if (html == null) throw new DOMException(DOMException.NOT_FOUND_ERR,"<html> element is not defined");
        Element currBody = ItsNatTreeWalker.getLastChildElement(html);
        if ((currBody instanceof HTMLBodyElement) || (currBody instanceof HTMLFrameSetElement))
            currBody.getParentNode().replaceChild(body,currBody);
        else // Será el <head>
            html.appendChild(body);
    }

    public NodeList getElementsByName(String elementName)
    {
        return new NodeListByNameImpl(this,elementName);
    }
}
