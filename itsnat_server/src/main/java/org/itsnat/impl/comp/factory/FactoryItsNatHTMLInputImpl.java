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

package org.itsnat.impl.comp.factory;

import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public abstract class FactoryItsNatHTMLInputImpl extends FactoryItsNatHTMLComponentImpl
{
    /**
     * Creates a new instance of FactoryItsNatHTMLAnchorImpl
     */
    public FactoryItsNatHTMLInputImpl()
    {
    }

    public abstract String getTypeAttr();

    public static String getKeyHTMLInput(HTMLInputElement element,String compType)
    {
        // Redefine el base
        String type = element.getType().toLowerCase();
        String localName = getLocalNameStatic(type);
        return FactoryItsNatHTMLComponentImpl.getKey(localName, compType);
    }

    public static String getLocalNameStatic(String typeAttr)
    {
        return "input type='" + typeAttr + "'";
    }

    public String getLocalName()
    {
        return "input type='" + getTypeAttr() + "'";
    }

    public boolean isFormControl()
    {
        return true;
    }
}
