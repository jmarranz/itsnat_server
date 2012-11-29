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

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderNotChildrenNodeImpl extends JSRenderNotAttrOrViewNodeImpl
{

    /** Creates a new instance of JSRenderNotChildrenNodeImpl */
    public JSRenderNotChildrenNodeImpl()
    {
    }

    public Object getAppendNewNodeCode(Node parent,Node newNode,String parentVarName,InsertAsMarkupInfoImpl insertMarkupInfo,ClientDocumentStfulImpl clientDoc)
    {
        // Es añadido al final no inserción en medio
        return getAppendCompleteChildNode(parent,newNode,parentVarName,clientDoc);
    }

    public Object getInsertNewNodeCode(Node newNode,ClientDocumentStfulImpl clientDoc)
    {
        return getInsertCompleteNodeCode(newNode,clientDoc);
    }
}
