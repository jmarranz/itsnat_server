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

package org.itsnat.impl.core.domimpl;

import org.apache.batik.dom.AbstractNode;
import org.itsnat.impl.core.domimpl.otherns.OtherNSDocumentImpl;
import org.itsnat.impl.core.domimpl.html.HTMLDocumentImpl;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.dom.GenericDocumentType;
import org.apache.batik.dom.events.EventSupport;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

/**
 *
 * @author jmarranz
 */
public class BatikItsNatDOMImplementationImpl extends GenericDOMImplementation
{
    public static final BatikItsNatDOMImplementationImpl SINGLETON = new BatikItsNatDOMImplementationImpl();

    public BatikItsNatDOMImplementationImpl()
    {
    }

    public static final BatikItsNatDOMImplementationImpl getBatikItsNatDOMImplementation()
    {
        return SINGLETON;
    }

    @Override
    public Document createDocument(String namespaceURI,
                                   String qualifiedName,
                                   DocumentType doctype) throws DOMException
    {
        Document doc;
        if (isHTMLorXHTMLDocument(namespaceURI,qualifiedName))
        {
            doc = new HTMLDocumentImpl(doctype, this);
            doc.appendChild(doc.createElement(qualifiedName)); // Da igual usar createElementNS
        }
        else
        {
            doc = new OtherNSDocumentImpl(doctype, this);
            doc.appendChild(doc.createElementNS(namespaceURI,qualifiedName));
        }
        return doc;
    }

    public static boolean isHTMLorXHTMLDocument(String namespaceURI,String qualifiedName)
    {
        if (namespaceURI == null)
            return qualifiedName.toLowerCase().equals("html");
        else
            return NamespaceUtil.isXHTMLNamespace(namespaceURI);
    }

    @Override
    public DocumentType createDocumentType(String qualifiedName,
                                           String publicId,
                                           String systemId)
    {
        if ((qualifiedName != null) && qualifiedName.toLowerCase().equals("html"))
            qualifiedName = "html"; // Evita que se use en mayúsculas
        return new GenericDocumentType(qualifiedName,publicId,systemId);
    }

    @Override
    public EventSupport createEventSupport(AbstractNode n)
    {
        return new EventSupportItsNatFixed(n);
    }
}
