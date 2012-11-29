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
 *
 * @author jmarranz
 */
public class CSSValueInheritImpl extends CSSValueImpl
{

    /** Creates a new instance of CSSValueInheritImpl */
    public CSSValueInheritImpl(SourceCode cssTextCode,int code,ObjectValueParent parent)
    {
        super(cssTextCode,code,parent);
    }

    public static boolean isCSSValueInherit(SourceCode cssTextCode)
    {
        String cssText = cssTextCode.toString();
        cssText = cssText.trim();
        return cssText.equals("inherit");
    }

    public short getCssValueType()
    {
        return CSS_INHERIT;
    }

    public void setCssText(String cssText) throws DOMException
    {
        String cssTextTmp = cssText.trim();
        if (!cssTextTmp.equals("inherit"))
            throw new DOMException(DOMException.INVALID_ACCESS_ERR,"Only \"inherit\" value is valid");

        super.setCssText(cssText);
    }

    public void rebuild(SourceCode cssText)
    {
    }

    public Object getUpdatedChildObjectValueFromElement(Object requester,int requesterCode)
    {
        // "No tiene hijos"
        throw new DOMException(DOMException.INVALID_ACCESS_ERR,"INTERNAL ERROR");
    }

    public void notifyToElementChangedCSSText(SourceCode cssTextCode, Object requester)
    {
        // "No tiene hijos"
        throw new DOMException(DOMException.INVALID_ACCESS_ERR,"INTERNAL ERROR");
    }
}
