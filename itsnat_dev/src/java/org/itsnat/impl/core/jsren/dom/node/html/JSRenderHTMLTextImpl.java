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

package org.itsnat.impl.core.jsren.dom.node.html;

import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.BrowserMSIEOld;
import org.itsnat.impl.core.browser.BrowserNetFront;
import org.itsnat.impl.core.jsren.dom.node.JSRenderTextImpl;
import org.itsnat.impl.core.jsren.dom.node.html.msie.JSRenderHTMLTextMSIEOldImpl;
import org.itsnat.impl.core.jsren.dom.node.html.w3c.JSRenderHTMLTextNetFrontImpl;


/**
 *
 * @author jmarranz
 */
public abstract class JSRenderHTMLTextImpl extends JSRenderTextImpl
{
    /**
     * Creates a new instance of JSRenderXMLTextDefaultImpl
     */
    public JSRenderHTMLTextImpl()
    {
    }

    public static JSRenderHTMLTextImpl getJSRenderHTMLText(Browser browser)
    {
        // Evitamos así buscar el render una y otra vez pues hay muchos navegadores.
        JSRenderHTMLTextImpl render = browser.getJSRenderHTMLText();
        if (render != null)
            return render;

        if (browser instanceof BrowserMSIEOld)
            render = JSRenderHTMLTextMSIEOldImpl.getJSRenderHTMLTextMSIEOld((BrowserMSIEOld)browser);
        else if (browser instanceof BrowserNetFront)
            render = JSRenderHTMLTextNetFrontImpl.SINGLETON;
        else
            render = JSRenderHTMLTextDefaultImpl.SINGLETON;

        browser.setJSRenderHTMLText(render);
        return render;
    }

}
