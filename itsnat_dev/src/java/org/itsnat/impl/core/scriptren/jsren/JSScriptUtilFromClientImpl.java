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

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class JSScriptUtilFromClientImpl extends JSScriptUtilImpl
{
    protected ClientDocumentStfulDelegateWebImpl clientDoc;

    public JSScriptUtilFromClientImpl(ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        this.clientDoc = clientDoc;
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return clientDoc.getItsNatStfulDocument();
    }

    public ClientDocumentStfulDelegateWebImpl getCurrentClientDocumentStfulDelegateWeb()
    {
        return clientDoc;
    }

    public void checkAllClientsCanReceiveJSCode()
    {
        if (!clientDoc.getClientDocumentStful().isSendCodeEnabled())
            throw new ItsNatException("This client cannot receive JavaScript code",this);
    }

    protected boolean preventiveNodeCaching2(Node node)
    {
        return preventiveNodeCachingOneClient(node,clientDoc);
    }
}
