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

package org.itsnat.impl.core.jsren.dom.node.otherns;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderOtherNSElementW3CSVGPluginImpl extends JSRenderOtherNSElementW3CImpl
{

    /**
     * Creates a new instance of JSRenderOtherNSElementW3CSVGPluginImpl
     */
    public JSRenderOtherNSElementW3CSVGPluginImpl()
    {
    }

    protected String bindBackupAndSetStylePropertyMethod(String methodName,ClientDocumentStfulImpl clientDoc)
    {
        // Redefinimos porque ni Batik applet ni Adobe SVG Viewer soportan el acceso a las propiedades de style directamente
        // (el interface CSS2Properties no está implementado), pero es posible a través de getPropertyValue y setProperty.
        // No es raro esto puesto que Adobe creo que usa Rhino (en C++) al igual que Batik (en Java).
        // El método getPropertyValue nunca devuelve undefined aunque la propiedad
        // sea desconocida (devuelve la cadena vacía) por lo que no es necesario el chequeo del undefined
        // setProperty con un valor de cadena vacía NO hace nada, mejor usar removeProperty
        // setProperty tiene tres parámetros, el tercero es el parámetro "important", en Batik es necesario
        // pues el style no es un ScriptableObject, es el CSSStyleDeclaration nativo, en ASV es opcional,
        // una cadena vacía vale.

        StringBuilder code = new StringBuilder();

        code.append( "var func = function (elem,propName,newValue)" );
        code.append( "{" );
        code.append( "  if (typeof elem.style == \"undefined\") return;"); // Esto ocurre por ejemplo con <script> en algun navegador (no me acuerdo) y con <foreignObject> en SVG en Opera Mobile 9.5.
        code.append( "  var name = \"style_itsnat_\" + propName;" );
        code.append( "  var cssProp = elem.style.getPropertyValue(propName);");
        code.append( "  this.setPropInNative(elem,name,cssProp);");
        code.append( "  if (newValue != \"\") elem.style.setProperty(propName,newValue,\"\");" );
        code.append( "  else elem.style.removeProperty(propName);" );
        code.append( "};" );

        code.append( "itsNatDoc." + methodName + " = func;\n" );

        clientDoc.bindClientMethod(methodName);

        return code.toString();
    }

    protected String bindRestoreBackupStylePropertyMethod(String methodName,ClientDocumentStfulImpl clientDoc)
    {
        StringBuilder code = new StringBuilder();

        code.append( "var func = function (elem,propName)" );
        code.append( "{" );
        code.append( "  if (typeof elem.style == \"undefined\") return;");
        code.append( "  var name = \"style_itsnat_\" + propName;" );
        code.append( "  var cssProp = this.getPropInNative(elem,name);\n");
        code.append( "  if (cssProp == null) return;\n"); // No se salvó
        code.append( "  if (cssProp != \"\") elem.style.setProperty(propName,cssProp,\"\");" );
        code.append( "  else elem.style.removeProperty(propName);" );
        code.append( "  this.removePropInNative(elem,name);\n");
        code.append( "};" );

        code.append( "itsNatDoc." + methodName + " = func;\n" );

        clientDoc.bindClientMethod(methodName);

        return code.toString();
    }

}
