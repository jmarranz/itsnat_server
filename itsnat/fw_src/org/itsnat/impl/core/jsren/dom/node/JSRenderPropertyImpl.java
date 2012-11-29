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

package org.itsnat.impl.core.jsren.dom.node;

import java.util.Iterator;
import java.util.LinkedList;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.itsnat.impl.core.jsren.JSRenderImpl;
import org.itsnat.impl.core.jsren.dom.node.html.JSRenderHTMLPropertyImpl;
import org.itsnat.impl.core.jsren.dom.node.otherns.JSRenderXULPropertyImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.util.MapListImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderPropertyImpl
{
    // No es necesario sincronizar esta coleccion porque va a ser sólo leída
    protected final MapListImpl propertiesByTagName = new MapListImpl();

    public JSRenderPropertyImpl()
    {
    }

    public static JSRenderPropertyImpl getJSRenderProperty(Element elem,Browser browser)
    {
        // Como se puede ver puede devolver nulo
        if (isHTMLProperty(elem))
            return JSRenderHTMLPropertyImpl.getJSRenderHTMLProperty(browser);
        else
        {
            String namespace = elem.getNamespaceURI();
            if (namespace != null)
            {
                if (NamespaceUtil.isXULNamespace(namespace))
                    return JSRenderXULPropertyImpl.SINGLETON;
                else
                    return null; // Caso válido, puede ser SVG que no tiene propiedades identificadas (propias de elementos SVG), debe detectarse este caso
            }
            else return null; // Es raro que sea nulo el namespace pues lo normal es que se herede de un elemento padre o del elemento root, aunque via Document.createElement() se consigue que sea nulo en cualquier tipo de documento.
        }
    }

    public static PropertyImpl getProperty(Element elem,String attrName,Browser browser)
    {
        JSRenderPropertyImpl render = getJSRenderProperty(elem,browser);
        if (render == null) return null;
        return render.getProperty(elem, attrName);
    }

    public static boolean isHTMLProperty(Element elem)
    {
        // La propiedad será tratada como HTML (incluye XHTML) si el elemento
        // es HTML (o XHTML). Esto es válido para documentos no X/HTML
        return DOMUtilHTML.isHTMLElement(elem);
    }

    protected void addProperty(String localName,String propName,int type)
    {
        addProperty(localName,propName,propName,type,null);
    }

    protected void addProperty(String localName,String propName,String attrName,int type)
    {
        addProperty(localName,propName,attrName,type,null);
    }

    protected void addProperty(String localName,String propName,String attrName,int type,String nullValue)
    {
        PropertyImpl property = new PropertyImpl(this,propName,attrName,type,nullValue);
        propertiesByTagName.add(localName,property);
    }

    public PropertyImpl getProperty(Element elem,String attrName)
    {
        String localNameElem = elem.getLocalName(); // Al devolver el localName nos evitamos el problema de si tiene prefijo o no
        LinkedList propList = propertiesByTagName.get(localNameElem);
        if (propList == null) return null;

        String attrNameLower = attrName.toLowerCase();
        if (propList.size() == 1)
        {
            // Para evitar crear un Iterator
            PropertyImpl prop = (PropertyImpl)propList.getFirst();
            if (prop.getAttributeNameLower().equals(attrNameLower))
                return prop;
        }
        else
        {
            for(Iterator it = propList.iterator(); it.hasNext(); )
            {
                PropertyImpl prop = (PropertyImpl)it.next();
                if (prop.getAttributeNameLower().equals(attrNameLower))
                    return prop;
            }
        }
        return null;
    }

    protected String attrValueJSToPropValueJS(PropertyImpl prop,boolean setValue,String attrValueJS,String value)
    {
        int type = prop.getType();
        String propValueJS;
        if (setValue)
        {
            switch(type)
            {
                case PropertyImpl.BOOLEAN:
                    propValueJS = "true";
                    break;
                case PropertyImpl.STRING: // El propio attrValueJS
                    propValueJS = attrValueJS;
                    break;
                case PropertyImpl.INTEGER:
                    propValueJS = "parseInt(" +  attrValueJS + ")";
                    break;
                default:
                    propValueJS = null; // NUNCA se da este caso
            }
        }
        else
        {
            propValueJS = prop.getNullValue();
        }
        return propValueJS;
    }


    public String renderRemoveProperty(PropertyImpl prop,Element elem,String elemVarName,ClientDocumentStfulImpl clientDoc)
    {
        return renderProperty(prop,elem,elemVarName,null,null,false,clientDoc);
    }

    public String renderSetProperty(PropertyImpl prop,Element elem,String elemVarName,String attrValueJS,String value,ClientDocumentStfulImpl clientDoc)
    {
        return renderProperty(prop,elem,elemVarName,attrValueJS,value,true,clientDoc);
    }

    protected String renderProperty(PropertyImpl prop,Element elem,String elemVarName,String attrValueJS,String value,boolean setValue,ClientDocumentStfulImpl clientDoc)
    {
        String propName = prop.getPropertyName();
        String propValueJS = attrValueJSToPropValueJS(prop,setValue,attrValueJS,value);

        return elemVarName + "." + propName + " = " + propValueJS + ";\n";
    }

    public String renderAttrAsProperty(PropertyImpl prop,Element elem,String elemVarName,ClientDocumentStfulImpl clientDoc)
    {
        Attr attr = elem.getAttributeNode(prop.getAttributeName());
        if (attr != null)
        {
            String value = attr.getValue();
            String attrValueJS = JSRenderImpl.toTransportableStringLiteral(value,clientDoc.getBrowser());
            return renderSetProperty(prop,elem,elemVarName,attrValueJS,value,clientDoc);
        }
        else
            return renderRemoveProperty(prop,elem,elemVarName,clientDoc);
    }

    public static String renderUIControlProperty(Element elem,String elemVarName,String attrName,ClientDocumentStfulImpl clientDoc)
    {
        JSRenderPropertyImpl render = getJSRenderProperty(elem,clientDoc.getBrowser());
        if (render == null) return ""; // RARO
        PropertyImpl prop = render.getProperty(elem,attrName);
        return render.renderAttrAsProperty(prop,elem,elemVarName,clientDoc);
    }
}
