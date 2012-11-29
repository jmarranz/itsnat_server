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

package org.itsnat.impl.core.resp.shared.html;

import java.util.LinkedList;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.BrowserMSIEOld;
import org.itsnat.impl.core.browser.BrowserMSIE6;
import org.itsnat.impl.core.browser.BrowserMSIEPocket;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.resp.ResponseLoadStfulDocumentValid;

/**
 *
 * @author jmarranz
 */
public abstract class ResponseDelegateHTMLLoadDocMSIEOldImpl extends ResponseDelegateHTMLLoadDocImpl
{
    public ResponseDelegateHTMLLoadDocMSIEOldImpl(ResponseLoadStfulDocumentValid response)
    {
        super(response);
    }

    public static ResponseDelegateHTMLLoadDocMSIEOldImpl createResponseDelegateHTMLLoadDocMSIEOld(BrowserMSIEOld browser,ResponseLoadStfulDocumentValid responseParent)
    {
        if (browser instanceof BrowserMSIE6)
            return new ResponseDelegateHTMLLoadDocMSIE6Impl(responseParent);
        else if (browser instanceof BrowserMSIEPocket)
            return new ResponseDelegateHTMLLoadDocMSIEPocketImpl(responseParent);
        else
            return null; // No hay más
    }

    protected LinkedList fixOtherNSElementsInHTMLFindRootElems()
    {
        return null; // Nada que hacer, sólo W3C
    }

    protected LinkedList fixOtherNSElementsInHTMLSaveValidNames(LinkedList otherNSRootElemsInHTML)
    {
        return null; // Nada que hacer, sólo W3C
    }

    protected void fixOtherNSElementsInHTMLCleanAuxAttribs(LinkedList attribs)
    {
        // Nada que hacer, sólo W3C
    }

    protected void fixOtherNSElementsInHTMLGenCode(LinkedList otherNSElemsInHTML)
    {
        // Nada que hacer, sólo W3C
    }

}
