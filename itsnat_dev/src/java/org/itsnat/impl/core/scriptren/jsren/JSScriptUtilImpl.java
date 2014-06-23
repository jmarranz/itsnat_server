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

package org.itsnat.impl.core.scriptren.jsren;

import java.io.Serializable;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.script.ScriptExpr;
import org.itsnat.core.script.ScriptUtil;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.clientdoc.NodeCacheRegistryImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.scriptren.jsren.dom.node.JSRenderNodeImpl;
import org.itsnat.impl.core.dompath.NodeLocationWithParentImpl;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class JSScriptUtilImpl implements ScriptUtil,Serializable
{
    /**
     * Creates a new instance of ScriptUtil
     */
    public JSScriptUtilImpl()
    {
    }

    public abstract ItsNatStfulDocumentImpl getItsNatStfulDocument();
    public abstract ClientDocumentStfulDelegateWebImpl getCurrentClientDocumentStfulDelegateWeb();

    public String getNodeReference(Node node)
    {
        if (node == null) return "null";
        preventiveNodeCaching(node);
        ClientDocumentStfulDelegateWebImpl clientDoc = getCurrentClientDocumentStfulDelegateWeb();
        // Es muy importante el cacheIfPossible = false pues antes hemos hecho
        // un esfuerzo en cachear y enviar al cliente, se trata de minimizar los
        // problemas que pueden ocurrir si el usuario no envía el código, para
        // ello la llamada JavaScript debe ser "sólo lectura" es decir sin cambio de estado
        // para ello evitamos el cacheado del propio nodo y de padres.
        return clientDoc.getNodeReference(node,false,true);
    }

    public String getTransportableStringLiteral(String text)
    {
        ClientDocumentStfulDelegateWebImpl clientDoc = getCurrentClientDocumentStfulDelegateWeb();

        return JSRenderImpl.toTransportableStringLiteral(text,clientDoc.getBrowserWeb());
    }

    public String getTransportableCharLiteral(char c)
    {
        ClientDocumentStfulDelegateWebImpl clientDoc = getCurrentClientDocumentStfulDelegateWeb();

        return JSRenderImpl.getTransportableCharLiteral(c,clientDoc.getBrowserWeb());
    }

    public String encodeURIComponent(String text)
    {
        return JSRenderImpl.encodeURIComponent(text);
    }

    public String encodeURIComponent(char c)
    {
        return JSRenderImpl.encodeURIComponent(c);
    }

    public String getCallMethodCode(Object obj,String methodName,Object[] params)
    {
        return getCallMethodCode(obj,methodName,params,false);
    }

    public String getCallMethodCode(Object obj,String methodName,Object[] params,boolean endSentence)
    {
        preventiveNodeCaching(obj);
        preventiveNodeCaching(params);
        ClientDocumentStfulDelegateWebImpl clientDoc = getCurrentClientDocumentStfulDelegateWeb();
        // cacheIfPossible = false al igual que en getNodeReference
        return JSRenderMethodCallImpl.getCallMethodCode(obj,methodName,params,endSentence,false,clientDoc);
    }

    public String getSetPropertyCode(Object obj,String propertyName,Object value)
    {
        return getSetPropertyCode(obj,propertyName,value,false);
    }

    public String getSetPropertyCode(Object obj,String propertyName,Object value,boolean endSentence)
    {
        preventiveNodeCaching(obj);
        preventiveNodeCaching(value);
        ClientDocumentStfulDelegateWebImpl clientDoc = getCurrentClientDocumentStfulDelegateWeb();
        // cacheIfPossible = false al igual que en getNodeReference
        return JSRenderImpl.getSetPropertyCode(obj,propertyName,value,endSentence,false,clientDoc);
    }

    public String getGetPropertyCode(Object obj,String propertyName)
    {
        return getGetPropertyCode(obj,propertyName,false);
    }

    public String getGetPropertyCode(Object obj,String propertyName,boolean endSentence)
    {
        preventiveNodeCaching(obj);
        ClientDocumentStfulDelegateWebImpl clientDoc = getCurrentClientDocumentStfulDelegateWeb();
        // cacheIfPossible = false al igual que en getNodeReference
        return JSRenderImpl.getGetPropertyCode(obj,propertyName,endSentence,false,clientDoc);
    }

    public String toScript(Object value)
    {
        preventiveNodeCaching(value);
        ClientDocumentStfulDelegateWebImpl clientDoc = getCurrentClientDocumentStfulDelegateWeb();
        // cacheIfPossible = false al igual que en getNodeReference
        return JSRenderImpl.javaToJS(value,false,clientDoc);
    }

    public ScriptExpr createScriptExpr(Object value)
    {
        return new JScriptExprImpl(value,this);
    }

    public ScriptReference createScriptReference(Object value)
    {
        // POR AHORA NO ES PUBLICO.
        // Quizás más adelante cuando se haga un modelo completo de metaprogramación
        // JavaScript en java
        return new JSReferenceImpl(value,this);
    }

    protected abstract void checkAllClientsCanReceiveJSCode();

    protected boolean isNodeCachedWithId(Node node,String id,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        NodeCacheRegistryImpl nodeCache = clientDoc.getNodeCacheRegistry(); // No puede ser nula
        Node nodeCached = nodeCache.getNodeById(id);
        return node == nodeCached;
    }

    protected void preventiveNodeCaching(Node node,String id,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        NodeCacheRegistryImpl nodeCache = clientDoc.getNodeCacheRegistry(); // No puede ser nula
        nodeCache.addNode(node,id); // node no puede ser nulo (dará error)

        // Con cacheIfPossible = true también cacheamos padres, minimizando problemas.
        NodeLocationWithParentImpl nodeLoc = NodeLocationWithParentImpl.getNodeLocationWithParentUsingCache(node,id,true,nodeCache);
        if (!nodeLoc.isJustCached())
            throw new ItsNatException("INTERNAL ERROR");
        clientDoc.addCodeToSend( JSRenderNodeImpl.addNodeToCache(nodeLoc) );
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

        checkAllClientsCanReceiveJSCode();

        ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
        if (!itsNatDoc.isNodeCacheEnabled())
            return false; // Se generan paths absolutos

        if (!NodeCacheRegistryImpl.isCacheableNode(node,itsNatDoc.getDocument()))
            return false; // No es cacheable por lo que no está en las caches ni puede estar

        return preventiveNodeCaching2(node);
    }

    protected abstract boolean preventiveNodeCaching2(final Node node);

    protected boolean preventiveNodeCachingOneClient(Node node,ClientDocumentStfulDelegateWebImpl clientDoc)
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

}
