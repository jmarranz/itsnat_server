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

package org.itsnat.impl.core.resp.shared.bybrow.web;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.resp.ResponseLoadStfulDocumentValid;

/**
 *
 * @author jmarranz
 */
public class ResponseLoadStfulDocumentValidFakeForOpera implements ResponseLoadStfulDocumentValid
{
    protected ClientDocumentStfulImpl clientDoc;

    public ResponseLoadStfulDocumentValidFakeForOpera(ClientDocumentStfulImpl clientDoc)
    {
        this.clientDoc = clientDoc;
    }

    public ClientDocumentStfulImpl getClientDocumentStful()
    {
        return clientDoc;
    }

    public ClientDocumentImpl getClientDocument()
    {
        return clientDoc;
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return clientDoc.getItsNatStfulDocument();
    }

    public boolean isSerializeBeforeDispatching()
    {
        throw new ItsNatException("INTERNAL ERROR");
    }

    public void preSerializeDocumentStful()
    {
        throw new ItsNatException("INTERNAL ERROR");
    }

    public boolean isOnlyReturnMarkupOfFinalScripts()
    {
        throw new ItsNatException("INTERNAL ERROR");
    }

    public boolean isNeededAbsoluteURL()
    {
        throw new ItsNatException("INTERNAL ERROR");
    }

    public boolean isInlineLoadFrameworkScripts()
    {
        throw new ItsNatException("INTERNAL ERROR");
    }


}
