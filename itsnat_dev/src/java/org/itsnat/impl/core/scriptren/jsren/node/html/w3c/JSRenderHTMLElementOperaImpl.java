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

package org.itsnat.impl.core.scriptren.jsren.node.html.w3c;

import org.itsnat.impl.core.browser.web.opera.BrowserOpera;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLElementOperaImpl extends JSRenderHTMLElementW3CImpl
{
    public static final JSRenderHTMLElementOperaImpl SINGLETON = new JSRenderHTMLElementOperaImpl();
    
    /** Creates a new instance of JSMSIEHTMLElementRenderImpl */
    public JSRenderHTMLElementOperaImpl()
    {
        // A partir de la lista de MSIE probados uno a uno

        // No probado: tagNamesWithoutInnerHTML.add("frameset");
        tagNamesWithoutInnerHTML.add("textarea");

        /* Caso <style> dentro de un innerHTML: funciona bien y el estilo se aplica en desktop y Opera Mini.
         */        
    }

    public static JSRenderHTMLElementOperaImpl getJSRenderHTMLElementOpera(BrowserOpera browser)
    {
         return JSRenderHTMLElementOperaImpl.SINGLETON;
    }

    @Override    
    public String getCurrentStyleObject(String itsNatDocVar,String elemName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return itsNatDocVar + ".win.getComputedStyle(" + elemName + ", null)";
    }
}

