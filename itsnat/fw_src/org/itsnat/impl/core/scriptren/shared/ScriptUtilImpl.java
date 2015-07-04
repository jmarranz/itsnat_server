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

package org.itsnat.impl.core.scriptren.shared;

import org.itsnat.core.ItsNatException;
import org.itsnat.core.script.ScriptExpr;
import org.itsnat.core.script.ScriptUtil;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.NodeCacheRegistryImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.doc.droid.ItsNatStfulDroidDocumentImpl;
import org.itsnat.impl.core.doc.web.ItsNatStfulWebDocumentImpl;
import org.itsnat.impl.core.dompath.NodeLocationWithParentImpl;
import org.itsnat.impl.core.scriptren.bsren.BSScriptUtilFromDocImpl;
import org.itsnat.impl.core.scriptren.jsren.JSScriptUtilFromDocImpl;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class ScriptUtilImpl implements ScriptUtil
{
    public static ScriptUtilImpl createScriptUtilFromDoc(ItsNatStfulDocumentImpl itsNatDoc)
    {
        if (itsNatDoc instanceof ItsNatStfulWebDocumentImpl)
            return new JSScriptUtilFromDocImpl((ItsNatStfulWebDocumentImpl)itsNatDoc);
        else if (itsNatDoc instanceof ItsNatStfulDroidDocumentImpl)
            return new BSScriptUtilFromDocImpl((ItsNatStfulDroidDocumentImpl)itsNatDoc);
        return null;
    }
    

    public void checkAllClientsCanReceiveScriptCode()
    {
        ClientDocumentStfulDelegateImpl clientDoc = getCurrentClientDocumentStfulDelegate();
        if (!clientDoc.getClientDocumentStful().isSendCodeEnabled())
            throw new ItsNatException("This client cannot receive JavaScript code",this);
    }    
    
    public String getNodeReference(Node node)
    {
        if (node == null) return "null";
        preventiveNodeCaching(node);
        ClientDocumentStfulDelegateImpl clientDoc = getCurrentClientDocumentStfulDelegate();
        // Es muy importante el cacheIfPossible = false pues antes hemos hecho
        // un esfuerzo en cachear y enviar al cliente, se trata de minimizar los
        // problemas que pueden ocurrir si el usuario no envía el código, para
        // ello la llamada JavaScript debe ser "sólo lectura" es decir sin cambio de estado
        // para ello evitamos el cacheado del propio nodo y de padres.
        return clientDoc.getNodeReference(node,false,true);
    }    
    
    public String getTransportableStringLiteral(String text)
    {
        ClientDocumentStfulDelegateImpl clientDoc = getCurrentClientDocumentStfulDelegate();

        return JSAndBSRenderImpl.toTransportableStringLiteral(text,clientDoc.getBrowser());
    }

    public String getTransportableCharLiteral(char c)
    {
        ClientDocumentStfulDelegateImpl clientDoc = getCurrentClientDocumentStfulDelegate();

        return JSAndBSRenderImpl.getTransportableCharLiteral(c,clientDoc.getBrowser());
    }    
    
    protected boolean isNodeCachedWithId(Node node,String id,ClientDocumentStfulDelegateImpl clientDoc)
    {
        NodeCacheRegistryImpl nodeCache = clientDoc.getNodeCacheRegistry(); // No puede ser nula
        Node nodeCached = nodeCache.getNodeById(id);
        return node == nodeCached;
    }    
    
    public boolean preventiveNodeCaching(Object obj)
    {
        if (obj == null) return false;
        if (!(obj instanceof Node)) return false;

        return preventiveNodeCaching((Node)obj);
    }

    public boolean preventiveNodeCaching(final Node node)
    {
        if (node == null) return false;

        /* Cada cliente tiene su propia cache sin embargo lo normal
         * es que la cadena de localización del nodo se envíe a todos
         * los clientes usando ItsNatDocument.addCodeToSend(Object)
         * por tanto tenemos que conseguir que la misma cadena valga
         * para todos los clientes.
         * En el caso de paths absolutos no hay problema pues el path absoluto
         * es el mismo en todos los clientes (salvo IE Mobile en donde los comentarios son invisibles)
         * pero si el nodo está cacheado es un problema pues los ids pueden ser diferentes.
         * Por ello lo que hacemos es eliminar el nodo en cuestión de las cachés
         * y volverlo a añadir usando un mismo id, como los ids son generados por el documento
         * no hay problema de compartición entre los cachés de los clientes.
         * Además así evitamos el posible error de que el programador no envíe
         * el nodo al cliente o a todos los clientes por olvido por error antes de enviar etc,
         * pues al generar la cadena podría cachearse automáticamente en ese momento en el servidor
         * y no enterarse el cliente (al no recibir la cadena), al estar ya cacheado no hay problema.
         * Esto puede no funcionar con IE Mobile si la caché está desactivada, hay comentarios o <meta> por medio en el cálculo del path absoluto
         * y hay control remoto, debido a que el path absoluto calculado por el cliente principal
         * no será el mismo que el necesario para el cliente de control remoto, ya sea IE Mobile
         * el principal o el remoto.
         */

        checkAllClientsCanReceiveScriptCode();

        ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
        if (!itsNatDoc.isNodeCacheEnabled())
            return false; // Se generan paths absolutos

        if (!NodeCacheRegistryImpl.isCacheableNode(node,itsNatDoc.getDocument()))
            return false; // No es cacheable por lo que no está en las caches ni puede estar

        return preventiveNodeCaching2(node);
    }

    protected abstract boolean preventiveNodeCaching2(final Node node);

    protected boolean preventiveNodeCachingOneClient(Node node,ClientDocumentStfulDelegateImpl clientDoc)
    {
        NodeCacheRegistryImpl nodeCache = clientDoc.getNodeCacheRegistry(); // No puede ser nula la caché
        String oldId = nodeCache.getId(node);
        if (oldId != null) return false; // Ya está cacheado y sólo hay un cliente

        ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
        String id = NodeCacheRegistryImpl.generateUniqueId(itsNatDoc);
        preventiveNodeCaching(node,id,clientDoc);

        return true;
    }

    protected boolean preventiveNodeCaching(Object[] nodes)
    {
        if (nodes == null) return false;
        boolean someOneCached = false;
        for(int i = 0; i < nodes.length; i++)
            if (preventiveNodeCaching(nodes[i])) someOneCached = true;
        return someOneCached;
    }
    
    protected void preventiveNodeCaching(Node node,String id,ClientDocumentStfulDelegateImpl clientDoc)
    {
        NodeCacheRegistryImpl nodeCache = clientDoc.getNodeCacheRegistry(); // No puede ser nula
        nodeCache.addNode(node,id); // node no puede ser nulo (dará error)

        // Con cacheIfPossible = true también cacheamos padres, minimizando problemas.
        NodeLocationWithParentImpl nodeLoc = NodeLocationWithParentImpl.getNodeLocationWithParentUsingCache(node,id,true,nodeCache);
        if (!nodeLoc.isJustCached())
            throw new ItsNatException("INTERNAL ERROR");
        clientDoc.addCodeToSend( renderAddNodeToCache(nodeLoc) );
    }


    public String getCallMethodCode(Object obj,String methodName,Object[] params)
    {
        return getCallMethodCode(obj,methodName,params,false);
    }

    public String getCallMethodCode(Object obj,String methodName,Object[] params,boolean endSentence)
    {
        preventiveNodeCaching(obj);
        preventiveNodeCaching(params);
        ClientDocumentStfulDelegateImpl clientDoc = getCurrentClientDocumentStfulDelegate();
        // cacheIfPossible = false al igual que en getNodeReference
        return renderGetCallMethodCode(obj,methodName,params,endSentence,clientDoc);
    }

    public String getSetPropertyCode(Object obj,String propertyName,Object value)
    {
        return getSetPropertyCode(obj,propertyName,value,false);
    }    
    
    public String getGetPropertyCode(Object obj,String propertyName)
    {
        return getGetPropertyCode(obj,propertyName,false);
    }    
    
    public String getGetPropertyCode(Object obj,String propertyName,boolean endSentence)
    {
        preventiveNodeCaching(obj);
        ClientDocumentStfulDelegateImpl clientDoc = getCurrentClientDocumentStfulDelegate();
        // cacheIfPossible = false al igual que en getNodeReference
        return renderGetPropertyCode(obj,propertyName,endSentence,clientDoc);
    }    
    
    public String getSetPropertyCode(Object obj,String propertyName,Object value,boolean endSentence)
    {
        preventiveNodeCaching(obj);
        preventiveNodeCaching(value);
        ClientDocumentStfulDelegateImpl clientDoc = getCurrentClientDocumentStfulDelegate();
        // cacheIfPossible = false al igual que en getNodeReference
        return renderSetPropertyCode(obj,propertyName,value,endSentence,false,clientDoc);
    }    
    
    public String toScript(Object value)
    {
        preventiveNodeCaching(value);
        ClientDocumentStfulDelegateImpl clientDoc = getCurrentClientDocumentStfulDelegate();
        // cacheIfPossible = false al igual que en getNodeReference
        return javaToScript(value,clientDoc);
    }    
    
    public ScriptExpr createScriptExpr(Object value)
    {
        return new ScriptExprImpl(value,this);
    }    
    
    
    protected abstract String renderAddNodeToCache(NodeLocationWithParentImpl nodeLoc);    
    public abstract ClientDocumentStfulDelegateImpl getCurrentClientDocumentStfulDelegate();    
    public abstract ItsNatStfulDocumentImpl getItsNatStfulDocument();    
    protected abstract String renderGetCallMethodCode(Object obj,String methodName,Object[] params,boolean endSentence,ClientDocumentStfulDelegateImpl clientDoc);    
    protected abstract String renderSetPropertyCode(Object obj,String propertyName,Object value,boolean endSentence,boolean cacheIfPossible,ClientDocumentStfulDelegateImpl clientDoc);
    protected abstract String renderGetPropertyCode(Object obj,String propertyName,boolean endSentence,ClientDocumentStfulDelegateImpl clientDoc);    
    protected abstract String javaToScript(Object value,ClientDocumentStfulDelegateImpl clientDoc);   
}
