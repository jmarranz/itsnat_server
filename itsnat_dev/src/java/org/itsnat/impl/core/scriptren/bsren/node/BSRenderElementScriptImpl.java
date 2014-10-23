/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.scriptren.bsren.node;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.scriptren.shared.node.InsertAsMarkupInfoImpl;
import org.itsnat.impl.core.util.IOUtil;
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
        ServletContext servContext = getServletContext(clientDoc);
        return getScript((Element)newNode,servContext);
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
        ServletContext servContext = getServletContext(clientDoc);        
        return getScript((Element)newNode,servContext);
    }

    @Override
    protected String createElement(Element nodeElem, String tagName, ClientDocumentStfulDelegateImpl clientDoc)
    {
        throw new ItsNatException("INTERNAL ERROR"); // No se llega a llamar nunca
    }
        
    public static String getScript(Element nodeElem,ServletContext servContext)
    {   
        // El elemento <script> que estamos procesando se elimina en otro lugar inmediatamente después de ésto        
        String src = nodeElem.getAttribute("src");
        if (!"".equals(src))
        {
            String basePath = servContext.getRealPath("/");
            String filePath = basePath + src;
            
            File fileBasePath = new File(basePath);            
            File file = new File(filePath);
            boolean unexpected = false;
            try
            {
                unexpected = !file.getCanonicalPath().startsWith(fileBasePath.getCanonicalPath()); // Debemos evitar un intento de leer archivos fuera de la app web
            }
            catch (IOException ex) { throw new ItsNatException(ex); }

            if (unexpected) throw new ItsNatException("Unexpected security break attempt"); // Inexperado pues se supone que el path de <script> lo pone el programador
            
            return IOUtil.readTextFile(file,"UTF-8");
        }
        
        return DOMUtilInternal.getTextContent(nodeElem, true);
    }           
    
    private static ServletContext getServletContext(ClientDocumentStfulDelegateImpl clientDoc)
    {
        return clientDoc.getItsNatStfulDocument().getItsNatDocumentTemplateImpl().getItsNatServletImpl().getServlet().getServletConfig().getServletContext();                 
    }
}
