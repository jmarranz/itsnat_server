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

import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.browser.webkit.BrowserWebKit;

/**
 * Vale para FireFox +1 y Minimo +0.2
 *
 * Versiones de Gecko: http://developer.mozilla.org/En/Gecko
 *
 * Ejemplos de user agent:
 * FireFox  2.0:   Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.16) Gecko/20080702 Firefox/2.0.0.16
 * FireFox  3.0:   Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.0.1) Gecko/2008070208 Firefox/3.0.1
 *          3.5.2: Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2
 * Minimo   0.2: Mozilla/5.0 (Windows; U; Windows CE 5.2; rv:1.8.1.4pre) Gecko/20070327 Minimo/0.020
 * Fennec 1.0a1: Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.1b2pre) Gecko/20081015 Fennec/1.0a1
 *               Mozilla/5.0 (Windows; U; Windows CE 5.2; en-US; rv:1.9.2a1pre) Gecko/20090513 Fennec/1.0a1
 *        1.0a2: Mozilla/5.0 (Windows; U; Windows CE 5.2; en-US; rv:1.9.2a1pre) Gecko/20090626 Fennec/1.0a2
 * SkyFire  0.8: Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.17) Gecko/20080829 Firefox/2.0.0.17
 * SkyFire  1.0: Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.20) Gecko/20080829 Firefox/2.0.0.20
 * Moblin2  0.0: Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2a1pre) Gecko/20090518 Firefox/3.5 MoblinWebBrowser/0.0
 *
 * Recordar que SkyFire está basado en servidor.
 * El Moblin Browser no tiene nada en particular respecto al FireFox 3.x, no lo
 * identificamos porque ni siquiera podemos considerarlo un navegador móvil
 * porque un Intel Atom es un procesador decente.
 *
 * @author jmarranz
 */
public abstract class BrowserGecko extends BrowserW3C
{
    protected static final int DESKTOP = 1;
    protected static final int MINIMO = 2;
    protected static final int FENNEC = 3;
    protected static final int SKYFIRE = 4;
    protected static final int UCWEB = 5;

    protected float geckoVersion;

    /** Creates a new instance of BrowserGecko */
    public BrowserGecko(String userAgent)
    {
        super(userAgent);

        this.browserType = GECKO;
    }

    public static BrowserGecko createBrowserGecko(String userAgent,ItsNatServletRequestImpl itsNatRequest)
    {
        if (BrowserGeckoUCWEB.isUCWEB(userAgent,itsNatRequest))
            return new BrowserGeckoUCWEB(userAgent);
        else
            return new BrowserGeckoOther(userAgent,itsNatRequest);
    }

    public static boolean isGecko(String userAgent,ItsNatServletRequestImpl itsNatRequest)
    {
        if (BrowserGeckoUCWEB.isUCWEB(userAgent,itsNatRequest))
            return true;
        else
        {
            // Los navegadores WebKit y NetFront incluyen en userAgent la palabra Gecko.
            // No usamos la palabra "FireFox" para soportar Mozilla y Camino también
            return (userAgent.indexOf("Gecko") != -1) &&
                    !BrowserWebKit.isWebKit(userAgent) &&
                    !BrowserNetFront.isNetFront(userAgent);
        }
    }

    public boolean isMobile()
    {
        return (browserSubType != DESKTOP);
    }

    public boolean isSkyFire()
    {
        return (browserSubType == SKYFIRE);
    }

    public boolean isTextAddedToInsertedHTMLScriptNotExecuted()
    {
        return false;
    }

    public boolean isInsertedSVGScriptNotExecuted()
    {
        return false; 
    }

    public boolean isTextAddedToInsertedSVGScriptNotExecuted()
    {
        return false;
    }

    public boolean canNativelyRenderOtherNSInXHTMLDoc()
    {
        return true; // SVG y MathML al menos
    }

    public boolean isClientWindowEventTarget()
    {
        return true;
    }
}
