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

package org.itsnat.impl.core.jsren.dom.node.html.msie;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.jsren.dom.node.PropertyImpl;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLPropertyMSIEPocketImpl extends JSRenderHTMLPropertyMSIEOldImpl
{
    public static final JSRenderHTMLPropertyMSIEPocketImpl SINGLETON = new JSRenderHTMLPropertyMSIEPocketImpl();

    /** Creates a new instance of JSRenderHTMLPropertyMSIEPocketImpl */
    public JSRenderHTMLPropertyMSIEPocketImpl()
    {
        // Añadimos todas las propiedades posibles *con atributo asociado*
        // El objetivo es EVITAR el definir como atributo pues es costosísimo
        // y si tiene propiedad asociada la funcionalidad la hará igual definiendo la propiedad
        // http://msdn.microsoft.com/en-us/library/bb415489.aspx

        // El atributo "class" se define globalmente más arriba, el "style"
        // en otro lugar y el "id" aquí como atributo global

        // La documentación de Microsoft es incompleta, muchos atributos
        // tienen propiedades asociadas, la forma de saberlo es ejecutar
        // (no hace falta definir los atributos, las props. siempre están):
        /*
            var elem = ...;
            var exclude =
                {"childNodes":true,"children":true,"className":true,
                 "currentStyle":true,"document":true,"form":true,"id":true,"innerHTML":true,
                 "innerText":true,"offsetHeight":true,"offsetLeft":true,
                 "offsetParent":true,"offsetTop":true,"offsetWidth":true,
                 "parentElement":true,"parentNode":true,"style":true,"tagName":true};

            var obj = new Array();
            for(var name in elem)
            {
                if (exclude[name]) continue;
                obj[obj.length] = name;
            }
            obj.sort(); // ordena alfabeticamente
            var str = "";
            for(var i = 0; i < obj.length; i++) { str += obj[i] + ","; }
            alert(str);
         */

        // Globales
        addGlobalProperty("id",PropertyImpl.STRING);

        // Por tags:

        addProperty("a","accessKey","accesskey",PropertyImpl.STRING);
        addProperty("a","href",PropertyImpl.STRING);
        addProperty("a","name",PropertyImpl.STRING);
        addProperty("a","target",PropertyImpl.STRING);

        // applet: aunque tiene attribs accessKey y name, no son vistos como propiedades

        addProperty("area","accessKey","accesskey",PropertyImpl.STRING);
        addProperty("area","href",PropertyImpl.STRING);
        addProperty("area","name",PropertyImpl.STRING); // No documentado
        addProperty("area","target",PropertyImpl.STRING);

        addProperty("body","aLink","alink",PropertyImpl.STRING); // No es alinkColor, la doc. oficial está mal. El problema es que ignora el cambio de valor si ya fue definido como atributo (si no fue definido sí vale)
        // "background" existe pero no funciona y no documentado por Microsoft
        addProperty("body","bgColor","bgcolor",PropertyImpl.STRING);
        addProperty("body","bottomMargin","bottommargin",PropertyImpl.STRING); // El atributo no está documentado pero existe (deducido por la propiedad y por los demás similares)
        addProperty("body","leftMargin","leftmargin",PropertyImpl.STRING);
        addProperty("body","link",PropertyImpl.STRING); // Ver notas sobre aLink
        // "noWrap" existe pero no funciona y no documentado por Microsoft
        addProperty("body","rightMargin","rightmargin",PropertyImpl.STRING);
        // "scroll" existe pero no funciona y no documentado por Microsoft
        addProperty("body","text",PropertyImpl.STRING);
        addProperty("body","topMargin","topmargin",PropertyImpl.STRING);
        addProperty("body","vLink","vlink",PropertyImpl.STRING); // Ver notas sobre aLink

        addProperty("button","accessKey","accesskey",PropertyImpl.STRING);
        addProperty("button","disabled",PropertyImpl.BOOLEAN);
        // "name" existe pero es sólo lectura
        // "type" existe pero es sólo lectura
        // "value" ya está definido en clases superiores

        addProperty("div","align",PropertyImpl.STRING); // Parece mentira pero funciona

        addProperty("form","action",PropertyImpl.STRING);
        addProperty("form","encoding","enctype",PropertyImpl.STRING);
        addProperty("form","method",PropertyImpl.STRING);
        // "name" existe pero es sólo lectura
        addProperty("form","target",PropertyImpl.STRING);

        // frame: no evaluado
        // frameset: no evaluado

        // addProperty("iframe","align",PropertyImpl.STRING); Lástima, no funciona como propiedad (no documentado por Microsoft)
        // hspace y vspace existen como propiedades en iframe pero como atributos no hacen nada (no documentados)

        // <img>:
        // "alt" es solo lectura
        // idem "border", solo lectura.
        addProperty("img","height",PropertyImpl.INTEGER);
        // hspace es solo lectura
        addProperty("img","src",PropertyImpl.STRING);
        // vspace es solo lectura
        addProperty("img","width",PropertyImpl.INTEGER);

        // <input>: consideramos la "suma" de todos los subtipos según "type"
        addProperty("input","accessKey","accesskey",PropertyImpl.STRING);
        // "checked" ya está definido en clases superiores
        addProperty("input","disabled",PropertyImpl.BOOLEAN);
        // "name" existe pero es sólo lectura
        addProperty("input","readOnly","readonly",PropertyImpl.BOOLEAN); // El atributo no está documentado (por ej. en type=text") pero es existe
        // "size" existe pero es sólo lectura
        // "type" existe pero es sólo lectura
        // "value" ya está definido en clases superiores

        // <option>: selected y value se definen más arriba

        addProperty("select","accessKey","accesskey",PropertyImpl.STRING);
        addProperty("select","disabled",PropertyImpl.BOOLEAN);
        // "multiple" es solo lectura
        // "name" existe pero es sólo lectura
        // "size" es solo lectura
        // Existe la propiedad "value" pero no existe el atributo "value" (si se define no se manifiesta en la propiedad). No vale para nada.

        addProperty("textarea","accessKey","accesskey",PropertyImpl.STRING);
        // "cols" es sólo lectura
        addProperty("textarea","disabled",PropertyImpl.BOOLEAN);
        // "name" es sólo lectura
        addProperty("textarea","readOnly","readonly",PropertyImpl.BOOLEAN);
        // "rows" es sólo lectura
        // "size" es solo lectura
        // "value" ya está definido en clases superiores
    }

    protected String renderProperty(PropertyImpl prop,Element elem,String elemVarName,String attrValueJS,String value,boolean setValue,ClientDocumentStfulImpl clientDoc)
    {
        if (prop.getType() == PropertyImpl.FUNCTION)
        {
            // Los handlers de IE Mobile
            // Si este handler no esta soportado por IE (no esta como atributo) no vale la pena ponerlo como atributo "normal" (muy costoso) pues no hace nada
            String attrName = prop.getAttributeName();
            if (setValue)
                return registerInlineHandler(attrName,attrValueJS,elemVarName);
            else
                return removeInlineHandler(attrName,elemVarName);
        }
        else if ("className".equals(prop.getPropertyName()) &&
                 ((elem instanceof HTMLTableSectionElement) ||
                  (elem instanceof HTMLTableRowElement) ||
                  (elem instanceof HTMLTableCellElement))
                )
        {
            // Necesario ocultar la tabla

            StringBuffer code = new StringBuffer();
            code.append("var dispOldTable;");
            code.append("var table = itsNatDoc.getParentTable(" + elemVarName + ".parentNode);");
            code.append("if (table) dispOldTable = itsNatDoc.hideTable(table);");

            code.append( super.renderProperty(prop,elem,elemVarName,attrValueJS,value,setValue,clientDoc) );

            code.append("if (table) itsNatDoc.showTable(table,dispOldTable);");
            return code.toString();
        }
        else
            return super.renderProperty(prop,elem,elemVarName,attrValueJS,value,setValue,clientDoc);
    }

    private static String registerInlineHandler(String attrName,String jsValue,String elemVarName)
    {
        return elemVarName + " = itsNatDoc.procAttrAsHandler(" + elemVarName + ",\"" + attrName + "\"," + jsValue + ");\n";
    }

    private static String removeInlineHandler(String attrName,String elemVarName)
    {
        // Esto es porque el cambio de atributo cambia el elemento, necesitamos actualizar la referencia al elemento
        return elemVarName + " = itsNatDoc.procAttrAsHandler(" + elemVarName + ",\"" + attrName + "\",null);\n";
    }


}
