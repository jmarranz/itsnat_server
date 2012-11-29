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
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLElementNetFrontImpl extends JSRenderHTMLElementW3CImpl
{
    public static final JSRenderHTMLElementNetFrontImpl SINGLETON = new JSRenderHTMLElementNetFrontImpl();

    /** Creates a new instance of JSMSIEHTMLElementRenderImpl */
    public JSRenderHTMLElementNetFrontImpl()
    {
        // A partir de los no permitidos en MSIE probados uno a uno

        tagNamesWithoutInnerHTML.add("table");
        tagNamesWithoutInnerHTML.add("tbody");
        tagNamesWithoutInnerHTML.add("tfoot");
        tagNamesWithoutInnerHTML.add("thead");
        tagNamesWithoutInnerHTML.add("tr");
        tagNamesWithoutInnerHTML.add("col"); // Funciona bien pero tiene poco sentido usar innerHTML pues debería estar vacío
        tagNamesWithoutInnerHTML.add("colgroup");
        tagNamesWithoutInnerHTML.add("html");
        tagNamesWithoutInnerHTML.add("select");

        // No probado: tagNamesWithoutInnerHTML.add("frameset");

        /* Caso <style> dentro de innerHTML: funciona bien y el estilo se aplica.
         * Es más, via DOM el estilo NO se aplica.
         */
    }

    protected String createElement(Element nodeElem,String tagName,ClientDocumentStfulImpl clientDoc)
    {
        if (DOMUtilHTML.isHTMLSelectMultipleOrWithSize(nodeElem))
        {
            StringBuffer code = new StringBuffer();
            code.append("function(itsNatDoc)");
            code.append("{");
            code.append("  var span = itsNatDoc.doc.createElement(\"span\");");
            code.append("  span.style.display = \"none\";");
            code.append("  span.innerHTML=\"<select");
            if (nodeElem.hasAttribute("multiple"))
                code.append(" multiple='" + nodeElem.getAttribute("multiple") + "'");
            if (nodeElem.hasAttribute("size"))
                code.append(" size='" + nodeElem.getAttribute("size") + "'");
            code.append(   "/>\";");
            code.append("  itsNatDoc.doc.body.appendChild(span);");
            code.append("  var select = span.firstChild;");
            code.append("  itsNatDoc.doc.body.removeChild(span);");
            code.append("  return select;");
            code.append("}(itsNatDoc)");
            return code.toString();
        }
        else return super.createElement(nodeElem, tagName,clientDoc);
    }

    public String getCurrentStyleObject(String itsNatDocVar,String elemName,ClientDocumentStfulImpl clientDoc)
    {
        return elemName + ".currentStyle";
    }

}

