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

package org.itsnat.impl.core.clientdoc;

import org.itsnat.core.script.ScriptUtil;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.droid.BrowserDroid;
import org.itsnat.impl.core.browser.web.BrowserWeb;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.mut.client.ClientMutationEventListenerStfulImpl;
import org.itsnat.impl.core.dompath.DOMPathResolver;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public abstract class ClientDocumentStfulDelegateImpl
{
    protected ClientDocumentStfulImpl clientDoc;
    protected ClientMutationEventListenerStfulImpl mutationListener; 
    protected NodeCacheRegistryImpl nodeCache;     
    protected DOMPathResolver pathResolver;
    
    public ClientDocumentStfulDelegateImpl(ClientDocumentStfulImpl clientDoc)
    {
        this.clientDoc = clientDoc;
        this.mutationListener = ClientMutationEventListenerStfulImpl.createClientMutationEventListenerStful(this); 
        this.pathResolver = DOMPathResolver.createDOMPathResolver(this);        
        // A día de hoy sólo los documentos HTML y SVG generan JavaScript necesario para mantener un caché de nodos en el cliente
        if (clientDoc.getItsNatStfulDocument().isNodeCacheEnabled())
            this.nodeCache = new NodeCacheRegistryImpl(this);          
    }

    public static ClientDocumentStfulDelegateImpl createClientDocumentStfulDelegate(ClientDocumentStfulImpl clientDoc)
    {
        Browser browser = clientDoc.getBrowser();
        if (browser instanceof BrowserWeb)
            return new ClientDocumentStfulDelegateWebImpl(clientDoc);
        else if (browser instanceof BrowserDroid)
            return new ClientDocumentStfulDelegateDroidImpl(clientDoc);        
        else
            return null;
    }
    
    public ClientDocumentStfulImpl getClientDocumentStful()
    {
        return clientDoc;
    }
    
    public Browser getBrowser()
    {
        return clientDoc.getBrowser();
    }      
    
    public DOMPathResolver getDOMPathResolver()
    {
        return pathResolver;
    }        
    
    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return clientDoc.getItsNatStfulDocument();
    }
        
    public ClientMutationEventListenerStfulImpl getClientMutationEventListenerStful()
    {
        return mutationListener;
    }        
    
    public boolean isNodeCacheEnabled()
    {
        return nodeCache != null;
    }

    public NodeCacheRegistryImpl getNodeCacheRegistry()
    {
        return nodeCache; // puede ser null (no caché)
    }    
    
    public void addCodeToSend(Object code)
    {
        clientDoc.addCodeToSend(code);
    }
    
    public String getCachedNodeId(Node node)
    {
        NodeCacheRegistryImpl cacheNode = getNodeCacheRegistry();
        if (cacheNode == null) return null;
        return cacheNode.getId(node);
    }        
    
    public String removeNodeFromCache(Node node)
    {
        NodeCacheRegistryImpl cacheNode = getNodeCacheRegistry();
        if (cacheNode == null)
            return null;
        return cacheNode.removeNode(node);
    }        
    
    public Node getNodeFromPath(String pathStr,Node topParent)
    {
        return getDOMPathResolver().getNodeFromPath(pathStr,topParent);
    }    
    
    public String getRelativeStringPathFromNodeParent(Node child)
    {
        return getDOMPathResolver().getRelativeStringPathFromNodeParent(child);
    }    
    
    public String getStringPathFromNode(Node node)
    {
        return getDOMPathResolver().getStringPathFromNode(node);
    }    
    
    public String getStringPathFromNode(Node node,Node topParent)
    {
        return getDOMPathResolver().getStringPathFromNode(node,topParent);
    }    
    
    public Node getNextSiblingInClientDOM(Node node)
    {
        return getDOMPathResolver().getNextSiblingInClientDOM(node);        
    }        
    
    public NodeLocationImpl getNodeLocation(Node node,boolean cacheIfPossible)
    {
        return NodeLocationImpl.getNodeLocation(this,node,cacheIfPossible);
    }    
    
    public NodeLocationImpl getRefNodeLocationInsertBefore(Node newNode,Node nextSibling)
    {
        return NodeLocationImpl.getRefNodeLocationInsertBefore(this, newNode, nextSibling);
    }    
    
    public abstract ScriptUtil createScriptUtil();
    public abstract boolean dispatchEvent(EventTarget target,Event evt,int commMode,long eventTimeout) throws EventException;   
    public abstract String getNodeReference(Node node,boolean cacheIfPossible,boolean errIfNull);    
}
