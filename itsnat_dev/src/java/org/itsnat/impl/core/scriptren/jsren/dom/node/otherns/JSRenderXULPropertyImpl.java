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

package org.itsnat.impl.core.scriptren.jsren.dom.node.otherns;

import org.itsnat.impl.core.scriptren.jsren.dom.node.PropertyImpl;

/**
 *
 * @author jmarranz
 */
public class JSRenderXULPropertyImpl extends JSRenderOtherNSPropertyImpl
{
    public static final JSRenderXULPropertyImpl SINGLETON = new JSRenderXULPropertyImpl();

    /** Creates a new instance of JSRenderHTMLPropertyImpl */
    public JSRenderXULPropertyImpl()
    {
        // En XUL el cambio del atributo suele manifestarse con un cambio visual,
        // pero he descubierto que no siempre ocurre (por ejemplo el value de un textbox, caso documentado),
        // que es más seguro a través de la propiedad.
        // Consideramos sólo los atributos/propiedades que cambian por acciones del usuario
        // que yo creo que son las suceptibles a ignorar el cambio de estado via únicamente
        // cambio del atributo.
        // https://developer.mozilla.org/en/XUL_Reference
        // Conviene ver estos atributos/propiedades a través de:
        // https://developer.mozilla.org/en/XUL/Attribute
        // pues para cada atributo te viene los elementos que lo usan (ej. value el cual está presente en todos los
        // elementos pero sólo significativo visualmente en unos pocos)

        // Controls:
        addProperty("button",       "checkState",PropertyImpl.INTEGER);
        addProperty("button",       "checked",  PropertyImpl.BOOLEAN); // Caso de type=checkbox y radio  el caso es que dichos tipos no cambian nada (?)
        addProperty("checkbox",     "checked",  PropertyImpl.BOOLEAN);
        addProperty("colorpicker",  "color",    PropertyImpl.STRING);
        addProperty("datepicker",   "value",    PropertyImpl.STRING);
        addProperty("listitem",     "selected", PropertyImpl.BOOLEAN);
        addProperty("menulist",     "label",    PropertyImpl.BOOLEAN);  // Cuando es editable
        addProperty("menuitem",     "checked",  PropertyImpl.BOOLEAN);
        // menuitem: selected es read-only, debe cambiarse via selectedIndex o selectedItem en el padre
        addProperty("prefpane",     "selected", PropertyImpl.BOOLEAN);
        addProperty("progressmeter","value",    PropertyImpl.INTEGER); // OJO es un entero
        addProperty("radio",        "selected", PropertyImpl.BOOLEAN);
        addProperty("richlistitem", "selected", PropertyImpl.BOOLEAN);
        addProperty("textbox",      "value",    PropertyImpl.STRING);
        addProperty("tab",          "selected", PropertyImpl.BOOLEAN);
        addProperty("timepicker",   "value",    PropertyImpl.STRING);
        addProperty("toolbarbutton","checkState",PropertyImpl.INTEGER);
        addProperty("toolbarbutton","checked",  PropertyImpl.BOOLEAN); // Caso de type=checkbox y radio  el caso es que dichos tipos no cambian nada (?)

        // Habría que estudiar el caso de los tree cells "editable" aunque
        // yo creo que el cambio no se puede obtener con un simple atributo de un elemento (hay que obtenerlo via JavaScript).
        // Estudiar también el drag-drop de columnas etc

        // Añadir más propiedades aquí
    }

}
