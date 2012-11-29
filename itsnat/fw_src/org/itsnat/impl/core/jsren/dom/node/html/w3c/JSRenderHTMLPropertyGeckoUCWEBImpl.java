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

package org.itsnat.impl.core.jsren.dom.node.html.w3c;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.itsnat.impl.core.jsren.dom.node.PropertyImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLPropertyGeckoUCWEBImpl extends JSRenderHTMLPropertyW3CImpl
{
    public static final JSRenderHTMLPropertyGeckoUCWEBImpl SINGLETON = new JSRenderHTMLPropertyGeckoUCWEBImpl();

    /** Creates a new instance of JSRenderHTMLPropertyW3CDefaultImpl */
    public JSRenderHTMLPropertyGeckoUCWEBImpl()
    {
    }

    protected String renderProperty(PropertyImpl prop,Element elem,String elemVarName,String attrValueJS,String value,boolean setValue,ClientDocumentStfulImpl clientDoc)
    {
        if (DOMUtilHTML.isHTMLTextAreaOrInputTextBox(elem) &&
            "value".equals(prop.getPropertyName())) // No puede ser otra propiedad pero por si acaso y por clarida
        {
            // Esto sirve para evitar que un cambio de la propiedad value desde el servidor via
            // JavaScript suponga que itsnat_value tenga un valor diferente, provocando
            // un falso cambio del valor por parte del usuario.
            StringBuffer code = new StringBuffer();
            code.append( super.renderProperty(prop, elem, elemVarName, attrValueJS, value, setValue, clientDoc) );
            code.append( elemVarName + ".itsnat_value = " + elemVarName + ".value;\n" );
            return code.toString();
        }
        else
            return super.renderProperty(prop, elem, elemVarName, attrValueJS, value, setValue, clientDoc);
    }
}
