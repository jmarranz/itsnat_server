/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2014 Jose Maria Arranz Santamaria, Spanish citizen

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

package org.itsnat.impl.core.browser.web.webkit;

import java.util.Map;
import org.w3c.dom.html.HTMLElement;

/**
 * Opera desktop y mobile (Android) basado en WebKit (NO en Presto)
 * 
 * Opera Mini sigue funcionando con Presto en el servidor
 * 
 * Ej. de user agent (v26): 
 *  Desktop: Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.65 Safari/537.36 OPR/26.0.1656.32
 *  Android: Mozilla/5.0 (Linux; Android 4.0.3; Transformer TF101 Build/IML74K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.59 Safari/537.36 OPR/26.0.1656.86386
 * 
 * @author jmarranz
 */
public class BrowserWebKitOpera extends BrowserWebKit
{
    protected boolean mobile;
    
    public BrowserWebKitOpera(String userAgent)
    {
        super(userAgent,OPERA);
        this.mobile = userAgent.contains("Android");
    }

    @Override
    public boolean hasBeforeUnloadSupportHTML()
    {
        return !mobile;
    }

    @Override
    public boolean isMobile()
    {
        return mobile;
    }

    @Override
    public boolean isFocusOrBlurMethodWrong(String methodName, HTMLElement formElem)
    {
        return false;
    }

    @Override
    public Map<String, String[]> getHTMLFormControlsIgnoreZIndex()
    {
        return null;
    }
    
}
