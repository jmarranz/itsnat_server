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

package org.itsnat.impl.comp.layer;

import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.BrowserGeckoUCWEB;
import org.itsnat.impl.core.browser.BrowserMSIEPocket;
import org.itsnat.impl.core.browser.opera.BrowserOpera9Mini;
import org.itsnat.impl.core.browser.webkit.BrowserWebKitBolt;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;

/**
 * Pocket IE: no admite posicionamiento absoluto ni z-index por lo que usamos la técnica
 * de ocultar los elementos bajo body ANTES de que el programador añada la "vista modal".
 * En este caso el elemento layer modal no sirve para nada pero es necesario para los demás
 * navegadores en el caso de control remoto.
 *
 * Opera Mini: admite posicionamiento absoluto pero los elementos ocultos son pulsables.
 *
 * @author jmarranz
 */
public class ItsNatModalLayerClientDocHTMLEverCleanImpl extends ItsNatModalLayerClientDocHTMLImpl
{
    public ItsNatModalLayerClientDocHTMLEverCleanImpl(ItsNatModalLayerHTMLImpl comp,ClientDocumentStfulImpl clientDoc)
    {
        super(comp,clientDoc);
    }

    public static boolean isEverCleanNeeded(Browser browser)
    {
        return (browser instanceof BrowserMSIEPocket)||
               (browser instanceof BrowserOpera9Mini)||
               (browser instanceof BrowserGeckoUCWEB)||
               (browser instanceof BrowserWebKitBolt);
    }

    public boolean isCleanBelowMode()
    {
        // Redefinimos, es siempre "clean"
        return true;
    }

    public void initModalLayer()
    {
        ClientDocumentStfulImpl clientDoc = getClientDocumentStful();
        Browser browser = clientDoc.getBrowser();
        if ((browser instanceof BrowserMSIEPocket)||
            (browser instanceof BrowserGeckoUCWEB))
            return; // No sirve para nada todo esto en este caso, no soporta posicionamiento absoluto
        else
            super.initModalLayer();
    }

    public void preRemoveLayer()
    {
        ClientDocumentStfulImpl clientDoc = getClientDocumentStful();
        Browser browser = clientDoc.getBrowser();
        if ((browser instanceof BrowserMSIEPocket)||
            (browser instanceof BrowserGeckoUCWEB))
            return; // No sirve para nada en este caso
        else
            super.preRemoveLayer();
    }

}
