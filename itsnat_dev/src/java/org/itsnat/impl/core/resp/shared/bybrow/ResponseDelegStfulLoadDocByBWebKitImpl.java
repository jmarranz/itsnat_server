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
import org.itsnat.impl.core.browser.webkit.BrowserWebKitMoto;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.resp.shared.*;
import org.itsnat.impl.res.core.js.LoadScriptImpl;

/**
 *
 * @author jmarranz
 */
public class ResponseDelegStfulLoadDocByBWebKitImpl extends ResponseDelegStfulLoadDocByBW3CImpl
{
    public ResponseDelegStfulLoadDocByBWebKitImpl(ResponseDelegateStfulLoadDocImpl parent)
    {
        super(parent);
    }

    public String getOnInitScriptContentCodeFixDOMCode()
    {
        return null;
    }

    public boolean getRevertJSChanges()
    {
        // Los navegadores WebKit rellenan los formularios con valores
        // antiguos al volver a la página con un back, incluso cuando se recarga la página desde el servidor
        // (pues la página NO se cachea sin embargo los valores de los formularios SÍ)
        // Esto incluye controles tal y como <input type="button"> que por el usuario no pueden cambiarse
        // pero via código pudieron ser cambiados en el estado último de la página.

        // El S60WebKit no tiene autofill sin embargo el WebKit usado es muy antiguo y es
        // esperable que en versiones futuras lo tenga.
        return true;
    }

    public void fillFrameworkScriptFileNamesOfBrowser(LinkedList list)
    {
        super.fillFrameworkScriptFileNamesOfBrowser(list);

        ClientDocumentStfulImpl clientDoc = parent.getClientDocumentStful();
        Browser browser = clientDoc.getBrowser();
        if (browser instanceof BrowserWebKitMoto)
            list.add(LoadScriptImpl.ITSNAT_MOTOWEBKIT);
    }

    public String getJSMethodInitName()
    {
        ClientDocumentStfulImpl clientDoc = parent.getClientDocumentStful();
        Browser browser = clientDoc.getBrowser();

        if (browser instanceof BrowserWebKitMoto)
            return "itsnat_init_motowebkit";
        else
            return super.getJSMethodInitName();
    }
}
