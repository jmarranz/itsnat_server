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
import org.itsnat.core.domutil.ElementListStructure;
import org.itsnat.core.domutil.ElementList;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableRowElement;

/**
 * @author jmarranz
 */
public class ElementListStructureDefaultImpl implements ElementListStructure,Serializable
{
    public static final ElementListStructureDefaultImpl SINGLETON = new ElementListStructureDefaultImpl();

    /**
     * Creates a new instance of ElementListStructureDefaultImpl
     */
    private ElementListStructureDefaultImpl()
    {
    }

    public static ElementListStructureDefaultImpl newElementListStructureDefault()
    {
        // A día de hoy no se guarda estado por lo que el SINGLETON ayuda a disminuir el número de objetos
        return SINGLETON;
    }

    public static Element getContentElement(int index,Element elem)
    {
        if (elem instanceof HTMLTableRowElement)
        {
            /*
               Ejemplo de patrón:
                <table>
                    <tbody id="someId">
                        <tr> <-- elem
                            <td>Cell Pattern</td>
                        </tr>
                    </tbody>
                 </table>
             */
            return (HTMLTableCellElement)ItsNatTreeWalker.getFirstChildElement(elem);
        }
        else
            return elem;
    }

    public Element getContentElement(ElementList list,int index,Element elem)
    {
        if (elem == null) elem = list.getElementAt(index);

        return getContentElement(index,elem);
    }

}
