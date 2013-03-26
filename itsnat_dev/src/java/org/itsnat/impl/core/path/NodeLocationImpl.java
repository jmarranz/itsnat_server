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
package org.itsnat.impl.core.path;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.jsren.JSRenderImpl;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class NodeLocationImpl 
{
    protected ClientDocumentStfulImpl clientDoc;
    protected String id;
    protected Node node;       
    protected boolean used = false;
    
    public NodeLocationImpl(Node node,String id,ClientDocumentStfulImpl clientDoc)
    {
        this.node = node;
        this.id = id;
        this.clientDoc = clientDoc;        
    }    
    
    /*
    public static NodeLocationImpl getNodeLocationAlreadyCached(ClientDocumentStfulImpl clientDoc,Node node,String id)
    {
        if (id == null || id.equals("")) throw new ItsNatException("INTERNAL ERROR");
        return new NodeLocationNotParentImpl(node,id,null,clientDoc);
    }    
    */
    
    public static NodeLocationImpl getNodeLocation(ClientDocumentStfulImpl clientDoc,Node node,boolean cacheIfPossible)
    {
        if (node == null) return NodeLocationNullImpl.getNodeLocationNull(clientDoc);

        return NodeLocationWithParentImpl.getNodeLocationWithParent(node,cacheIfPossible,clientDoc);
    }

    public static NodeLocationImpl getRefNodeLocationInsertBefore(ClientDocumentStfulImpl clientDoc,Node newNode,Node nextSibling)
    {
        // El NodeLocationImpl a obtener es el de nextSibling
        if (nextSibling == null) return NodeLocationNullImpl.getNodeLocationNull(clientDoc);

        return NodeLocationNotParentImpl.getNodeLocationNotParentInsertBefore(newNode,nextSibling,clientDoc);
    }

    public static NodeLocationImpl getNodeLocationRelativeToParent(ClientDocumentStfulImpl clientDoc,Node node)
    {
        if (node == null) return NodeLocationNullImpl.getNodeLocationNull(clientDoc);

        return NodeLocationNotParentImpl.getNodeLocationNotParentRelativeToParent(node,clientDoc);
    }    
    
    public Node getNode()
    {
        return node;
    }

    private String getId()
    {
        return id;
    }    
    
    protected static String getIdJS(String id)
    {
        return JSRenderImpl.toLiteralStringJS(id);
    }            
    
    protected String getIdJS()
    {
        return getIdJS(getId());
    }        
    
    protected boolean isNull(String str)
    {
        return ((str == null) || str.equals("null"));
    }    
    
    public void throwNullException()
    {
        if (node == null) throw new ItsNatException("No specified node");
    }

    public ClientDocumentStfulImpl getClientDocumentStful()
    {
        return clientDoc;
    }

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
    
    public static String toJSNodeLocationOnlyId(String id)
    {
        return id != null ? "[" + getIdJS(id) + "]" : "null";
    }    
    
    public String toJSNodeLocationOnlyId()
    {
        return toJSNodeLocationOnlyId(id);
    }        
    
    public abstract String toJSNodeLocation(boolean errIfNull);    
}
