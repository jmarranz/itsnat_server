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

import org.itsnat.impl.core.resp.shared.*;
import org.itsnat.impl.core.browser.opera.BrowserOpera9Mobile;

/**
 *
 * @author jmarranz
 */
public class ResponseDelegStfulLoadDocByBOpera9MobileImpl extends ResponseDelegStfulLoadDocByBOpera9Impl
{
    public ResponseDelegStfulLoadDocByBOpera9MobileImpl(ResponseDelegateStfulLoadDocImpl parent)
    {
        super(parent);
    }

    public String getServletPathForEvents()
    {
        String path = super.getServletPathForEvents();

        BrowserOpera9Mobile browser = (BrowserOpera9Mobile)parent.getClientDocumentStful().getBrowser();
        if (browser.isCookiesNotSaved() && (path.toLowerCase().indexOf(";jsessionid=") == -1))
        {
            // Al parecer no se pasa nunca por aquí porque el servidor Tomcat 5.5 al menos
            // detecta que el cliente no acepta cookies (aunque cookies esté "enabled")
            // y añade el jsessionid. Pero así tenemos la seguridad de que el session ID se envía
            // en el caso de otros servlet containers.

            // Ver notas en isCookiesNotSaved()
            // Forzamos el que el URL tenga el ";jsessionid=..."
            // El nombre "jsessionid" es MUY estándar (también en mayúsculas), es el usado (reconocido) por defecto
            // en Jetty, Tomcat, Caucho etc
            String standardId = parent.getResponseLoadDoc().getRequestLoadDoc().getItsNatSession().getStandardSessionId();
            path = path + ";jsessionid=" + standardId;
        }
        return path;
    }
}
