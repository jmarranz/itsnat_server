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
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLAttributeWebKitMotoImpl extends JSRenderHTMLAttributeW3CImpl
{
    public final static JSRenderHTMLAttributeWebKitMotoImpl SINGLETON = new JSRenderHTMLAttributeWebKitMotoImpl();

    /** Creates a new instance of JSW3CDefaultHTMLAttributeRenderImpl */
    public JSRenderHTMLAttributeWebKitMotoImpl()
    {
    }

    public static boolean isHTMLInputCheckBoxCheckedProp(String attrName,Element elem)
    {
        return DOMUtilHTML.isHTMLInputCheckBox(elem) &&
               attrName.toLowerCase().equals("checked");
    }

    public boolean isRenderAttributeAlongsideProperty(String attrName,Element elem)
    {
        if (isHTMLInputCheckBoxCheckedProp(attrName,elem))
        {
            // La propiedad "checked" de <input type="checkbox"> no se distingue del atributo
            // esto curiosamente NO ocurre con otras propiedades de elementos form.
            return false;
        }
        return true;
    }

    protected String setAttributeWithProperty(Attr attr,String attrName,String valueJS,Element elem,String elemVarName,boolean newElem,PropertyImpl prop,ClientDocumentStfulImpl clientDoc)
    {
        if (isHTMLInputCheckBoxCheckedProp(attrName,elem))
        {
            // El elemento <input type="checkbox"> es muy raro en MotoWebKit, por una parte
            // el evento click no es emitido y sin embargo el change sí. Por ello usamos
            // el change para disparar un evento click. Sin embargo he descubierto que
            // la modificación de la propiedad "checked" ya sea via propiedad o como atributo (da igual, están erróneamente sincronizados)
            // dispara el evento change (si hay cambio de valor), lo cual crea falsos "clicks"
            // y puede dar lugar que lo que se vea sea diferente al valor guardado en el servidor.
            // Este problema se manifiesta como fallo en la edición in place pues
            // al ppio se pone como true por defecto generando un evento change que no debería.
            // La solución no es sencilla porque al cambiar el checked se encola un evento pero dicho
            // evento NO se despacha hasta que el script termina por lo que no sirven las técnicas de demarcar el antes y después
            // el cambio de propiedad con algún tipo de flag. La solución es usar un flag y un setTimeout(func,0), el cual
            // asegura que es ejecutado DESPUES de despachar el evento, restauramos ahí el flag.

            StringBuffer code = new StringBuffer();
            code.append( elemVarName + ".itsNatFireClick = false;\n" );
            code.append( super.setAttributeWithProperty(attr,attrName,valueJS,elem,elemVarName,newElem,prop,clientDoc) );
            code.append( "var func = function() { arguments.callee.elem.itsNatFireClick = true; };\n" );
            code.append( "func.elem = " + elemVarName + ";\n" );
            code.append( "itsNatDoc.setTimeout(func,0);\n" );
            return code.toString();
        }
        else
            return super.setAttributeWithProperty(attr,attrName,valueJS,elem,elemVarName,newElem,prop,clientDoc);
    }

    protected String removeAttributeWithProperty(Attr attr,String attrName,Element elem,String elemVarName,PropertyImpl prop,ClientDocumentStfulImpl clientDoc)
    {
        if (isHTMLInputCheckBoxCheckedProp(attrName,elem))
        {
            // Leer notas en setAttributeWithProperty
            StringBuffer code = new StringBuffer();
            code.append(elemVarName + ".itsNatFireClick = false;\n");
            code.append( super.removeAttributeWithProperty(attr,attrName,elem,elemVarName,prop,clientDoc) );
            code.append( "var func = function() { arguments.callee.elem.itsNatFireClick = true; };\n" );
            code.append( "func.elem = " + elemVarName + ";\n" );
            code.append( "itsNatDoc.setTimeout(func,0);\n" );
            return code.toString();
        }
        else
            return super.removeAttributeWithProperty(attr,attrName,elem,elemVarName,prop,clientDoc);
    }
}
