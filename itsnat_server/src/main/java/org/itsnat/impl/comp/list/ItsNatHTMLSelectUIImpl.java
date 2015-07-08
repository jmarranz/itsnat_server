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

package org.itsnat.impl.comp.list;

import org.itsnat.comp.ItsNatHTMLElementComponent;
import org.itsnat.comp.list.ItsNatHTMLSelect;
import org.itsnat.comp.list.ItsNatHTMLSelectUI;
import org.itsnat.impl.core.domutil.ElementGroupManagerImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLSelectElement;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatHTMLSelectUIImpl extends ItsNatListUIImpl implements ItsNatHTMLSelectUI
{
    /**
     * Creates a new instance of ItsNatHTMLSelectUIImpl
     */
    public ItsNatHTMLSelectUIImpl(ItsNatHTMLSelectImpl parentComp)
    {
        super(parentComp);

        // Pasamos directamente el pattern para que no haya necesidad de poner un <option> pattern en el template
        Document doc = getItsNatDocumentImpl().getDocument();
        HTMLOptionElement newOption = (HTMLOptionElement)doc.createElementNS(NamespaceUtil.XHTML_NAMESPACE,"option"); 

        boolean master = parentComp.isMarkupDriven() ? false : true;

        ElementGroupManagerImpl factory = getItsNatDocumentImpl().getElementGroupManagerImpl();
        this.elementList = factory.createElementListInternal(master,getHTMLSelectElement(),newOption,false,null,true,null,null);
    }

    public ItsNatHTMLSelect getItsNatHTMLSelect()
    {
        return (ItsNatHTMLSelect)parentComp;
    }

    public HTMLSelectElement getHTMLSelectElement()
    {
        return getItsNatHTMLSelect().getHTMLSelectElement();
    }

    public Element getContentElementAt(int index,Element optionElem)
    {
        return optionElem;
    }

    public HTMLOptionElement getHTMLOptionElementAt(int index)
    {
        return (HTMLOptionElement)getElementAt(index);
    }

    public ItsNatHTMLElementComponent getItsNatHTMLElementComponent()
    {
        return (ItsNatHTMLElementComponent)parentComp;
    }

    public boolean isEnabled()
    {
        HTMLSelectElement element = getHTMLSelectElement();
        return !element.getDisabled();
    }

    public void setEnabled(boolean b)
    {
        HTMLSelectElement element = getHTMLSelectElement();
        element.setDisabled( ! b );
    }
}
