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

package org.itsnat.impl.core.browser.web;

import org.itsnat.impl.core.scriptren.jsren.node.html.JSRenderHTMLElementImpl;
import org.itsnat.impl.core.scriptren.jsren.node.html.w3c.JSRenderHTMLElementBatikImpl;
import org.itsnat.impl.core.scriptren.jsren.node.html.w3c.JSRenderHTMLElementOperaOldImpl;

/**
 * En un applet Batik el user agent es configurable pero el que propone
 * por defecto tiene el formato "Batik/..."
 * Por ejemplo el applet envía por defecto (se puede cambiar):
 *  "Batik/development.build"
 *
 * @author jmarranz
 */
public class BrowserBatik extends BrowserSVGPlugin 
{

    /** Creates a new instance of BrowserUnknown */
    public BrowserBatik(String userAgent)
    {
        super(userAgent);

        this.browserType = BATIK;
    }

    public static boolean isBatik(String userAgent)
    {
        return (userAgent.contains("Batik"));
    }


    @Override
    public boolean isNeededAbsoluteURL()
    {
        // El XMLHttpRequest lo necesita
        return true;
    }
    

    @Override
    public JSRenderHTMLElementImpl getJSRenderHTMLElementSingleton() 
    {
        return getJSRenderHTMLElementSingletonStatic(); 
    }    
    
    public static JSRenderHTMLElementImpl getJSRenderHTMLElementSingletonStatic() 
    {
        return JSRenderHTMLElementBatikImpl.SINGLETON; 
    }    
}
