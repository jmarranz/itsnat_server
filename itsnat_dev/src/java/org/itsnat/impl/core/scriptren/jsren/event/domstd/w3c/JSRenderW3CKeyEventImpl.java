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

package org.itsnat.impl.core.scriptren.jsren.event.domstd.w3c;

import org.itsnat.impl.core.browser.web.BrowserBatik;
import org.itsnat.impl.core.browser.web.BrowserBlackBerryOld;
import org.itsnat.impl.core.browser.web.BrowserGecko;
import org.itsnat.impl.core.browser.web.BrowserMSIE9;
import org.itsnat.impl.core.browser.web.opera.BrowserOpera;
import org.itsnat.impl.core.browser.web.BrowserW3C;
import org.itsnat.impl.core.browser.web.webkit.BrowserWebKit;

/**
    Sobre el caos de los key events:
    http://unixpapa.com/js/key.html
    http://www.hallvord.com/opera/keyevents.htm
    http://my.opera.com/hallvors/blog/show.dml/217592
 *
 * @author jmarranz
 */
public abstract class JSRenderW3CKeyEventImpl extends JSRenderW3CUIEventImpl
{

    /** Creates a new instance of JSMouseEventRenderImpl */
    public JSRenderW3CKeyEventImpl()
    {
    }

    public static JSRenderW3CKeyEventImpl getJSW3CKeyEventRender(BrowserW3C browser)
    {
        if (browser instanceof BrowserGecko)
            return JSRenderW3CKeyEventGeckoImpl.SINGLETON;
        else if (browser instanceof BrowserWebKit)
            return JSRenderW3CKeyEventWebKitImpl.getJSRenderW3CKeyEventWebKit((BrowserWebKit)browser);
        else if (browser instanceof BrowserMSIE9)
            return JSRenderW3CKeyEventMSIE9Impl.SINGLETON;
        else if (browser instanceof BrowserBlackBerryOld)
            return JSRenderW3CKeyEventBlackBerryOldImpl.SINGLETON;
        else if (browser instanceof BrowserOpera)
            return JSRenderW3CKeyEventOperaImpl.SINGLETON;
        else if (browser instanceof BrowserBatik)
            return JSRenderW3CKeyEventBatikImpl.SINGLETON;
        else // Desconocido
            return JSRenderW3CKeyEventGeckoImpl.SINGLETON;
    }

}
