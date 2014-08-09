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

package org.itsnat.impl.core.scriptren.jsren.node;

import java.util.Iterator;
import java.util.LinkedList;
import org.itsnat.core.ItsNatDOMException;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.domimpl.AbstractViewImpl;
import org.itsnat.impl.core.scriptren.jsren.JSRenderImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.itsnat.impl.core.scriptren.jsren.event.JSRenderEventImpl;
import org.itsnat.impl.core.scriptren.shared.node.JSAndBSRenderNodeImpl;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.views.DocumentView;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderNodeImpl extends JSRenderImpl
{
    /**
     * Creates a new instance of JSRender
     */
    public JSRenderNodeImpl()
    {
    }

    public static JSRenderNodeImpl getJSRenderNode(Node node,ClientDocumentStfulDelegateWebImpl clientDoc)
    {       
        int nodeType = node.getNodeType();
        switch(nodeType)
        {
            // Está primero los más habituales (Element y Text nodes)
            case Node.ELEMENT_NODE:
                return JSRenderElementImpl.getJSRenderElement((Element)node,clientDoc);
            case Node.TEXT_NODE:
                return JSRenderTextImpl.getJSRenderText((Text)node,clientDoc);
            case Node.ATTRIBUTE_NODE:
                throw new ItsNatException("INTERNAL ERROR");
                //return attrRender;
            case Node.CDATA_SECTION_NODE:
                return JSRenderCDATASectionImpl.SINGLETON;
            case Node.COMMENT_NODE:
                return JSRenderCommentImpl.getJSRenderComment((Comment)node,clientDoc);
            case Node.DOCUMENT_FRAGMENT_NODE:
                return JSRenderDocumentFragmentImpl.SINGLETON;
            case Node.ENTITY_REFERENCE_NODE:
                return JSRenderEntityReferenceImpl.SINGLETON;
            case Node.PROCESSING_INSTRUCTION_NODE:
                return JSRenderProcessingInstructionImpl.SINGLETON;
            case Node.DOCUMENT_NODE:
                throw new ItsNatDOMException("Unexpected Document node",node);
            case Node.DOCUMENT_TYPE_NODE:
                throw new ItsNatDOMException("Unexpected DocumentType node",node);
            case Node.ENTITY_NODE:
                throw new ItsNatDOMException("Unexpected Entity node",node);
            case Node.NOTATION_NODE:
                throw new ItsNatDOMException("Unexpected Notation node",node);
            case AbstractViewImpl.ABSTRACT_VIEW:
                return JSRenderAbstractViewImpl.SINGLETON;
        }

        throw new ItsNatDOMException("Internal error",node);
    }

    public static String addNodeToCache(NodeLocationImpl nodeLoc)
    {
        return JSAndBSRenderNodeImpl.addNodeToCache(nodeLoc);
    }

    public static String removeNodeFromCache(String id)
    {
        return "itsNatDoc.removeNodeCache([" + toLiteralStringJS(id) + "]);\n";
    }

    public static String removeNodeFromCache(LinkedList<String> idList)
    {
        StringBuilder code = new StringBuilder();
        for(Iterator<String> it = idList.iterator(); it.hasNext(); )
        {
            String id = toLiteralStringJS(it.next());
            if (code.length() > 0) code.append("," + id);
            else code.append(id);
        }
        return "itsNatDoc.removeNodeCache([" + code.toString() + "]);\n";
    }

    public static String getNodeReference(Node node,boolean cacheIfPossible,boolean errIfNull,ClientDocumentStfulDelegateImpl clientDoc)
    {
        // El código devuelto debe ser enviado al cliente y ejecutado pues puede llevar información de cacheado y haber sido cacheado ahora en el servidor
        if (node == null)
            if (errIfNull) throw new ItsNatException("No specified node");
            else return "null";

        ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();
        Document doc = itsNatDoc.getDocument();
        if (node == ((DocumentView)doc).getDefaultView())
            return "itsNatDoc.win";
        else if (node == doc)
            return "itsNatDoc.doc";
        else if (node == doc.getDoctype())
            return "itsNatDoc.doc.doctype";
        else if (node == doc.getDocumentElement())
            return "itsNatDoc.doc.documentElement";

        // Es una tentación considerar también el caso document.body pero creo recordar
        // que en Safari y XHTML el API DOM no es el HTML DOM sino DOM Core a secas.

        // node no puede ser nulo, ya está comprobado antes
        NodeLocationImpl nodeLoc = clientDoc.getNodeLocation(node,cacheIfPossible);
        return getNodeReference(nodeLoc,errIfNull); // errIfNull puede ser false si se quiere, es redundante pues ya se chequeó antes
    }

    public static String getNodeReference(NodeLocationImpl nodeLoc,boolean errIfNull)
    {
        String nodeRef = JSAndBSRenderNodeImpl.getNodeLocation(nodeLoc, errIfNull);
        return "itsNatDoc.getNode(" + nodeRef + ")";        
    }

    public static String getSetNodePropertyCode(Node node,String propertyName,String value,boolean cacheIfPossible,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return clientDoc.getNodeReference(node,cacheIfPossible,true) + "." + propertyName + "=" + toTransportableStringLiteral(value,clientDoc.getBrowserWeb()) + ";\n"; // El \n es un embellecedor
    }

    public static String getGetNodePropertyCode(Node object,String propertyName,boolean cacheIfPossible,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return clientDoc.getNodeReference(object,cacheIfPossible,true) + "." + propertyName;
    }

    public static String getCodeDispatchEvent(EventTarget node,Event evt,String varResName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        JSRenderEventImpl evtRender = JSRenderEventImpl.getJSEventRender(evt,clientDoc.getBrowserWeb());
        NodeLocationImpl nodeLoc = clientDoc.getNodeLocation((Node)node,true);

        return evtRender.getDispatchEvent(varResName,nodeLoc,evt,clientDoc);
    }
}
