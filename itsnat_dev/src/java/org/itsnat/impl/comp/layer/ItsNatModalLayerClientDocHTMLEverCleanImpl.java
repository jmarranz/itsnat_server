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
import org.itsnat.impl.core.browser.opera.BrowserOpera9Mini;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;

/**
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
        return (browser instanceof BrowserOpera9Mini);
    }

    @Override
    public boolean isCleanBelowMode()
    {
        // Redefinimos, es siempre "clean"
        return true;
    }
}
