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

package org.itsnat.impl.core.domimpl.deleg;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.domimpl.ItsNatDocumentInternal;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public class DelegateDocumentImpl extends DelegateNodeImpl
{
    protected ItsNatDocumentImpl itsNatDoc;
    protected int mutEventInternal = 0;

    public DelegateDocumentImpl(ItsNatDocumentInternal node)
    {
        super(node);
    }

    public Document getDocument()
    {
        return (Document)node;
    }

    public boolean isMutationEventInternal()
    {
        return mutEventInternal > 0;
    }

    public void setMutationEventInternal(boolean value)
    {
        if (value) this.mutEventInternal++;
        else this.mutEventInternal--;
    }

    public boolean isInternalMode()
    {
        if (itsNatDoc == null) return true;
        if (internalMode == null) // No ha sido definido, tomamos el modo por defecto del documento
            return itsNatDoc.isDOMInternalMode();
        else
            return internalMode.booleanValue();
    }

    public DelegateDocumentImpl getDelegateDocument()
    {
        return this;
    }

    public ItsNatDocumentImpl getItsNatDocument()
    {
        return itsNatDoc;
    }

    public void setItsNatDocument(ItsNatDocumentImpl itsNatDoc)
    {
        if ((itsNatDoc != null) && (itsNatDoc.getDocument() != getDocument()))
            throw new ItsNatException("INTERNAL ERROR");
        this.itsNatDoc = itsNatDoc;
    }

    public Event createRemoteEvent(String eventType) throws DOMException
    {
        return itsNatDoc.createEvent(eventType);
    }

    public boolean isDisconnectedFromClient()
    {
        checkHasSenseDisconnectedFromClient();
        return false; // No se puede desconectar
    }

    public boolean isDisconnectedChildNodesFromClient()
    {
        checkHasSenseDisconnectedChildNodesFromClient();
        return false; // No se puede desconectar el contenido del documento (el elemento root por ejemplo)
    }

    public void setDisconnectedChildNodesFromClient(boolean disconnectedChildNodesFromClient)
    {
        checkHasSenseDisconnectedChildNodesFromClient();
        if (disconnectedChildNodesFromClient) throw new ItsNatException("Child nodes of document cannot be disconnected from client");
    }
}
