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


package org.itsnat.impl.core.scriptren.shared.node;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;

/**
 *
 * @author jmarranz
 */
public class NodeScriptRefImpl
{
    protected Object nodeRef;
    protected ClientDocumentStfulDelegateImpl clientDoc;

    public NodeScriptRefImpl(NodeLocationImpl nodeRef)
    {
        this.nodeRef = nodeRef;
        this.clientDoc = nodeRef.getClientDocumentStfulDelegate();
    }

    public NodeScriptRefImpl(String nodeRef,ClientDocumentStfulDelegateImpl clientDoc)
    {
        this.nodeRef = nodeRef;
        this.clientDoc = clientDoc;
    }

    public ClientDocumentStfulDelegateImpl getClientDocumentStfulDelegate()
    {
        return clientDoc;
    }

    public Object getNodeRef()
    {
        return nodeRef;
    }
}
