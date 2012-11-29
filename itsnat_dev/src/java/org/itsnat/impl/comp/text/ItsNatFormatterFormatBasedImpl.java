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

import java.text.Format;
import java.text.ParseException;
import org.itsnat.comp.text.ItsNatFormattedTextField;

/**
 *
 * @author jmarranz
 */
public class ItsNatFormatterFormatBasedImpl extends ItsNatFormatterDefaultImpl
{
    protected Format format; // Admite el nulo

    /** Creates a new instance of ItsNatFormatterFormatBasedImpl */
    public ItsNatFormatterFormatBasedImpl(Format format)
    {
        this.format = format;
    }

    public Object stringToValue(String str,ItsNatFormattedTextField comp) throws ParseException
    {
        if (format == null)
            return super.stringToValue(str,comp);

        return format.parseObject(str);
    }

    public String valueToString(Object value,ItsNatFormattedTextField comp) throws ParseException
    {
        if (format == null)
            return super.valueToString(value,comp);

        return format.format(value);
    }

}
