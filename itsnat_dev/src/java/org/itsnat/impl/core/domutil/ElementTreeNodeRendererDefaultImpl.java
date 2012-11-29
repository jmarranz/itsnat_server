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
import org.itsnat.core.domutil.ElementTreeNodeRenderer;
import org.itsnat.core.domutil.ElementTreeNode;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ElementTreeNodeRendererDefaultImpl implements ElementTreeNodeRenderer,Serializable
{
    // A día de hoy no tiene información de estado, por tanto para ahorrar memoria usamos un singleton
    protected final static ElementTreeNodeRendererDefaultImpl SINGLETON = new ElementTreeNodeRendererDefaultImpl();

    /**
     * Creates a new instance of ElementTreeNodeRendererDefaultImpl
     */
    private ElementTreeNodeRendererDefaultImpl()
    {
    }

    public static ElementTreeNodeRendererDefaultImpl newElementTreeNodeRendererDefault()
    {
        // Si se añade en el futuro alguna información de estado crear el objeto de forma normal con new
        return SINGLETON;
    }

    public void renderTreeNode(ElementTreeNode treeNode,Object value,Element labelElem,boolean isNew)
    {
        if (labelElem == null) labelElem = treeNode.getLabelElement();
        ElementRendererDefaultImpl.SINGLETON.render(null,value,labelElem,isNew);
    }

    public void unrenderTreeNode(ElementTreeNode treeNode,Element labelElem)
    {
    }
}
