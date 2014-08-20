/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.scriptren.bsren.node;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.scriptren.shared.node.InsertAsMarkupInfoImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Los métodos implementados (salvo createElement) son los métodos "raiz" que se llaman tras obtener este objeto via BSRenderNodeImpl.getBSRenderNode(Node) 
 * siendo el parámetro el elemento <script>
 * 
 * @author jmarranz
 */
public class BSRenderElementScriptImpl extends BSRenderElementImpl
{
    public static final BSRenderElementScriptImpl SINGLETON = new BSRenderElementScriptImpl();    
  
    
    @Override
    public Object getInsertNewNodeCode(Node newNode,ClientDocumentStfulDelegateDroidImpl clientDoc)    
    {
        return getScript((Element)newNode);
    }
    
    @Override
    public String getRemoveNodeCode(Node removedNode,ClientDocumentStfulDelegateDroidImpl clientDoc)    
    {
        return ""; // Como en el cliente no hay realmente nodo, no hay nada que eliminar
    }
            
    @Override
    public String getRemoveAllChildCode(Node parentNode,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return ""; // Como en el cliente no hay realmente nodo, no hay nada que eliminar
    }
    
    @Override
    public Object getAppendNewNodeCode(Node parent,Node newNode,String parentVarName,InsertAsMarkupInfoImpl insertMarkupInfo,ClientDocumentStfulDelegateImpl clientDoc)    
    {
        return getScript((Element)newNode);
    }

    @Override
    protected String createElement(Element nodeElem, String tagName, ClientDocumentStfulDelegateImpl clientDoc)
    {
        throw new ItsNatException("INTERNAL ERROR"); // No se llega a llamar nunca
    }
        
    private String getScript(Element nodeElem)
    {       
        // El elemento <script> que estamos procesando se elimina en otro lugar inmediatamente después de ésto
        return DOMUtilInternal.getTextContent(nodeElem, true);
    }           
}
