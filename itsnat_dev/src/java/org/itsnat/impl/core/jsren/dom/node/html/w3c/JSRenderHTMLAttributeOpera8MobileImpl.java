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
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLAttributeOpera8MobileImpl extends JSRenderHTMLAttributeW3CImpl
{
    public final static JSRenderHTMLAttributeOpera8MobileImpl SINGLETON = new JSRenderHTMLAttributeOpera8MobileImpl();

    /** Creates a new instance of JSW3CDefaultHTMLAttributeRenderImpl */
    public JSRenderHTMLAttributeOpera8MobileImpl()
    {
    }

    public boolean isRenderAttributeAlongsideProperty(String attrName,Element elem)
    {
        // Opera Mobile 8.6x aparentemente distingue atributos de propiedades
        // excepto en el caso al menos de la propiedad/atributo select de <option>
        // En este caso el cambio del atributo via option.setAttribute("selected","selected")
        // supone que al preguntar option.getAttribute("selected") devuelve "true" en vez
        // de "selected" y sin embargo la propiedad selected no ha cambiado (WinCE 8.65 al menos).
        // Yo creo que el fallo es por la presencia inicial del atributo selected="selected" en la carga de la página
        // Opera Mobile lo cambia a ser "true" pero ya no responde al cambio de la propiedad
        // (esto no ocurre si el select fue insertado en la página tras la carga, incluso
        // con atributos selected).
        // En principio no hay problema por este fallo respecto a renderizar el atributo 
        // (sí tiene otras consecuencias que se resuelven en otro lugar)
        // pues visualmente responde a la propiedad, de hecho el renderizar también los atributos
        // hace que funcione mal la v8.60, parece como si el removeAttribute
        // pusiera de nuevo la propiedad selected a "true".
        // Como parece que los atributos no son muy correctos en estos casos
        // evitamos definir también la propiedad además porque este navegador es muy inestable.
        return false;
    }

    public String toJSAttrValue(Attr attr,Element elem,boolean newElem,ClientDocumentStfulImpl clientDoc)
    {
        // Es un error estúpido de Opera Mobile 8.6, la sentencia
        // elem.setAttribute("style","");
        // no hace nada aunque no da error, por tanto el valor anterior de style se conserva,
        // un simple espacio vale para solucionar esto.
        // Nota: verificar que el "equalsIgnoreCase" es necesario (debería ser equals).
        if (attr.getName().equalsIgnoreCase("style") && attr.getValue().equals(""))
            return "\" \"";
        else
            return super.toJSAttrValue(attr,elem,newElem,clientDoc);
    }

}
