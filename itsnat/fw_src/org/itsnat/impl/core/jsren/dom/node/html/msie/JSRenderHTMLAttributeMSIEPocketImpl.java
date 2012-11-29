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

import java.util.Iterator;
import java.util.LinkedList;
import org.itsnat.impl.core.browser.BrowserMSIEPocket;
import org.itsnat.impl.core.path.NodeJSRefImpl;
import org.itsnat.impl.core.path.NodeLocationImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLAttributeMSIEPocketImpl extends JSRenderHTMLAttributeMSIEOldImpl
{
    public static final JSRenderHTMLAttributeMSIEPocketImpl SINGLETON = new JSRenderHTMLAttributeMSIEPocketImpl();

    /** Creates a new instance of JSMSIEHTMLAttributeRenderDefaultImpl */
    public JSRenderHTMLAttributeMSIEPocketImpl()
    {
    }

    public String setAttributeOnlyCode(Attr attr,String attrName,String jsValue,NodeJSRefImpl nodeRef,boolean newElem)
    {
        if (nodeRef.getNodeRef() instanceof NodeLocationImpl)
            return super.setAttributeOnlyCode(attr, attrName, jsValue, nodeRef, newElem);
        else
        {
            // Esto es porque el cambio de atributo cambia el elemento, necesitamos actualizar la referencia al elemento
            String elemVarName = (String)nodeRef.getNodeRef();
            return elemVarName + " = itsNatDoc.setAttribute(" + elemVarName + ",\"" + attrName + "\"," + jsValue + ");\n";
        }
    }

    protected String removeAttributeOnlyCode(Attr attr,String attrName,Element elem,NodeJSRefImpl nodeRef)
    {
        if (nodeRef.getNodeRef() instanceof NodeLocationImpl)
            return super.removeAttributeOnlyCode(attr,attrName,elem,nodeRef);
        else
        {
            // Esto es porque el cambio de atributo cambia el elemento, necesitamos actualizar la referencia al elemento
            String elemVarName = (String)nodeRef.getNodeRef();
            return elemVarName + " = itsNatDoc.removeAttribute(" + elemVarName + ",\"" + attrName + "\");\n";
        }
    }

    public static void addEventListenerAttrs(Element elem,LinkedList types,LinkedList attributes,Document doc)
    {
        /* Este método y removeAttributes no son exactamente de generación
         * de código JavaScript pero está relacionado con ello, los ponemos
         * aquí porque es código compartido por el procesado el carga
         * y por lo mutation events.
         */
        for(Iterator it = types.iterator(); it.hasNext(); )
        {
            String type = (String)it.next();
            String itsNatHandler = BrowserMSIEPocket.getHandlerValue(type);
            Attr attr = elem.getAttributeNode("on" + type);
            if (attr == null)
            {
                attr = doc.createAttribute("on" + type);
                elem.setAttributeNode(attr);
            }
            else // Ya usado por el programador, añadimos al principio el handler de ItsNat
            {
                itsNatHandler = itsNatHandler + attr.getValue();
            }
            attr.setValue(itsNatHandler);

            if (attributes != null)
                attributes.add(attr);
        }
    }

    public static void removeAttributes(LinkedList attributes)
    {
        // Restauramos el estado anterior del DOM
        for(Iterator it = attributes.iterator(); it.hasNext(); )
        {
            Attr attr = (Attr)it.next();
            Element elem = attr.getOwnerElement();
            String type = attr.getName().substring(2); // Quitamos el "on"
            String itsNatHandler = BrowserMSIEPocket.getHandlerValue(type);
            String value = attr.getValue();
            if (value.length() == itsNatHandler.length())
                elem.removeAttributeNode(attr); // No existía antes del proceso
            else
            {
                // Quitamos la parte del handler de ItsNat del principio
                value = value.substring(itsNatHandler.length());
                attr.setValue(value); // Restauramos el valor original
            }
        }
    }
}
