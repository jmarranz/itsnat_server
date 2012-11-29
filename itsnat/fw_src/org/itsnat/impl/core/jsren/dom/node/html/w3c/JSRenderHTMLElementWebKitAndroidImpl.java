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

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLElementWebKitAndroidImpl extends JSRenderHTMLElementWebKitImpl
{
    public static final JSRenderHTMLElementWebKitAndroidImpl SINGLETON = new JSRenderHTMLElementWebKitAndroidImpl();

    /** Creates a new instance of JSMSIEHTMLElementRenderImpl */
    public JSRenderHTMLElementWebKitAndroidImpl()
    {
        // El select con atributo "multiple" (v1 r2 y anteriores) tiene problemas de que se selecciona
        // el primer elemento por defecto, por ello evitamos que se use innerHTML y así asegurar
        // que todas las opciones se insertan de la misma forma (pues se ha detectado que si el primer elemento
        // se inserta con innerHTML parece que no ocurre la autoselección que curiosamente
        // ocurre con el segundo si se inserta no usando innerHTML)
        // Aunque esto no ocurre en versiones superiores (v2) nos da igual que no podamos usar innerHTML
        this.tagNamesWithoutInnerHTML.add("select");
    }
}

