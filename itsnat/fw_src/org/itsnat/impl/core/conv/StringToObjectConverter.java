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

package org.itsnat.impl.core.conv;

import org.itsnat.core.ItsNatException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jmarranz
 */
public abstract class StringToObjectConverter
{
    public static final Map converters = new HashMap(); // No sincronizamos porque sólo leeremos

    static
    {
        addConverter(new StringToStringConverter());
        addConverter(new StringToBooleanConverter());
        addConverter(new StringToByteConverter());
        addConverter(new StringToCharacterConverter());
        addConverter(new StringToShortConverter());
        addConverter(new StringToIntegerConverter());
        addConverter(new StringToLongConverter());
        addConverter(new StringToFloatConverter());
        addConverter(new StringToDoubleConverter());
    }

    /** Creates a new instance of StringCoverter */
    public StringToObjectConverter()
    {
    }

    public static void addConverter(StringToObjectConverter conv)
    {
        converters.put(conv.getClassTarget(),conv);
        Class wrapper = conv.getClassTargetWrapper();
        if (wrapper != null)
            converters.put(wrapper,conv);
    }

    public static Object convert(String value,Class type)
    {
        if (value == null)
            throw new ItsNatException("Unexpected null value");
        StringToObjectConverter conv = (StringToObjectConverter)converters.get(type);
        if (conv == null)
            throw new ItsNatException("Class type not supported: " + type.getName());
        return conv.convert(value);
    }

    public abstract Class getClassTarget();
    public abstract Class getClassTargetWrapper();
    public abstract Object convert(String value);
}
