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

import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLAttributeW3CDefaultImpl extends JSRenderHTMLAttributeW3CImpl
{
    public final static JSRenderHTMLAttributeW3CDefaultImpl SINGLETON = new JSRenderHTMLAttributeW3CDefaultImpl();

    /** Creates a new instance of JSRenderHTMLAttributeW3CDefaultImpl */
    public JSRenderHTMLAttributeW3CDefaultImpl()
    {
    }

    public boolean isRenderAttributeAlongsideProperty(String attrName,Element elem)
    {
        // Los navegadores W3C distinguen normalmente entre propiedades y atributos
        // pues lo manda el estándar W3C y nuestro compromiso es mantener el DOM cliente sincronizado
        // con el servidor incluso en este caso en donde lo importante es el estado del control en el cliente.

        // En Android de hecho necesitamos el valor del atributo para corregir
        // el bug de la autoselección del primer option de los select multiple
        // pues en el cliente a través del atributo se sabe el valor que debe tener la propiedad

        return true;
    }
}
