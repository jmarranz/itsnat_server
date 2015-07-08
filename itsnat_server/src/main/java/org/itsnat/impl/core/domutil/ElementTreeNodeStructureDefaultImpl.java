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
import org.itsnat.core.domutil.ElementTreeNode;
import org.itsnat.core.domutil.ElementTreeNodeStructure;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

/**
  Ejemplo:
    <li>
        <span><span>handle</span><span>Icon</span><span><b>Item content</b></span></span>
        <ul>
        </ul>
    </li>

 * @author jmarranz
 */
public class ElementTreeNodeStructureDefaultImpl implements ElementTreeNodeStructure,Serializable
{
    public static final ElementTreeNodeStructureDefaultImpl SINGLETON = new ElementTreeNodeStructureDefaultImpl();

    /**
     * Creates a new instance of ElementTreeNodeStructureDefaultImpl
     */
    private ElementTreeNodeStructureDefaultImpl()
    {
    }

    public static ElementTreeNodeStructureDefaultImpl newElementTreeNodeStructureDefault()
    {
        // A día de hoy no se guarda estado por lo que el SINGLETON ayuda a disminuir el número de objetos
        return SINGLETON;
    }


    public static Element getEffectiveParentElement(Element treeNodeParent)
    {
        if (treeNodeParent instanceof HTMLTableRowElement)
        {
            /*
               Ejemplo de patrón:
                <table>
                    <tbody id="someId">
                        <tr> <-- el treeNodeParent
                            <td>
                                (nodo)
                            </td>
                        </tr>
                    </tbody>
                 </table>

                O como tree-table con <table>:
                    <table>
                        <tbody id="someId">
                            <tr> <-- el treeNodeParent
                                <td>
                                    (nodo)
                                </td>
                                <td>Otros</td>
                                ...
                            </tr>
                        </tbody>
                     </table>
             */
            return ItsNatTreeWalker.getFirstChildElement(treeNodeParent);
        }
        else
            return treeNodeParent;
    }

    public static Element getContentElement(boolean treeTable,Element treeNodeParent)
    {
        treeNodeParent = getEffectiveParentElement(treeNodeParent);

        // treeNodeParent es por ejemplo un <li> (o un <td> si es con <table>)

        if (!treeTable)
        {
            /*
                - Ejemplo de patrón:
             <ul id="someId">
                <li> <-- el (efectivo) treeNodeParent
                    <span><span>handle</span><span>Icon</span><span><b>Item content</b></span></span>
                    <ul>
                    </ul>
                </li>
             <ul>

             - Ejemplo con <table>:
            <table>
                <tbody id="someId">
                    <tr>
                        <td> <-- el (efectivo) treeNodeParent
                            <span><span>handle</span><span>Icon</span><span><b>Item content</b></span></span>
                            <table>
                                <tbody />
                            </table>
                        </td>
                    </tr>
                </tbody>
             </table>

              Devolverá el <span> padre del handler, icono y contenido
            */
            return ItsNatTreeWalker.getFirstChildElement(treeNodeParent);
        }
        else
        {
            /*
             - Ejemplo de patrón:
             <div id="someId">
                <p> <-- el (efectivo) treeNodeParent
                    <span>handle</span><span>Icon</span><span><b>Item content</b></span>
                </p>
             <div>

            - Ejemplo con <table>:
            <table>
                <tbody id="someId">
                    <tr>
                        <td> <-- el efectivo treeNodeParent
                            <span>handle</span>
                            <span>Icon</span>
                            <span><b>Label</b></span>
                        </td>
                        <td>Otros</td>
                        ...
                    </tr>
                </tbody>
             </table>
            */
            return treeNodeParent;
        }
    }

    public Element getContentElement(ElementTreeNode treeNode,Element nodeParent)
    {
        return getContentElement(treeNode.isTreeTable(),nodeParent);
    }

