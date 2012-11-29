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

package org.itsnat.impl.core.jsren.dom.event.domstd.w3c;

import org.itsnat.impl.core.browser.webkit.BrowserWebKit;
import org.itsnat.impl.core.event.client.domstd.w3c.WebKitKeyEventImpl;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderW3CKeyEventWebKitImpl extends JSRenderW3CKeyboardEventImpl
{
    /**
     * Creates a new instance of JSRenderW3CKeyEventWebKitDefaultImpl
     */
    public JSRenderW3CKeyEventWebKitImpl()
    {
    }

    public static JSRenderW3CKeyEventWebKitImpl getJSRenderW3CKeyEventWebKit(BrowserWebKit browser)
    {
        if (browser.isOldEventSystem())
            return JSRenderW3CKeyEventWebKitOldImpl.SINGLETON;
        else
            return JSRenderW3CKeyEventWebKitDefaultImpl.SINGLETON;
    }

    public String toKeyIdentifierByBrowser(int keyCode)
    {
        return WebKitKeyEventImpl.toKeyIdentifier(keyCode);
    }

}
