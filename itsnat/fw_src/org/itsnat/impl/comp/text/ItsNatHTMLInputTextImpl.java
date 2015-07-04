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

package org.itsnat.impl.comp.text;

import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.mgr.web.ItsNatStfulWebDocComponentManagerImpl;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatHTMLInputTextImpl extends ItsNatHTMLInputTextBasedImpl implements ItsNatHTMLInputText
{

    /**
     * Creates a new instance of ItsNatHTMLInputTextBasedImpl
     */
    public ItsNatHTMLInputTextImpl(HTMLInputElement element,NameValue[] artifacts,ItsNatStfulWebDocComponentManagerImpl componentMgr)
    {
        super(element,artifacts,componentMgr);
    }

    public static ItsNatHTMLInputText newItsNatHTMLInputText(HTMLInputElement element,String compType,NameValue[] artifacts,ItsNatStfulWebDocComponentManagerImpl componentMgr)
    {
        if (compType == null)
            return componentMgr.createItsNatHTMLInputText(element,artifacts);
        else if (compType.equals("formattedTextField"))
            return componentMgr.createItsNatHTMLInputTextFormatted(element,artifacts);
        else
            return null;
    }

    public String getExpectedType()
    {
        return "text";
    }

}
