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
import org.itsnat.core.domutil.ElementLabel;
import org.itsnat.core.domutil.ElementLabelRenderer;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ElementLabelRendererDefaultImpl implements ElementLabelRenderer,Serializable
{
    // A día de hoy no tiene información de estado, por tanto para ahorrar memoria usamos un singleton
    protected final static ElementLabelRendererDefaultImpl SINGLETON = new ElementLabelRendererDefaultImpl();

    /**
     * Creates a new instance of ElementLabelRendererDefaultImpl
     */
    private ElementLabelRendererDefaultImpl()
    {
    }

    public static ElementLabelRendererDefaultImpl newElementLabelRendererDefault()
    {
        // Si se añade en el futuro alguna información de estado crear el objeto de forma normal con new
        return SINGLETON;
    }

    public void renderLabel(ElementLabel label,Object value,Element elem,boolean isNew)
    {
        if (elem == null) elem = label.getParentElement();
        ElementRendererDefaultImpl.SINGLETON.render(null,value,elem,isNew);
    }

    public void unrenderLabel(ElementLabel label,Element elem)
    {
    }
}
