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

import org.itsnat.comp.button.toggle.ItsNatButtonCheckBox;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLInputCheckBoxUIImpl extends ItsNatHTMLInputButtonToggleUIImpl
{

    /** Creates a new instance of ItsNatHTMLInputCheckBoxUIImpl */
    public ItsNatHTMLInputCheckBoxUIImpl(ItsNatHTMLInputCheckBoxImpl parentComp)
    {
        super(parentComp);

        HTMLInputElement element = getHTMLInputElement();
        DOMUtilInternal.setAttribute(element,"type","checkbox"); // para asegurarnos
    }

    public ItsNatButtonCheckBox getItsNatButtonCheckBox()
    {
        return (ItsNatButtonCheckBox)parentComp;
    }

    public ItsNatHTMLInputCheckBox getItsNatHTMLInputCheckBox()
    {
        return (ItsNatHTMLInputCheckBox)parentComp;
    }
}
