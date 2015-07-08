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

package org.itsnat.impl.comp.button.toggle;

import org.itsnat.comp.button.toggle.ItsNatFreeCheckBoxLabel;
import org.itsnat.core.NameValue;
import org.itsnat.core.domutil.ElementRenderer;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.impl.core.domutil.ElementGroupManagerImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ItsNatFreeCheckBoxLabelImpl extends ItsNatFreeCheckBoxImpl implements ItsNatFreeCheckBoxLabel
{
    protected ElementRenderer renderer;
    protected Object value;

    /** Creates a new instance of ItsNatFreeCheckBoxLabelImpl */
    public ItsNatFreeCheckBoxLabelImpl(Element element,NameValue[] artifacts,ItsNatDocComponentManagerImpl componentMgr)
    {
        super(element,artifacts,componentMgr);

        ElementGroupManagerImpl factory = componentMgr.getItsNatDocumentImpl().getElementGroupManagerImpl();
        this.renderer = factory.createDefaultElementRenderer();

        init();
    }

    public Object getLabelValue()
    {
        return value;
    }

    public void setLabelValue(Object value)
    {
        this.value = value;

        ElementRenderer renderer = getElementRenderer();
        if (renderer != null)
            renderer.render(this,value,getElement(),false);
    }

    public ElementRenderer getElementRenderer()
    {
        return renderer;
    }

    public void setElementRenderer(ElementRenderer renderer)
    {
        this.renderer = renderer;
    }
}
