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

package org.itsnat.impl.core.browser.droid;

import org.itsnat.impl.core.browser.Browser;
import static org.itsnat.impl.core.browser.Browser.DROID;



/**
 * Normalmente será el caso del cliente HTTP de Android 
 *
 *
 * @author jmarranz
 */
public class BrowserDroid extends Browser
{

    /** Creates a new instance of BrowserUnknown */
    public BrowserDroid(String userAgent)
    {
        super(userAgent);

        this.browserType = DROID;
    }

    public static boolean isBrowserDroid(String userAgent)
    {
        // Tomadas del emulador 4.0.3
        // Con HttpClient: "Apache-HttpClient/UNAVAILABLE (java 1.4)" se le añade ItsNatDroidBrowser al final

        return userAgent.contains("ItsNatDroidBrowser");
    }

    public boolean isReferrerReferenceStrong()
    {
        return true;
    }

    public boolean isClientWindowEventTarget()
    {
        return true; // Por poner algo
    }
    
    @Override
    public boolean isNeededAbsoluteURL()
    {
        return true;
    }    
}
