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

package org.itsnat.impl.core.css;

import org.w3c.dom.DOMException;
import org.itsnat.impl.core.css.lex.SourceCode;

/**
 * NO SE USA
 *
 * @author jmarranz
 */
public class CSSValueCustomImpl extends CSSValueImpl
{

    /** Creates a new instance of CSSValueCustomImpl */
    public CSSValueCustomImpl(SourceCode cssTextCode,int code,ObjectValueParent parent)
    {
        super(cssTextCode,code,parent);

        // NO SE USA
        throw new DOMException(DOMException.INVALID_ACCESS_ERR,"INTERNAL ERROR");
    }

    public short getCssValueType()
    {
        return CSS_CUSTOM;
    }

    public void rebuild(SourceCode cssText)
    {
    }

    public Object getUpdatedChildObjectValueFromElement(Object requester,int requesterCode)
    {
        throw new DOMException(DOMException.INVALID_ACCESS_ERR,"INTERNAL ERROR");
    }

    public void notifyToElementChangedCSSText(SourceCode cssText, Object requester)
    {
    }
}
