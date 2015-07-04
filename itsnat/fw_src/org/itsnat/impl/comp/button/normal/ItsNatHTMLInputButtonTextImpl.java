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

package org.itsnat.impl.comp.button.normal;

import java.io.Serializable;
import org.itsnat.comp.button.ItsNatButtonLabel;
import org.itsnat.core.NameValue;
import org.itsnat.core.domutil.ElementRenderer;
import org.itsnat.impl.comp.mgr.web.ItsNatStfulWebDocComponentManagerImpl;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatHTMLInputButtonTextImpl extends ItsNatHTMLInputButtonNormalImpl implements ItsNatButtonLabel
{
    protected ElementRenderer renderer;
    protected Object value;

    /**
     * Creates a new instance of ItsNatHTMLInputButtonTextImpl
     */
    public ItsNatHTMLInputButtonTextImpl(HTMLInputElement element,NameValue[] artifacts,ItsNatStfulWebDocComponentManagerImpl componentMgr)
    {
        super(element,artifacts,componentMgr);

        this.renderer = HTMLInputButtonTextRenderer.SINGLETON;
    }

    public Object getLabelValue()
    {
        return value;
    }

    public void setLabelValue(Object value)
    {
        this.value = value;

        // Provocará un mutation event, no es necesario lanzar más tipos de eventos
        // el modelo del botón no considera los cambios en el texto
        ElementRenderer renderer = getElementRenderer();
        if (renderer != null)
            renderer.render(this,value,getHTMLInputElement(),false);
    }

    public ElementRenderer getElementRenderer()
    {
        return renderer;
    }

    public void setElementRenderer(ElementRenderer renderer)
    {
        this.renderer = renderer;
    }

    static class HTMLInputButtonTextRenderer implements ElementRenderer,Serializable
    {
        public static final HTMLInputButtonTextRenderer SINGLETON = new HTMLInputButtonTextRenderer();

        public void render(Object userObj, Object value, Element elem, boolean isNew)
        {
            HTMLInputElement inputElem = (HTMLInputElement)elem;
            inputElem.setValue(value.toString());
        }

        public void unrender(Object userObj,Element elem)
        {
        }
    }
}
