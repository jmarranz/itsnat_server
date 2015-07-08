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

import org.itsnat.comp.list.ItsNatListMultSel;
import org.itsnat.comp.list.ItsNatHTMLSelectMult;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.w3c.dom.html.HTMLOptionElement;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLSelectMultUIImpl extends ItsNatHTMLSelectUIImpl implements ItsNatListMultSelUIInternal
{
    /**
     * Creates a new instance of ItsNatHTMLSelectMultUIImpl
     */
    public ItsNatHTMLSelectMultUIImpl(ItsNatHTMLSelectMultImpl parentComp)
    {
        super(parentComp);
    }

    public ItsNatHTMLSelectMult getItsNatHTMLSelectMult()
    {
        return (ItsNatHTMLSelectMult)parentComp;
    }

    public ItsNatHTMLSelectMultImpl getItsNatHTMLSelectMultImpl()
    {
        return (ItsNatHTMLSelectMultImpl)parentComp;
    }

    public void setSelectedIndex(int index,boolean selected)
    {
        // Se ha detectado el extraño caso de selection model vacío (anteriormente con algo)
        // pero que al añadir un primer elemento (index = 0, length = 0) el caso es que genera
        // un evento con índices 0 y 1 existiendo un único elemento en teoría (size es 1).

        HTMLOptionElement option = getHTMLOptionElementAt(index);
        if (option == null)
            return;

        if (option.getSelected() != selected) // Es porque genera un mutation event aunque no cambie (para evitarlo)
        {
            DOMUtilInternal.setBooleanAttribute(option,"selected",selected);
            // option.setSelected(selected); no está definida en versiones de Xerces antiguas (ej. 2.6.2)
        }
    }

    public ItsNatListMultSel getItsNatListMultSel()
    {
        return (ItsNatListMultSel)parentComp;
    }

}
