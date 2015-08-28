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

package org.itsnat.impl.core.browser;

import org.itsnat.impl.core.browser.droid.BrowserDroid;
import java.io.Serializable;
import org.itsnat.impl.core.browser.web.BrowserWeb;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;

/**
 *
 * @author jmarranz
 */
public abstract class Browser implements Serializable
{
    protected String userAgent;

    /** Creates a new instance of Browser */
    public Browser(String userAgent)
    {
        this.userAgent = userAgent;
    }

    public static Browser createBrowser(ItsNatServletRequestImpl itsNatRequest)
    {
        String userAgent = itsNatRequest.getHeader("User-Agent");

        if (BrowserDroid.isBrowserDroid(userAgent))
            return new BrowserDroid(userAgent);
        else 
            return BrowserWeb.createBrowserWeb(userAgent, itsNatRequest);
    }

    public String getUserAgent()
    {
        return userAgent;
    }


    /* Si usamos una referencia strong para almacenar el referrer.
     * Aplicar cuando no hay garantía de que el nuevo documento se cargue antes del unload del anterior.
     * Es aconsejable cuando el back/forward es cacheado en el cliente por ejemplo
     * porque al volver a una página cacheada en el cliente puede perderse el documento
     * anterior via unload y no cargarse antes el nuevo documento.
     */
    public abstract boolean isReferrerReferenceStrong();

    /*
     * Si necesita URL absolutos para requests AJAX o en carga de scripts con <script>
     */
    public boolean isNeededAbsoluteURL()
    {
        // Se redefine en un par de casos
        return false;
    }
 
}
