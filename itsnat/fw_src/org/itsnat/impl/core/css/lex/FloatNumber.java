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

package org.itsnat.impl.core.css.lex;

/**
 *
 * @author jmarranz
 */
public class FloatNumber extends Token
{
    protected String value = "";

    /**
     * Creates a new instance of FloatNumber
     */
    public FloatNumber(String code,Cursor cursor)
    {
        super(cursor.getPos());
        parse(code,cursor);
    }

    public static boolean isFloatNumberStart(char c)
    {
        // Yo creo que el + no forma parte del estándar pero por si acaso
        return (c == '.') || (c == '+') || (c == '-') || Character.isDigit(c);
    }

    public static boolean isFloatNumberPart(char c)
    {
        // En CSS los números son en coma fija (no mantisa/exponente)
        return (c == '.') || Character.isDigit(c);
    }

    public String toString()
    {
        return value;
    }

    public float getFloat()
    {
        return Float.parseFloat(value);
    }

    public void parse(String code,Cursor cursor)
    {
        // cursor apunta al comienzo del número
        StringBuffer valueTmp = new StringBuffer();
        valueTmp.append( code.charAt(cursor.getPos()) );
        int i = cursor.inc(); // segunda letra (si hay)
        while((i < code.length()) &&
              isFloatNumberPart(code.charAt(i)))
        {
            valueTmp.append( code.charAt(i) );
            i = cursor.inc();
        }

        this.value = valueTmp.toString();

        cursor.dec();
        this.end = cursor.getPos(); // apunta al último caracter del número
    }
}
