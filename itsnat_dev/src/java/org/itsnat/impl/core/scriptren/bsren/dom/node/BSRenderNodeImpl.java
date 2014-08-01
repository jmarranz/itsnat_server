/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2014 Jose Maria Arranz Santamaria, Spanish citizen

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

package org.itsnat.impl.core.scriptren.bsren.dom.node;

import org.itsnat.impl.core.scriptren.bsren.BSRenderImpl;
import java.util.Iterator;
import java.util.LinkedList;
import org.itsnat.core.ItsNatDOMException;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.domimpl.AbstractViewImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.scriptren.bsren.dom.event.BSRenderEventImpl;
import static org.itsnat.impl.core.scriptren.jsren.JSRenderImpl.toLiteralStringJS;
import org.itsnat.impl.core.scriptren.shared.dom.node.JSAndBSRenderNodeImpl;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class BSRenderNodeImpl extends BSRenderImpl
{
    public static BSRenderNodeImpl getBSRenderNode(Node node)
    {
        int nodeType = node.getNodeType();
        switch(nodeType)
        {
            // Está primero los más habituales (Element y Text nodes)
            case Node.ELEMENT_NODE:
                return BSRenderElementImpl.getBSRenderElement();
            case Node.TEXT_NODE:
                return BSRenderTextImpl.getBSRenderText();            
            case Node.ATTRIBUTE_NODE:
                throw new ItsNatException("INTERNAL ERROR");
                //return attrRender;
            case Node.CDATA_SECTION_NODE:
                throw new ItsNatDOMException("Unexpected CDATA section node",node);
            case Node.COMMENT_NODE:
                throw new ItsNatDOMException("Unexpected comment node",node);                
            case Node.DOCUMENT_FRAGMENT_NODE:
                throw new ItsNatDOMException("Unexpected document fragment node",node);
            case Node.ENTITY_REFERENCE_NODE:
                throw new ItsNatDOMException("Unexpected entity reference node",node);
            case Node.PROCESSING_INSTRUCTION_NODE:
                throw new ItsNatDOMException("Unexpected processing instruction node",node);
            case Node.DOCUMENT_NODE:
                throw new ItsNatDOMException("Unexpected Document node",node);
            case Node.DOCUMENT_TYPE_NODE:
                throw new ItsNatDOMException("Unexpected DocumentType node",node);
            case Node.ENTITY_NODE:
                throw new ItsNatDOMException("Unexpected Entity node",node);
            case Node.NOTATION_NODE:
                throw new ItsNatDOMException("Unexpected Notation node",node);
            case AbstractViewImpl.ABSTRACT_VIEW:
                throw new ItsNatDOMException("Unexpected window use",node);
        }

        throw new ItsNatDOMException("Internal error",node);
    }
    
    public static String removeNodeFromCache(LinkedList<String> idList)
    {
        StringBuilder code = new StringBuilder();
        for(Iterator<String> it = idList.iterator(); it.hasNext(); )
        {
            String id = toLiteralStringBS(it.next());
            if (code.length() > 0) code.append("," + id);
            else code.append(id);
        }
        return "itsNatDoc.removeNodeCache(new String[]{" + code.toString() + "});\n";
    }    
    
    public static String shortNamespaceURI(String namespaceURI)
    {
        // En vez de poner: http://schemas.android.com/apk/res/android, ponemos NSAND que es una cte Java que en la parte Android tiene el valor del namespace
        if (NamespaceUtil.isAndroidNamespace(namespaceURI))
            return "NSAND";
        else return "\"" + namespaceURI + "\"";
    }    
    
    public static String getNodeReference(Node node,boolean cacheIfPossible,boolean errIfNull,ClientDocumentStfulDelegateImpl clientDoc)
    {
        // El código devuelto debe ser enviado al cliente y ejecutado pues puede llevar información de cacheado y haber sido cacheado ahora en el servidor
        if (node == null)
            if (errIfNull) throw new ItsNatException("No specified node");
            else return "null";

        NodeLocationImpl nodeLoc = clientDoc.getNodeLocation(node,cacheIfPossible);
        return getNodeReference(nodeLoc,errIfNull); // errIfNull puede ser false si se quiere, es redundante pues ya se chequeó antes
    }    
    
    public static String getNodeReference(NodeLocationImpl nodeLoc,boolean errIfNull)
    {
        String nodeRef = JSAndBSRenderNodeImpl.getNodeLocation(nodeLoc, errIfNull);
        return "itsNatDoc.getView(" + nodeRef + ")";        
    }    
    
    public static String addNodeToCache(NodeLocationImpl nodeLoc)
    {
        return "itsNatDoc.addNodeCache(" + nodeLoc.toScriptNodeLocation(true) + ");\n";
    }

    public static String removeNodeFromCache(String id)
    {
        return "itsNatDoc.removeNodeCache(new String[]{" + toLiteralStringJS(id) + "});\n";
    }    
    
    public static String getCodeDispatchEvent(EventTarget node,Event evt,String varResName,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        BSRenderEventImpl evtRender = BSRenderEventImpl.getBSEventRender(evt);
        NodeLocationImpl nodeLoc = clientDoc.getNodeLocation((Node)node,true);

        return evtRender.getDispatchEvent(varResName,nodeLoc,evt,clientDoc);
    }    
}
