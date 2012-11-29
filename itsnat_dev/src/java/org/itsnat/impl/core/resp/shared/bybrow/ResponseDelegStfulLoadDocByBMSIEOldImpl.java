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
package org.itsnat.impl.core.resp.shared.bybrow;

import java.util.LinkedList;
import org.itsnat.impl.core.resp.shared.*;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.BrowserMSIEPocket;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.res.core.js.LoadScriptImpl;

/**
 *
 * @author jmarranz
 */
public class ResponseDelegStfulLoadDocByBMSIEOldImpl extends ResponseDelegStfulLoadDocByBrowserImpl
{
    public ResponseDelegStfulLoadDocByBMSIEOldImpl(ResponseDelegateStfulLoadDocImpl parent)
    {
        super(parent);
    }

    public String getOnInitScriptContentCodeFixDOMCode()
    {
        return null;
    }

    public void fillFrameworkScriptFileNamesOfBrowser(LinkedList list)
    {
        ClientDocumentStfulImpl clientDoc = parent.getClientDocumentStful();
        Browser browser = clientDoc.getBrowser();

        list.add(LoadScriptImpl.ITSNAT_MSIE_OLD);
        if (browser instanceof BrowserMSIEPocket)
            list.add(LoadScriptImpl.ITSNAT_MSIE_POCKET);
    }

    public String getJSMethodInitName()
    {
        ClientDocumentStfulImpl clientDoc = parent.getClientDocumentStful();
        Browser browser = clientDoc.getBrowser();

        if (browser instanceof BrowserMSIEPocket)
            return "itsnat_init_msie_pocket";
        else
            return "itsnat_init_msie_old";
    }
}
