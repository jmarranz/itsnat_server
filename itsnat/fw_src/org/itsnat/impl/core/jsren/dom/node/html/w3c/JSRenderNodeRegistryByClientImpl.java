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

package org.itsnat.impl.core.jsren.dom.node.html.w3c;

import java.util.HashMap;
import java.util.Map;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.NameValue;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class JSRenderNodeRegistryByClientImpl
{
    protected ClientDocumentStfulImpl clientDoc;
    protected String prefix;
    protected Map idMap = new HashMap();
    protected Map nodeMap = new HashMap();

    public JSRenderNodeRegistryByClientImpl(String prefix,ClientDocumentStfulImpl clientDoc)
    {
        this.prefix = prefix;
        this.clientDoc = clientDoc;
    }

    public String registerNode(Node node)
    {
        String id = clientDoc.getUniqueIdGenerator().generateId(prefix);
        NameValue pair = new NameValue(id,null);
        if (idMap.put(id,node) != null) throw new ItsNatException("INTERNAL ERROR");
        if (nodeMap.put(node,pair) != null) throw new ItsNatException("INTERNAL ERROR");
        return id;
    }

    public String unRegisterNode(Node node)
    {
        return unRegisterNode(node,true);
    }

    public String unRegisterNode(Node node,boolean throwErr)
    {
        NameValue pair = (NameValue)nodeMap.remove(node); // Si pair es nulo dará error
        String id = pair.getName();
        if (idMap.remove(id) == null) if (throwErr) throw new ItsNatException("INTERNAL ERROR");
        return id;
    }

    public Node getNodeById(String id)
    {
        return (Node)idMap.get(id);
    }

    public String getIdByNode(Node node)
    {
        NameValue pair = (NameValue)nodeMap.get(node);
        if (pair == null) return null;
        return pair.getName();
    }

    public NameValue getNameValueByNode(Node node)
    {
        return (NameValue)nodeMap.get(node);
    }
}
