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

import org.itsnat.impl.core.browser.web.opera.BrowserOperaOld;
import org.itsnat.impl.core.browser.web.opera.BrowserOperaOldDesktop;
import org.itsnat.impl.core.browser.web.opera.BrowserOperaOldMini;
import org.itsnat.impl.core.browser.web.opera.BrowserOperaOldMobile;
import org.itsnat.impl.core.resp.ResponseLoadStfulDocumentValid;
import org.itsnat.impl.core.resp.shared.bybrow.web.ResponseDelegStfulLoadDocByBOperaOldImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ResponseDelegateHTMLLoadDocOperaOldImpl extends ResponseDelegateHTMLLoadDocW3CImpl
{
    public ResponseDelegateHTMLLoadDocOperaOldImpl(ResponseLoadStfulDocumentValid response)
    {
        super(response);
    }

    public static ResponseDelegateHTMLLoadDocOperaOldImpl createResponseDelegateHTMLLoadDocOperaOld(BrowserOperaOld browser,ResponseLoadStfulDocumentValid responseParent)
    {
        if (browser instanceof BrowserOperaOld)
        {
            if (browser instanceof BrowserOperaOldMini)
                return new ResponseDelegateHTMLLoadDocOperaOldMiniImpl(responseParent);
            else if (browser instanceof BrowserOperaOldMobile)
                return new ResponseDelegateHTMLLoadDocOperaOldMobileImpl(responseParent);
            else if (browser instanceof BrowserOperaOldDesktop)
                return new ResponseDelegateHTMLLoadDocOperaOldDesktopImpl(responseParent);
            else
                return null; // No hay más
        }
        else
            return null; // No hay más
    }
    

    public ResponseDelegStfulLoadDocByBOperaOldImpl getResponseDelegStfulLoadDocByBOperaOld()
    {
        return (ResponseDelegStfulLoadDocByBOperaOldImpl)delegByBrowser;
    }

    public void dispatchRequestListeners()
    {
        super.dispatchRequestListeners();

        getResponseDelegStfulLoadDocByBOperaOld().afterLoadRewriteClientUIControlProperties();
    }    
}

