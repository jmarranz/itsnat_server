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

package org.itsnat.impl.comp.table;

import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.comp.table.ItsNatFreeTable;
import org.itsnat.comp.table.ItsNatFreeTableHeader;
import org.itsnat.comp.table.ItsNatTableStructure;
import org.itsnat.comp.ItsNatComponentUI;
import org.itsnat.comp.table.ItsNatTableUI;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.*;
import org.itsnat.impl.comp.table.ItsNatFreeTableUIImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Ejemplos de prototipos posibles (tags inventados):
 * 1) Caso de header y body:
 * <tableParent>
 *   <header><item>..</item><item>..</item></header>
 *   <body>
 *      <row><item>..</item><item>..</item></row>
 *   </body>
 * </tableParent>
 * 2) Caso de sólo body:
 * <tableParent>
 *      <row><item>..</item><item>..</item></row>
 * </tableParent>
 * En el caso 2) es fundamental que sólo exista una fila patrón
 * para detectar que no hay header
 *
 * @author jmarranz
 */
public class ItsNatFreeTableImpl extends ItsNatTableImpl implements ItsNatFreeTable
{
    /** Creates a new instance of ItsNatFreeTableImpl */
    public ItsNatFreeTableImpl(Element parentElem,ItsNatTableStructure structure,NameValue[] artifacts,ItsNatDocComponentManagerImpl componentMgr)
    {
        super(parentElem,structure,artifacts,componentMgr);

        init();
    }

    public ItsNatFreeTableHeader getItsNatFreeTableHeader()
    {
        return (ItsNatFreeTableHeader)header;
    }

    public ItsNatTableUI createDefaultItsNatFreeTableUI()
    {
        return new ItsNatFreeTableUIImpl(this);
    }

    public ItsNatComponentUI createDefaultItsNatComponentUI()
    {
        return createDefaultItsNatFreeTableUI();
    }

    public ItsNatTableHeaderImpl createItsNatTableHeader(Element headerElem)
    {
        return new ItsNatFreeTableHeaderImpl(this,headerElem);
    }

    public Node createDefaultNode()
    {
        throw new ItsNatException("There is no default Element and later attachment is not allowed",this);
    }
}
