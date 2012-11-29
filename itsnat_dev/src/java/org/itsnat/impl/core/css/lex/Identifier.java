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
public class Identifier extends Token
{
    protected String value = "";

    /** Creates a new instance of Identifier */
    public Identifier(Cursor cursor)
    {
        super(cursor.getCurrentPos());
        parse(cursor);
    }

    public static boolean isIdentifierStart(char c)
    {
        return Character.isJavaIdentifierStart(c);
    }

    public static boolean isIdentifierPart(char c)
    {
        if (Character.isJavaIdentifierPart(c))
            return true;
        if (c == '-') // En HTML se admite como parte de identificadores
            return true;
        return false;
    }

    public String toString()
    {
        return value;
    }

    public void parse(Cursor cursor)
    {
        // cursor apunta al comienzo del identificador
        StringBuffer valueTmp = new StringBuffer();
        valueTmp.append( cursor.getCurrentChar() );
        cursor.inc(); // segunda letra (si hay)
        while( cursor.isValidPosition() &&
              isIdentifierPart(cursor.getCurrentChar()))
        {
            valueTmp.append( cursor.getCurrentChar() );
            cursor.inc();
        }

        this.value = valueTmp.toString();

        cursor.dec();
        this.end = cursor.getCurrentPos(); // apunta al último caracter del identificador
    }
}
