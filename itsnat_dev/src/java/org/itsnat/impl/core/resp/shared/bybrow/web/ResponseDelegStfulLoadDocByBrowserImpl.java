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


import org.itsnat.impl.core.resp.shared.bybrow.web.ResponseDelegStfulLoadDocByWebBrowserImpl;
import org.itsnat.impl.core.resp.shared.bybrow.droid.ResponseDelegStfulLoadDocByDroidImpl;
import org.itsnat.impl.core.resp.shared.*;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.droid.BrowserDroid;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ResponseDelegStfulLoadDocByBrowserImpl
{
    protected ResponseDelegateStfulLoadDocImpl parent;

    public ResponseDelegStfulLoadDocByBrowserImpl(ResponseDelegateStfulLoadDocImpl parent)
    {
        this.parent = parent;
    }

    public static ResponseDelegStfulLoadDocByBrowserImpl createResponseDelegStfulLoadDocByBrowser(ResponseDelegateStfulLoadDocImpl parent)
    {
        Browser browser = parent.getClientDocumentStful().getBrowser();
        if (browser instanceof BrowserDroid)
            return new ResponseDelegStfulLoadDocByDroidImpl((ResponseDelegateStfulDroidLoadDocImpl)parent);
        else
            return ResponseDelegStfulLoadDocByWebBrowserImpl.createResponseDelegStfulLoadDocByWebBrowser((ResponseDelegateStfulWebLoadDocImpl)parent);
    }

    public ClientDocumentStfulImpl getClientDocumentStful()
    {
        return parent.getClientDocumentStful();
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return parent.getItsNatStfulDocument();
    }

    public abstract String getOnInitScriptContentCodeFixDOMCode();

}
