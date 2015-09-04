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

package org.itsnat.impl.core.doc;

import java.io.Serializable;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.domimpl.ElementDocContainer;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLParamElement;

/**
 *
 * @author jmarranz
 */
public abstract class ElementDocContainerWrapperImpl implements Serializable
{
    protected ElementDocContainer elem;
    protected BoundElementDocContainerImpl bindInfo;

    public ElementDocContainerWrapperImpl(ElementDocContainer elem)
    {
        this.elem = elem;
    }

    public ElementDocContainer getElementDocContainer()
    {
        return elem;
    }

    public Element getElement()
    {
        return (Element)elem;
    }

    public BoundElementDocContainerImpl getBoundElementDocContainer()
    {
        return bindInfo;
    }

    public void setBoundElementDocContainer(BoundElementDocContainerImpl bindInfo)
    {
        this.bindInfo = bindInfo;
    }

    public Document getContentDocument()
    {
        if (bindInfo == null) return null;
        return bindInfo.getContentDocument();
    }

    public void setURL(String url,ClientDocumentStfulImpl clientDoc)
    {
        setURL(url);
    }

    public HTMLParamElement getHTMLParamElementWithSrc()
    {
        Element child = ItsNatTreeWalker.getFirstChildElement(getElement());
        while(child != null)
        {
            if (child instanceof HTMLParamElement)
            {
                HTMLParamElement param = (HTMLParamElement)child;
                if ("src".equals(param.getName()))
                    return param;
            }
            child = ItsNatTreeWalker.getNextSiblingElement(child);
        }
        return null;
    }

    public static ElementDocContainer getElementDocContainerIfURLAttr(Attr attr,Element elem)
    {
        ElementDocContainer elemDocCont = null;
        if (elem instanceof ElementDocContainer)
        {
            elemDocCont = (ElementDocContainer)elem;
        }
        else if (elem instanceof HTMLParamElement)
        {
            Node parentNode = elem.getParentNode();
            if (parentNode instanceof ElementDocContainer) // Lo normal es que sea un HTMLAppletElement o un HTMLObjectElement
                elemDocCont = (ElementDocContainer)parentNode;
        }

        if (elemDocCont == null) return null;
        if (elemDocCont.getElementDocContainerWrapper().isURLAttribute(attr))
            return elemDocCont;
        return null;
    }


    public abstract String getURL();
    public abstract void setURL(String url);

    public abstract String getURLAttrName();
    public abstract boolean isURLAttribute(Attr attr);
    public abstract boolean isJavaApplet();
}
