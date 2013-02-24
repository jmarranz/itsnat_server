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

import org.itsnat.impl.core.browser.opera.BrowserOpera;
import org.itsnat.impl.core.browser.opera.BrowserOperaDesktop;
import org.itsnat.impl.core.browser.opera.BrowserOperaMini;
import org.itsnat.impl.core.browser.opera.BrowserOperaMobile;
import org.itsnat.impl.core.resp.ResponseLoadStfulDocumentValid;
import org.itsnat.impl.core.resp.shared.bybrow.ResponseDelegStfulLoadDocByBOperaImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ResponseDelegateHTMLLoadDocOperaImpl extends ResponseDelegateHTMLLoadDocW3CImpl
{
    public ResponseDelegateHTMLLoadDocOperaImpl(ResponseLoadStfulDocumentValid response)
    {
        super(response);
    }

    public static ResponseDelegateHTMLLoadDocOperaImpl createResponseDelegateHTMLLoadDocOpera(BrowserOpera browser,ResponseLoadStfulDocumentValid responseParent)
    {
        if (browser instanceof BrowserOpera)
        {
            if (browser instanceof BrowserOperaMini)
                return new ResponseDelegateHTMLLoadDocOperaMiniImpl(responseParent);
            else if (browser instanceof BrowserOperaMobile)
                return new ResponseDelegateHTMLLoadDocOperaMobileImpl(responseParent);
            else if (browser instanceof BrowserOperaDesktop)
                return new ResponseDelegateHTMLLoadDocOperaDesktopImpl(responseParent);
            else
                return null; // No hay más
        }
        else
            return null; // No hay más
    }
    

    public ResponseDelegStfulLoadDocByBOperaImpl getResponseDelegStfulLoadDocByBOpera()
    {
        return (ResponseDelegStfulLoadDocByBOperaImpl)delegByBrowser;
    }

    public void dispatchRequestListeners()
    {
        super.dispatchRequestListeners();

        getResponseDelegStfulLoadDocByBOpera().afterLoadRewriteClientUIControlProperties();
    }    
}

