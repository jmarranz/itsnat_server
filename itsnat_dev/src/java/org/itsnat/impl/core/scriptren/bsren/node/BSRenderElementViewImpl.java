/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.scriptren.bsren.node;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import static org.itsnat.impl.core.scriptren.bsren.node.BSRenderNodeImpl.shortNamespaceURI;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class BSRenderElementViewImpl extends BSRenderElementImpl
{
    public static final BSRenderElementViewImpl SINGLETON = new BSRenderElementViewImpl();    
    
    protected String createElement(Element nodeElem,String tagName,ClientDocumentStfulDelegateImpl clientDoc)
    {       
        String namespaceURI = nodeElem.getNamespaceURI();
        if (namespaceURI != null)
        {
            String namespaceURIScript = shortNamespaceURI(namespaceURI);            
            return "itsNatDoc.createElementNS(" + namespaceURIScript + ",\"" + tagName + "\")";
        }
        else
            return "itsNatDoc.createElement(\"" + tagName + "\")";      
    }    
}
