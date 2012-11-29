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

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLElementMSIE9Impl extends JSRenderHTMLElementW3CImpl
{
    public static final JSRenderHTMLElementMSIE9Impl SINGLETON = new JSRenderHTMLElementMSIE9Impl();

    /** Creates a new instance of JSMSIEHTMLElementRenderImpl */
    public JSRenderHTMLElementMSIE9Impl()
    {
        // A partir de los no permitidos en MSIE probados uno a uno
        // Los comentados es que sí funcionan con innerHTML (aunque no tenga sentido usarlo)

        tagNamesWithoutInnerHTML.add("table");
        tagNamesWithoutInnerHTML.add("tbody");
        tagNamesWithoutInnerHTML.add("tfoot");
        tagNamesWithoutInnerHTML.add("thead");
        tagNamesWithoutInnerHTML.add("tr");

        tagNamesWithoutInnerHTML.add("col");
        tagNamesWithoutInnerHTML.add("colgroup");
        tagNamesWithoutInnerHTML.add("frameset"); // No probado realmente
        tagNamesWithoutInnerHTML.add("html");
        // tagNamesWithoutInnerHTML.add("style");
        // tagNamesWithoutInnerHTML.add("title");
        // tagNamesWithoutInnerHTML.add("script");
        tagNamesWithoutInnerHTML.add("select"); // Admite innerHTML pero no lo hace bien, considera los </option> como elementos
        tagNamesWithoutInnerHTML.add("pre");  // Leer notas para MSIE Old, yo creo que funciona bien pero por prudencia
        tagNamesWithoutInnerHTML.add("textarea"); // Leer notas para MSIE Old, en el caso de IE 9 confirmado que sigue funcionando mal

        // tagNamesWithoutInnerHTML.add("iframe"); // Esta la descubrí yo, no admite siquiera un ""
        // tagNamesWithoutInnerHTML.add("img"); // Idem
        tagNamesWithoutInnerHTML.add("input"); // Idem

        // tagNamesWithoutInnerHTML.add("object"); // Esta la descubrí yo
        // tagNamesWithoutInnerHTML.add("applet"); // Esta la descubrí yo

        //---------------------------------------------------
        // TagNames no válidos DENTRO de un innerHTML
        //---------------------------------------------------

        /* En MSIE 9 el <script> NO es ejecutado via innerHTML seguramente por razones de seguridad
       */
        tagNamesNotValidInsideInnerHTML.add("script");

        // Con <style> dentro de innerHTML el MSIE 9 lo filtra, no aparece en el DOM.
        tagNamesNotValidInsideInnerHTML.add("style");
    }

    public String getCurrentStyleObject(String itsNatDocVar,String elemName,ClientDocumentStfulImpl clientDoc)
    {
        return itsNatDocVar + ".win.getComputedStyle(" + elemName + ", null)";
    }

}

