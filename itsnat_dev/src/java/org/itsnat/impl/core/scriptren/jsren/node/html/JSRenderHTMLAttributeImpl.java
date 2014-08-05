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

package org.itsnat.impl.core.scriptren.jsren.node.html;

import org.itsnat.impl.core.browser.web.BrowserMSIEOld;
import org.itsnat.impl.core.browser.web.BrowserW3C;
import org.itsnat.impl.core.browser.web.BrowserWeb;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.itsnat.impl.core.scriptren.jsren.node.JSRenderAttributeImpl;
import org.itsnat.impl.core.scriptren.jsren.node.html.msie.JSRenderHTMLAttributeMSIEOldImpl;
import org.itsnat.impl.core.scriptren.jsren.node.html.w3c.JSRenderHTMLAttributeW3CImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderHTMLAttributeImpl extends JSRenderAttributeImpl
{

    /** Creates a new instance of JSRenderHTMLAttributeImpl */
    public JSRenderHTMLAttributeImpl()
    {
    }

    public static JSRenderHTMLAttributeImpl getJSRenderHTMLAttribute(BrowserWeb browser)
    {
        // Evitamos así buscar el render una y otra vez pues hay muchos navegadores.
        JSRenderHTMLAttributeImpl render = browser.getJSRenderHTMLAttribute();
        if (render != null)
            return render;

        if (browser instanceof BrowserMSIEOld)
            render = JSRenderHTMLAttributeMSIEOldImpl.getJSRenderHTMLAttributeMSIEOld((BrowserMSIEOld)browser);
        else
            render = JSRenderHTMLAttributeW3CImpl.getJSRenderHTMLAttributeW3C((BrowserW3C)browser);

        browser.setJSRenderHTMLAttribute(render);
        return render;
    }

    public boolean isIgnored(Attr attr,Element elem)
    {
        // En el caso de <input type="file"> el atributo/propiedad "value" es una
        // propiedad prohibida por motivos de seguridad, sobre todo la propiedad.
        // En MSIE el cambiar la propiedad es ignorado, en FireFox da error, en ambos
        // el atributo es ignorado.
        // En BlackBerry (JDE 4.6 y 4.7) es peor aún pues la sola presencia del atributo
        // value en el tag provoca una excepción, es decir <input type="file" value="" />
        // provoca una excepción al cargar la página aunque no se haga nada con JavaScript.
        
        return DOMUtilHTML.isHTMLInputFileValueAttr(elem,attr.getName());
    }
}
