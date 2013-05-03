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
public abstract class NodeLocationNotNullImpl extends NodeLocationImpl 
{
    protected String id;
    protected Node node;       
    
    public NodeLocationNotNullImpl(Node node,String id,ClientDocumentStfulImpl clientDoc)
    {
        super(clientDoc);
        this.node = node;
        this.id = id;      
        
        if (node == null) throw new ItsNatException("INTERNAL ERROR");         
    }    
   
    public Node getNode()
    {
        return node;
    }

    protected String getId()
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

}
