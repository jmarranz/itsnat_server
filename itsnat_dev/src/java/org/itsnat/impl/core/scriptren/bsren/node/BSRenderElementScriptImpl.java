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

package org.itsnat.impl.core.scriptren.bsren.node;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.domutil.NamespaceUtil;
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
        return getScript((Element)newNode,clientDoc);
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
        return getScript((Element)newNode,clientDoc);
    }

    @Override
    protected String createElement(Element nodeElem, String tagName, ClientDocumentStfulDelegateImpl clientDoc)
    {
        throw new ItsNatException("INTERNAL ERROR"); // No se llega a llamar nunca
    }
        
    public static String getScript(Element nodeElem,ClientDocumentStfulDelegateImpl clientDoc)
    {   
        // El elemento <script> que estamos procesando se elimina en otro lugar inmediatamente después de ésto        
        String src = nodeElem.getAttribute("src");
        if (!"".equals(src))
        {
            /* Ya no lo necesitamos pues lo hacemos en Android que es lo normal de un navegador
            URI uri = null;
            try { uri = new URI(src); }
            catch (URISyntaxException ex) { throw new ItsNatException(ex); }

            String scheme = uri.getScheme();            
            
            if (scheme == null)
            {       
                String docReqURL = clientDoc.getItsNatStfulDocument().getRequestURL();                      

                URL docURL = null;
                try { docURL = new URL(docReqURL); }
                catch (MalformedURLException ex) { throw new ItsNatException(ex); }

                StringBuilder baseURL = new StringBuilder(); 
                baseURL.append(docURL.getProtocol());
                baseURL.append(":");
                baseURL.append("//");
                baseURL.append(docURL.getAuthority());                

                ServletContext servContext = getServletContext(clientDoc);             
                //baseURL.append('/');                
                baseURL.append(servContext.getContextPath());                
                baseURL.append('/'); // Si el src ya empezara en / no pasa nada porque se repita                
                
                src = baseURL.toString() + src;
            }
            else if (!scheme.equals("http") && !scheme.equals("https")) 
                    throw new ItsNatException("Scheme not supported: " + scheme);             
            */                      
                    
            return "itsNatDoc.downloadScript(\"" + src + "\");";
        }
        
        return DOMUtilInternal.getTextContent(nodeElem, true);
    }           
    
    private static ServletContext getServletContext(ClientDocumentStfulDelegateImpl clientDoc)
    {
        return clientDoc.getItsNatStfulDocument().getItsNatDocumentTemplateImpl().getItsNatServletImpl().getServlet().getServletConfig().getServletContext();                 
    }
}
