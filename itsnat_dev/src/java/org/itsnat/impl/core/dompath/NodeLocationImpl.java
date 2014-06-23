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
package org.itsnat.impl.core.dompath;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.browser.web.BrowserWeb;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.NodeCacheRegistryImpl;
import org.itsnat.impl.core.scriptren.shared.JSAndBSRenderSharedUtil;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class NodeLocationImpl 
{
    protected ClientDocumentStfulDelegateImpl clientDoc;
    protected boolean used = false;
    
    public NodeLocationImpl(ClientDocumentStfulDelegateImpl clientDoc)
    {
        this.clientDoc = clientDoc;
    }    
   
    public boolean isScriptJS()
    {
        return clientDoc.getClientDocumentStful().getBrowser() instanceof BrowserWeb;
    }
    
    public boolean isScriptBeanshell()
    {
        return !isScriptJS();
    }    
    
    public abstract Node getNode();

    public ClientDocumentStfulDelegateImpl getClientDocumentStfulDelegate()
    {
        return clientDoc;
    }

    protected void setUsed()    
    {
        this.used = true;
    }
    
    protected String toScriptArray(String content)
    {
        StringBuilder code = new StringBuilder();
        boolean isJS = isScriptJS();
        if (isJS)
            code.append( "[" );
        else 
            code.append( "arr(" );    

        code.append( content );    
        
        if (isJS)
            code.append( "]" );
        else 
            code.append( ")" );
        
        return code.toString();
    }
   
    
    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();

        if (!used)
        {
            // Esto ayuda al programador final cuando genera código JavaScript y no envia al cliente,
            // pero también puede ayudar en el desarrollo del framework, pues si se detectara
            // esta excepción el paso siguiente podría ser guardar un Exception creado en los constructores
            // para recordar cual fue el contexto en el que se creó.
            throw new ItsNatException("Some nodes have been cached in server but not in client, generated JavaScript code was not sent to the client.");
            //ex.printStackTrace();
            //throw ex;
        }
    }    
     
    protected static boolean isNull(String str)
    {
        return ((str == null) || str.equals("null"));
    }        
    
    public abstract boolean isJustCached();    
    
    public abstract String toScriptNodeLocation(boolean errIfNodeNull);    
    
    public static String toLiteralStringScript(String value)
    {
        return JSAndBSRenderSharedUtil.toLiteralStringScript(value);
    }    
    
    
    public static NodeLocationImpl getNodeLocation(ClientDocumentStfulDelegateImpl clientDoc,Node node,boolean cacheIfPossible)
    {
        if (node == null) return new NodeLocationNullImpl(clientDoc);

        return NodeLocationWithParentImpl.getNodeLocationWithParent(node,cacheIfPossible,clientDoc);
    }

    public static NodeLocationImpl getRefNodeLocationInsertBefore(ClientDocumentStfulDelegateImpl clientDoc,Node newNode,Node nextSibling)
    {
        // El NodeLocationImpl a obtener ¡¡¡es el de nextSibling!!!
        if (nextSibling == null) return new NodeLocationNullImpl(clientDoc);

        String idRefChild = null;
        String refChildPathRel = null;
        boolean needRefNodePath = false;

        NodeCacheRegistryImpl nodeCache = clientDoc.getNodeCacheRegistry();
        if (nodeCache != null)
        {
            idRefChild = nodeCache.getId(nextSibling);
            if (idRefChild != null) // Está en la caché
            {
                needRefNodePath = false; // está cacheado, no necesitamos calcular el path
            }
            else
            {
                // No cacheado,intentamos cachear ahora, necesitamos calcular el path para que se cachee en el cliente también
                needRefNodePath = true;
                idRefChild = nodeCache.addNode(nextSibling); // el caso null es que no es cacheable por ejemplo, o la caché está "bloqueada"
            }
        }
        else needRefNodePath = true;

        if (needRefNodePath)
        {
            // Necesitamos calcular el path

            // En el navegador todavía no se ha insertado por lo que el path de newNode
            // es precisamente el path del nodo de referencia "desplazado" por el nuevo nodo
            // no es el path de nextSibling.
            // En el caso de que el nextSibling sea un Text node falta el sufijo
            refChildPathRel = clientDoc.getRelativeStringPathFromNodeParent(newNode);
            refChildPathRel = DOMPathResolver.removeTextNodeSuffix(refChildPathRel); // Pues nos interesa el path sólo pues este no es el verdadero nodo, es nextSibling
            if (nextSibling.getNodeType() == Node.TEXT_NODE)
                refChildPathRel += DOMPathResolver.getTextNodeSuffix();
            
            // El idParent no se necesita porque en el insertBefore se pasa el nodo parent como argumento
            return new NodeLocationPathBasedNotParentImpl(nextSibling,idRefChild,refChildPathRel,clientDoc);            
        }
        else
        {
            // Está ya cacheado
            return new NodeLocationAlreadyCachedNotParentImpl(nextSibling,idRefChild,clientDoc);              
        }

    }

    public static NodeLocationImpl getNodeLocationRelativeToParent(ClientDocumentStfulDelegateImpl clientDoc,Node node)
    {
        if (node == null) return new NodeLocationNullImpl(clientDoc);

        // Este método es útil cuando se dispone ya del parent en el cliente obtenido de alguna forma
        String idNode = null;
        String nodePathRel = null;
        boolean needNodePath = false;

        NodeCacheRegistryImpl nodeCache = clientDoc.getNodeCacheRegistry();
        if (nodeCache != null)
        {
            idNode = nodeCache.getId(node);
            if (idNode != null) // Está en la caché
            {
                needNodePath = false; // está cacheado, no necesitamos calcular el path
            }
            else
            {
                // Intentamos cachear ahora, necesitamos calcular el path para que se cachee en el cliente también
                needNodePath = true;
                idNode = nodeCache.addNode(node); // el caso null es que no es cacheable por ejemplo, o la caché está "bloqueada"
            }
        }
        else needNodePath = true;

        if (needNodePath)
        {
            // Necesitamos calcular el path
            nodePathRel = clientDoc.getRelativeStringPathFromNodeParent(node);
            
            // El idParent no se necesita porque el nodo parent se obtiene por otras vías
            return new NodeLocationPathBasedNotParentImpl(node,idNode,nodePathRel,clientDoc);            
        }
        else        
        {
            // Está ya cacheado
            return new NodeLocationAlreadyCachedNotParentImpl(node,idNode,clientDoc);              
        }                
    }        
    
    public static NodeLocationImpl getNodeLocationNotParent(Node node,String id,String path,ClientDocumentStfulDelegateImpl clientDoc)
    {    
        if (node == null) return new NodeLocationNullImpl(clientDoc);
        
        if (path != null)        
            return new NodeLocationPathBasedNotParentImpl(node,id,path,clientDoc);                    
        else // Está ya cacheado
            return new NodeLocationAlreadyCachedNotParentImpl(node,id,clientDoc);                      
    }


}
