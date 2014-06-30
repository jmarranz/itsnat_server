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
package org.itsnat.impl.core.scriptren.bsren;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.doc.droid.ItsNatDroidDocumentImpl;
import org.itsnat.impl.core.scriptren.shared.JSAndBSScriptUtilFromDocSharedImpl;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class BSScriptUtilFromDocImpl extends BSScriptUtilImpl
{
    protected JSAndBSScriptUtilFromDocSharedImpl delegate;

    public BSScriptUtilFromDocImpl(ItsNatDroidDocumentImpl itsNatDoc)
    {
        this.delegate = new JSAndBSScriptUtilFromDocSharedImpl(this,itsNatDoc);
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return delegate.getItsNatStfulDocument();
    }

    public ClientDocumentStfulDelegateImpl getCurrentClientDocumentStfulDelegate()    
    {
        return delegate.getCurrentClientDocumentStfulDelegate();
    }
 
    @Override
    public void checkAllClientsCanReceiveScriptCode()
    {
        delegate.checkAllClientsCanReceiveScriptCode();
    }

    protected boolean preventiveNodeCaching2(Node node)
    {
        return delegate.preventiveNodeCaching2(node);
    }

    @Override
    protected void preventiveNodeCaching(Node node,String id,ClientDocumentStfulDelegateImpl clientDoc)
    {
        // Es el caso de nodo ya cacheado en el que necesitamos armonizar el id (que sea el mismo) con los demás clientes, de otra manera al intentar cachear el mismo nodo con otro id daría error
        clientDoc.removeNodeFromCacheAndSendCode(node);

        super.preventiveNodeCaching(node,id,clientDoc);
    }
}
