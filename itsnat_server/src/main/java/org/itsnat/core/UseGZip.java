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

package org.itsnat.core;

/**
 * Defines some constants and utility methods related with GZip compression.
 *
 * <p>Constant values are bitwise defined to be used with | or & bit operators.
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatServletConfig#setUseGZip(int)
 */
public class UseGZip
{
    /**
     * Defines that neither markup nor JavaScript code is sent to the client compressed.
     */
    public static final int NONE = 0;

    /**
     * Defines that markup is sent to the client compressed.
     */
    public static final int MARKUP = 0x1;  // Bin: 01

    /**
     * Defines that markup is sent to the client compressed.
     */
    public static final int SCRIPT = 0x2;  // Bin: 10

    /**
     * Informs whether the argument includes the SCRIPT
     * bit.
     *
     * @param value the value to inspect.
     * @return true if the argument includes the SCRIPT bit.
     */
    public static boolean isScriptUsingGZip(int value)
    {
        return (value & SCRIPT) != 0;
    }

    /**
     * Informs whether the argument includes the MARKUP
     * bit.
     *
     * @param value the value to inspect.
     * @return true if the argument includes the MARKUP bit.
     */
    public static boolean isMarkupUsingGZip(int value)
    {
        return (value & MARKUP) != 0;
    }

    /**
     * Informs whether the argument is NONE.
     *
     * @param value the value to inspect.
     * @return true if the argument is NONE.
     */
    public static boolean isNoneUsingGZip(int value)
    {
        return (value == NONE);
    }
}
