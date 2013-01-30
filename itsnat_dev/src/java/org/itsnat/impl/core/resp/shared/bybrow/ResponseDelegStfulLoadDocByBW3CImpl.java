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
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.opera.BrowserOpera;
import org.itsnat.impl.core.browser.webkit.BrowserWebKit;
import org.itsnat.impl.core.resp.shared.*;
import org.itsnat.impl.res.core.js.LoadScriptImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ResponseDelegStfulLoadDocByBW3CImpl extends ResponseDelegStfulLoadDocByBrowserImpl
{
    public ResponseDelegStfulLoadDocByBW3CImpl(ResponseDelegateStfulLoadDocImpl parent)
    {
        super(parent);
    }

    public static ResponseDelegStfulLoadDocByBW3CImpl createResponseDelegStfulLoadDocByBW3C(ResponseDelegateStfulLoadDocImpl parent)
    {
        Browser browser = parent.getClientDocumentStful().getBrowser();
        if (browser instanceof BrowserOpera)
        {
            if (browser instanceof BrowserOpera)
                return ResponseDelegStfulLoadDocByBOperaImpl.createResponseDelegStfulLoadDocByBOpera(parent);
            else
                return null; // No hay más
        }
        else if (browser instanceof BrowserWebKit)
            return new ResponseDelegStfulLoadDocByBWebKitImpl(parent);
        else
            return new ResponseDelegStfulLoadDocByBW3CDefaultImpl(parent);
    }

    public void fillFrameworkScriptFileNamesOfBrowser(LinkedList list)
    {
        // Se redefine en más de un caso (añadiendo otro)
        list.add(LoadScriptImpl.ITSNAT_W3C);
    }

    public String getJSMethodInitName()
    {
        // Se redefine en un caso
        return "itsnat_init_w3c";
    }
}
