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

package org.itsnat.impl.core.browser.opera;

import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.browser.*;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;

/**
  En Opera el unload no se dispara siempre y onbeforeunload no está definido
  http://www.quirksmode.org/bugreports/archives/2004/11/load_and_unload.html

 * @author jmarranz
 */
public abstract class BrowserOpera extends BrowserW3C
{
    public static final int OPERA_DESKTOP = 1;
    public static final int OPERA_MINI = 2;
    public static final int OPERA_MOBILE_8 = 3;
    public static final int OPERA_MOBILE_9 = 4;

    /** Creates a new instance of BrowserOpera */
    public BrowserOpera(String userAgent)
    {
        super(userAgent);

        this.browserType = OPERA;
    }

    public static BrowserOpera createBrowserOpera(String userAgent)
    {
        if (BrowserOpera9Mini.isOperaMini(userAgent))
            return new BrowserOpera9Mini(userAgent);
        else if (BrowserOpera8Mobile.isOpera8Mobile(userAgent))
            return new BrowserOpera8Mobile(userAgent);
        else if (BrowserOpera9Mobile.isOperaMobile9(userAgent))
            return new BrowserOpera9Mobile(userAgent);
        else
            return new BrowserOpera9Desktop(userAgent);
    }

    public static boolean isOpera(String userAgent,ItsNatServletRequestImpl itsNatRequest)
    {
        // Hay que tener en cuenta que UCWEB puede simular ser un Opera
        return (userAgent.indexOf("Opera") != -1) &&
                !BrowserGeckoUCWEB.isUCWEB(userAgent,itsNatRequest);
    }

    public boolean hasBeforeUnloadSupport(ItsNatStfulDocumentImpl itsNatDoc)
    {
        return false;
    }

    public boolean isReferrerReferenceStrong()
    {
        // El back/forward está cacheado en el cliente.
        return true;
    }

    public boolean isCachedBackForward()
    {
        // Opera Mini: sólo tiene Back
        return true;
    }

    public boolean isSetTimeoutSupported()
    {
        /* Incluso en Opera Mini (proxy) funciona bien, obviamente ejecutado
         en el servidor, por lo menos hasta 10 segundos (no testeado más)
         */
        return true;
    }

    public boolean isTextAddedToInsertedHTMLScriptNotExecuted()
    {
        return false;
    }

    public boolean isClientWindowEventTarget()
    {
        return true;
    }
}
