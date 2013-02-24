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

import java.io.Serializable;
import org.itsnat.comp.text.ItsNatFormattedTextField;
import java.lang.reflect.Constructor;
import java.text.ParseException;

/**
 *
 * @author jmarranz
 */
public class ItsNatFormatterDefaultImpl implements ItsNatFormattedTextField.ItsNatFormatter,Serializable
{
    /** Creates a new instance of ItsNatFormatterDefaultImpl */
    public ItsNatFormatterDefaultImpl()
    {
    }

    public Class<?> getValueClass(ItsNatFormattedTextField comp)
    {
        // Devuelve la clase del valor actual
        Object value = comp.getValue();
        if (value != null)
            return value.getClass();
        else
            return null;
    }

    public Object stringToValue(String str,ItsNatFormattedTextField comp) throws ParseException
    {
        Class<?> vc = getValueClass(comp);

        if (vc != null)
        {
            Constructor<?> cons;

            try
            {
                cons = vc.getConstructor(new Class<?>[] { String.class });
            }
            catch (NoSuchMethodException nsme)
            {
                cons = null;
            }

            if (cons != null)
            {
                try
                {
                    return cons.newInstance(new Object[] { str });
                }
                catch (Exception ex)
                {
                    throw new ParseException("Error creating instance", 0);
                }
            }
        }
        return str;
    }

    public String valueToString(Object value,ItsNatFormattedTextField comp) throws ParseException
    {
        if (value == null)
        {
            return "";
        }
        return value.toString();
    }

}
