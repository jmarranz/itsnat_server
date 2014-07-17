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

package org.itsnat.impl.core.scriptren.jsren.dom.node;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.w3c.dom.Node;

/**
 * La verdad es que esta clase sobra pues al insertar un DocumentFragment en el DOM
 * no se inserta el propio DocumentFragment sino los hijos
 * Aun así nos sirve desde un punto de vista estructural para forzar que
 * JSRenderElementImpl sea específica de Element y JSRenderHasChildrenNodeImpl sea genérica,
 * pues en el futuro quien sabe si consideramos una renderización con hijos de DocumentType
 *
 * @author jmarranz
 */
public class JSRenderDocumentFragmentImpl extends JSRenderHasChildrenNodeImpl
{
    public static final JSRenderDocumentFragmentImpl SINGLETON = new JSRenderDocumentFragmentImpl();

    /** Creates a new instance of JSDocumentFragmentRender */
    public JSRenderDocumentFragmentImpl()
    {
    }

    public String createNodeCode(Node node,ClientDocumentStfulDelegateImpl clientDoc)
    {
        return "itsNatDoc.doc.createDocumentFragment()";
    }

    public String addAttributesBeforeInsertNode(Node node,String elemVarName,ClientDocumentStfulDelegateImpl clientDoc)
    {
        return "";
    }

    public boolean isAddChildNodesBeforeNode(Node parent,ClientDocumentStfulDelegateImpl clientDoc)
    {
        return false;
    }
    
    @Override    
    public Object getInsertNewNodeCode(Node newNode,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return super.getInsertNewNodeCode(newNode,null,clientDoc);
    }
}
