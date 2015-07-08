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

import org.itsnat.impl.core.scriptren.jsren.node.PropertyImpl;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.web.BrowserMSIEOld;
import org.itsnat.impl.core.browser.web.BrowserW3C;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.itsnat.impl.core.scriptren.jsren.node.JSRenderPropertyImpl;
import org.itsnat.impl.core.scriptren.jsren.node.html.msie.JSRenderHTMLPropertyMSIEOldImpl;
import org.itsnat.impl.core.scriptren.jsren.node.html.w3c.JSRenderHTMLPropertyW3CImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderHTMLPropertyImpl extends JSRenderPropertyImpl
{
    /** Creates a new instance of JSRenderHTMLPropertyImpl */
    public JSRenderHTMLPropertyImpl()
    {
        // http://msdn.microsoft.com/library/default.asp?url=/workshop/author/dhtml/reference/properties.asp

        // Propiedades que deben definirse (al mismo tiempo que el atributo)
        // para que el control visual se entere. 
        // Son propiedades que cambian por acciones del usuario.
        // Cuando se está creando el elemento no se necesita pues el valor inicial del control
        // se obtiene por el atributo, pero más tarde cuando el control
        // ya se ha visualizado el cambio del atributo no se manifiesta visualmente
        // se ha de cambiar la propiedad.

        //PropertyImpl.addProperty("select",  "selectedIndex",INTEGER);
        addProperty("option",  "selected",PropertyImpl.BOOLEAN);
        addProperty("option",  "value",   PropertyImpl.STRING);
        addProperty("input",   "checked", PropertyImpl.BOOLEAN);
        addProperty("input",   "value",   PropertyImpl.STRING);
        addProperty("textarea","value",   PropertyImpl.STRING);

        // Preparado para añadir más propiedades aquí
    }

    public static JSRenderHTMLPropertyImpl getJSRenderHTMLProperty(Browser browser)
    {
        // En un caso devuelve nulo.
        if (browser instanceof BrowserMSIEOld)
            return JSRenderHTMLPropertyMSIEOldImpl.getJSRenderHTMLPropertyMSIEOld((BrowserMSIEOld)browser);
        else
            return JSRenderHTMLPropertyW3CImpl.getJSRenderHTMLPropertyW3C((BrowserW3C)browser);
    }

    protected String renderProperty(PropertyImpl prop,Element elem,String elemVarName,String attrValueJS,String value,boolean setValue,ClientDocumentStfulDelegateImpl clientDoc)
    {
        if (DOMUtilHTML.isHTMLInputFileValueAttr(elem, prop.getPropertyName())) // Yo creo que ya no se llega nunca aquí, pues se evita antes.
        {
            // Esto es porque no se puede cambiar la propiedad "value" de un <input type="file">
            // por JavaScript por motivos de seguridad, en MSIE es ignorado y en FireFox da un error.
            // Si evitamos la operación permitimos que funcione bien el control remoto cuando
            // el visor remoto intenta actualizar el estado (value) del control del control observado
            return "";
        }
        else return super.renderProperty(prop, elem, elemVarName, attrValueJS, value, setValue, clientDoc);
    }

}
