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

import org.itsnat.core.domutil.ElementListBase;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class ElementListBaseImpl extends ElementGroupImpl implements ElementListBase
{
    /** Creates a new instance of ElementListBaseImpl */
    public ElementListBaseImpl(ItsNatDocumentImpl itsNatDoc)
    {
        super(itsNatDoc);
    }

    public Node item(int index)
    {
        return getElementAt(index);
    }

    public abstract ListElementInfoImpl getListElementInfoFromNode(Node node,Element limitElem);
}
