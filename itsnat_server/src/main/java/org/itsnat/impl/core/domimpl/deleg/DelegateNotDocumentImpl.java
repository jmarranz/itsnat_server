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

import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.domimpl.ItsNatDocumentInternal;
import org.itsnat.impl.core.domimpl.ItsNatNodeInternal;
import org.w3c.dom.Document;

/**
 *
 * @author jmarranz
 */
public abstract class DelegateNotDocumentImpl extends DelegateNodeImpl
{
    public DelegateNotDocumentImpl(ItsNatNodeInternal node)
    {
        super(node);
    }

    @Override
    public boolean isInternalMode()
    {
        DelegateDocumentImpl docDeleg = getDelegateDocument(); // No suponemos null, caso raro
        ItsNatDocumentImpl itsNatDoc = docDeleg.getItsNatDocument();
        if (itsNatDoc == null) return true; // Funcionamiento "normal"
        if (internalMode == null) // No ha sido definido, tomamos el modo por defecto del documento
            return docDeleg.isInternalMode();
        else
            return internalMode.booleanValue();
    }

    @Override
    public ItsNatDocumentImpl getItsNatDocument()
    {
        DelegateDocumentImpl docDeleg = getDelegateDocument();
        if (docDeleg == null) return null; // Muy raro
        return docDeleg.getItsNatDocument();
    }

    @Override
    public DelegateDocumentImpl getDelegateDocument()
    {
        Document doc = node.getOwnerDocument();
        if (doc == null) return null; // Raro
        return ((ItsNatDocumentInternal)doc).getDelegateDocument();
    }

    @Override
    public boolean isMutationEventInternal()
    {
        DelegateDocumentImpl docDeleg = getDelegateDocument();
        if (docDeleg == null) return false; // Muy raro, por poner algo
        return docDeleg.isMutationEventInternal();
    }

    @Override
    public void setMutationEventInternal(boolean value)
    {
        DelegateDocumentImpl docDeleg = getDelegateDocument();
        if (docDeleg == null) return; // Muy raro, por poner algo
        docDeleg.setMutationEventInternal(value);
    }



}