    public static Element getHandleElement(Element contentParent)
    {
        if (contentParent instanceof HTMLTableElement)
        {
            // Estructura guiada por un <table> (por ejemplo):
            // <table><tbody><tr><td><img src="Handle" /></td><td><img src="Icon" /></td><td>Label</td></tr></td></table>
            HTMLTableSectionElement tbody = (HTMLTableSectionElement)ItsNatTreeWalker.getFirstChildElement(contentParent);
            HTMLTableRowElement row = (HTMLTableRowElement)ItsNatTreeWalker.getFirstChildElement(tbody);
            return getHandleElement(row);
        }
        else
        {
            // Estructura tipo
            // <contentParent><handle/><icon/><label>...Label...</label></contentParent>
            // Por ejemplo:
            // <span><img src="Handle" /><img src="Icon" /><span><b>Label</b></span></span>
            // Devuelve null si es una estructura tipo:
            // <contentParent>Label</contentParent>

            Element handleElem = ItsNatTreeWalker.getFirstChildElement(contentParent);
            if (handleElem == null)
                return null; // Es una estructura tipo: <contentParent>Label</contentParent>
            Element siblingElem = ItsNatTreeWalker.getNextSiblingElement(handleElem);
            if (siblingElem == null)
                return null; // Es una estructura tipo: <contentParent><opt>Label</opt></contentParent> , ej. <span><b>Label</b></span>

            return handleElem;
        }
    }

    public static Element getHandleElement(boolean treeTable,Element treeNodeParent)
    {
        Element contentParent = getContentElement(treeTable,treeNodeParent);
        return getHandleElement(contentParent);
    }


    public static Element getIconElement(boolean treeTable,Element treeNodeParent)
    {
        Element handleElem = getHandleElement(treeTable,treeNodeParent);
        if (handleElem != null)
            return ItsNatTreeWalker.getNextSiblingElement(handleElem);
        else
            return null; // Si no hay handle no puede haber icon, caso de <contentParent>Label</contentParent> o similar
    }

    public static Element getLabelElement(boolean treeTable,Element treeNodeParent)
    {
        Element iconElem = getIconElement(treeTable,treeNodeParent);
        if (iconElem != null)
            return ItsNatTreeWalker.getNextSiblingElement(iconElem);
        else
        {
            //  Caso de <contentParent>Label</contentParent> o
            // <contentParent><opt>Label</opt></contentParent> o similar
            return getContentElement(treeTable,treeNodeParent);
        }
    }

    public static Element getChildListElement(boolean treeTable,Element treeNodeParent)
    {
        if (treeTable)
            return null; // Es el caso de TreeTable, no tiene sentido llamar a getChildListElement en el caso de TreeTable pero por si acaso

        Element contentElem = getContentElement(treeTable,treeNodeParent);
        Element nextSiblingToContentElem = ItsNatTreeWalker.getNextSiblingElement(contentElem);

        if (nextSiblingToContentElem == null)
            return null; // No hay hijos

        if (nextSiblingToContentElem instanceof HTMLTableElement)
        {
            /* Ver en getContentElement el ejemplo completo:
                <td>
                    <span><span>handle</span><span>Icon</span><span><b>Item content</b></span></span>
                    <table> <-- nextSiblingToContentElem
                        <tbody>
                        </tbody>
                    </table>
                </td>
             */
            return (HTMLTableSectionElement)ItsNatTreeWalker.getFirstChildElement(nextSiblingToContentElem); // Devuelve el <tbody>
        }
        else
        {
            /* Ver en getContentElement el ejemplo completo:
                <li>
                    <span><span>handle</span><span>Icon</span><span><b>Item content</b></span></span>
                    <ul>  <-- nextSiblingToContentElem
                    </ul>
                </li>
             */
            return nextSiblingToContentElem;
        }
    }


    public Element getHandleElement(ElementTreeNode treeNode,Element nodeParent)
    {
        return getHandleElement(treeNode.isTreeTable(),nodeParent);
    }

    public Element getIconElement(ElementTreeNode treeNode,Element nodeParent)
    {
        return getIconElement(treeNode.isTreeTable(),nodeParent);
    }

    public Element getLabelElement(ElementTreeNode treeNode,Element nodeParent)
    {
        return getLabelElement(treeNode.isTreeTable(),nodeParent);
    }

    public Element getChildListElement(ElementTreeNode treeNode,Element nodeParent)
    {
        return getChildListElement(treeNode.isTreeTable(),nodeParent);
    }
}
