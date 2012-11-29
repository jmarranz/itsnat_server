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

package org.itsnat.impl.core.domutil;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.itsnat.core.domutil.ElementRenderer;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.html.HTMLElement;

/**
 *
 * @author jmarranz
 */
public class ElementRendererDefaultImpl implements ElementRenderer,Serializable
{
    // A día de hoy no tiene información de estado, por tanto para ahorrar memoria usamos un singleton
    protected final static ElementRendererDefaultImpl SINGLETON = new ElementRendererDefaultImpl();

    public final static Set htmlTagNamesWithoutTextNode = new HashSet();

    static
    {
        // Elementos en los que no tiene sentido nodos de texto significativos (no espacios etc)
        htmlTagNamesWithoutTextNode.add("table");
        htmlTagNamesWithoutTextNode.add("tbody");
        htmlTagNamesWithoutTextNode.add("tfoot");
        htmlTagNamesWithoutTextNode.add("thead");
        htmlTagNamesWithoutTextNode.add("tr");
        htmlTagNamesWithoutTextNode.add("col");
        htmlTagNamesWithoutTextNode.add("colgroup");
        htmlTagNamesWithoutTextNode.add("frameset");
        htmlTagNamesWithoutTextNode.add("html");
        htmlTagNamesWithoutTextNode.add("select");
        htmlTagNamesWithoutTextNode.add("iframe");
        htmlTagNamesWithoutTextNode.add("img");
        htmlTagNamesWithoutTextNode.add("input");
        htmlTagNamesWithoutTextNode.add("br");
        // No se si se me olvida alguno
    }


    /**
     * Creates a new instance of ElementRendererDefaultImpl
     */
    private ElementRendererDefaultImpl()
    {
    }

    public static ElementRendererDefaultImpl newElementRendererDefault()
    {
        // Si se añade en el futuro alguna información de estado crear el objeto de forma normal con new
        return SINGLETON;
    }

    public void render(Object userObj,Object value,Element labelElem,boolean isNew)
    {
        if (value instanceof Node) // puede ser Element, DocumentFragment, Text etc
        {
            DOMUtilInternal.replaceContent(labelElem,(Node)value);
        }
        else
        {
            String text = DOMUtilInternal.toString(value);
            setTextDeepestFirstTextNode(labelElem,text);
        }
    }

    public void unrender(Object userObj,Element elem)
    {
    }

    public static void setTextDeepestFirstTextNode(Element elem,String value)
    {
        Text textNode = getFirstSignificativeTextNode(elem);
        if (textNode != null)
        {
            DOMUtilInternal.setCharacterDataContent(textNode,value);
        }
        else
        {
            // En este caso nos vale el primer elemento que pueda albergar nodos de texto significativos
            Element deepestElem = getDeepestFirstElementCapableOfChildTextNode(elem);
            if (deepestElem != null)
            {
                textNode = DOMUtilInternal.getFirstTextNode(deepestElem);
                if (textNode != null)
                {
                    DOMUtilInternal.setCharacterDataContent(textNode,value);
                }
                else
                {
                    // Creamos un nuevo nodo de texto bajo el elemento más bajo
                    // (si value tiene algo de otra manera no merece la pena insertar un nodo vacío)
                    if ((value != null) && !value.equals(""))
                    {
                        textNode = deepestElem.getOwnerDocument().createTextNode(value);
                        deepestElem.appendChild(textNode);
                    }
                }
            }
            else
            {
                if ((value != null) && !value.equals(""))
                {
                    textNode = elem.getOwnerDocument().createTextNode(value);
                    elem.appendChild(textNode);
                }
            }
        }
    }

    public static boolean isElementCapableOfChildTextNode(Element parent)
    {
        if ((parent instanceof HTMLElement) &&
             htmlTagNamesWithoutTextNode.contains(parent.getLocalName()))
                return false;

        return true; // De otros namespaces no estudiamos (con HTML tenemos suficiente)
    }

    public static Element getDeepestFirstElementCapableOfChildTextNode(Element parent)
    {
        // Ejemplo: <table><tbody><tr><td><img/><b></b></td>...
        // debería devolver el <b>

        Element child = ItsNatTreeWalker.getFirstChildElement(parent);

        while(child != null)
        {
            Element deepest = getDeepestFirstElementCapableOfChildTextNode(child);
            if (deepest != null) return deepest;
            child = ItsNatTreeWalker.getNextSiblingElement(child);
        }

        // El propio padre a lo mejor puede, es el caso por ejemplo de un <b> que contiene
        // imágenes
        if  (isElementCapableOfChildTextNode(parent))
            return parent;

        return null;
    }


    public static Element getFirstDeepestElement(Element parent)
    {
        // Por ejemplo, en este caso:
        // <div>
        //    <b>
        //     Prueba
        //    </b>
        // </div>
        // El nodo que devuelve es el elemento <b>
        // En el caso: <div/> devolvería sí mismo

        Element child = ItsNatTreeWalker.getFirstChildElement(parent);
        while(child != null)
        {
            parent = child;
            child = ItsNatTreeWalker.getFirstChildElement(parent);
        }

        return parent;
    }
    
    public static Text getFirstSignificativeTextNode(Node parent)
    {
        // Por ejemplo, en este caso:
        // <div>
        //    <b>
        //     Prueba
        //    </b>
        // </div>
        // El nodo que devuelve es nodo de texto Prueba

        Node child = parent.getFirstChild();
        while(child != null)
        {
            if (child.getNodeType() == Node.TEXT_NODE)
            {
                Text text = (Text)child;
                if (!DOMUtilInternal.isSeparator(text))
                    return text;
            }
            else
            {
                Text textNode = getFirstSignificativeTextNode(child);
                if (textNode != null)
                    return textNode;
            }
            child = child.getNextSibling();
        }

        return null;
    }

}
