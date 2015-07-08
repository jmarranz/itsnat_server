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

import org.itsnat.core.ItsNatException;

/**
 *
 * @author jmarranz
 */
public abstract class StringLiteral extends Token
{
    protected String value = ""; // Contenido de la cadena sin comillas

    /**
     * Creates a new instance of StringLiteral
     */
    public StringLiteral(Cursor cursor)
    {
        super(cursor.getCurrentPos());
        parse(cursor);
    }

    public String toString()
    {
        char c = getDelimiterChar();
        return c + value + c;
    }

    public abstract char getDelimiterChar();

    public void parse(Cursor cursor)
    {
        // cursor apunta a la primera comilla
        // Faltaría procesar los escapes \ incluidos los \" y \'
        StringBuilder valueTmp = new StringBuilder();
        cursor.inc();
        char endChar = getDelimiterChar();
        while(cursor.isValidPosition() &&
              (endChar != cursor.getCurrentChar()))
        {
            valueTmp.append( cursor.getCurrentChar() );
            cursor.inc();
        }

        this.value = valueTmp.toString();

        if (cursor.isInTheEnd())
            throw new ItsNatException("Missing matching " + endChar + " start pos: " + start + " code: \"" + cursor.getCode() + "\"");

        int end = cursor.getCurrentPos();
        this.end = end; // apunta a la comilla finalizadora
    }
}
