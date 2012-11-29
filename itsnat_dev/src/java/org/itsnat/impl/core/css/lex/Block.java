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

import java.util.LinkedList;
import org.itsnat.core.ItsNatException;

/**
 *
 * @author jmarranz
 */
public abstract class Block extends Token
{
    protected SourceCode content;

    /** Creates a new instance of Block */
    public Block(Cursor cursor)
    {
        super(cursor.getCurrentPos()); // apunta al caracter de comienzo del bloque
        parse(cursor);
    }

    public void parse(Cursor cursor)
    {
        // cursor apunta al caracter de comienzo del bloque
        cursor.inc(); // interior del bloque
        char endChar = getEndBlockChar();
        LinkedList tokens = Token.parse(cursor,true,endChar);

        if (cursor.isInTheEnd())
            throw new ItsNatException("Missing matching " + endChar + " start pos: " + start + " code: \"" + cursor.getCode() + "\"");

        int end = cursor.getCurrentPos();
        this.end = end; // apunta al caracter de final del bloque
        this.content = new SourceCode(tokens);
    }

    public abstract char getBeginBlockChar();
    public abstract char getEndBlockChar();

    public String toString()
    {
        return "(" + content.toString() + ")";
    }

    public SourceCode getContent()
    {
        return content;
    }
}
