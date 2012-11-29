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

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.css.lex.SourceCode;
import org.w3c.dom.css.Counter;

/*
 * NO SE USA TODAVÍA
 */

public class CounterImpl extends CSSPrimitiveValueLiteralImpl implements Counter
{

    /** Creates a new instance of CounterImpl */
    public CounterImpl()
    {
    }

    public int getCode()
    {
        return COUNTER;
    }

    public String getPropertyName()
    {
        throw new ItsNatException("INTERNAL ERROR");
    }

    public Object getUpdatedChildObjectValueFromElement(Object requester, int requesterCode)
    {
        throw new ItsNatException("INTERNAL ERROR");
    }

    public void notifyToElementChangedCSSText(SourceCode cssText, Object requester)
    {
        throw new ItsNatException("INTERNAL ERROR");
    }

    public String getIdentifier()
    {
        throw new ItsNatException("INTERNAL ERROR");
    }

    public String getListStyle()
    {
        throw new ItsNatException("INTERNAL ERROR");
    }

    public String getSeparator()
    {
        throw new ItsNatException("INTERNAL ERROR");
    }

}
