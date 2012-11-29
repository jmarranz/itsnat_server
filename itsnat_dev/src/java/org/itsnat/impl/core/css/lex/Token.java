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

import java.io.Serializable;
import org.itsnat.core.ItsNatException;
import java.util.LinkedList;

/**
 * Una vez creado es de sólo lectura
 *
 * @author jmarranz
 */
public abstract class Token implements Serializable
{
    protected int start;
    protected int end;

    /** Creates a new instance of Token */
    public Token(int start)
    {
        this.start = start;
    }

    public Token()
    {
    }

    public int getStart()
    {
        return start;
    }

    public int getEnd()
    {
        return end;
    }

    public static LinkedList parse(String code)
    {
        Cursor cursor = new Cursor(code);
        return parse(cursor,false,' ');
    }

    public static LinkedList parse(Cursor cursor,boolean checkEndChar,char endChar)
    {
        LinkedList tokens = new LinkedList();
        for( ; cursor.isValidPosition(); cursor.inc())
        {
            int i = cursor.getCurrentPos();
            char c = cursor.getCurrentChar();
            Token token;
            if (checkEndChar && (c == endChar))
                break;
            if (Space.isSpace(c))
            {
                token = new Space(c,i);
            }
            else if (c == ';')
            {
                token = new SemiColon(i);
            }
            else if (c == ',')
            {
                token = new Comma(i);
            }
            else if (c == '(')
            {
                token = new ParenthesisBlock(cursor);
            }
            else if (c == '"')
            {
                token = new StringDoubleQuote(cursor);
            }
            else if (c == '\'')
            {
                token = new StringSimpleQuote(cursor);
            }
            else if (c == ':')
            {
                token = new Colon(i);
            }
            else if (c == '%')
            {
                token = new Percent(i);
            }
            else if (Identifier.isIdentifierStart(c))
            {
                token = new Identifier(cursor);
            }
            else if (FloatNumber.isFloatNumberStart(c))
            {
                token = new FloatNumber(cursor);
            }
            else if (HexNumber.isHexNumberStart(c))
            {
                token = new HexNumber(cursor);
            }
            else throw new ItsNatException("Unexpected char, pos: " + cursor.getCurrentPos() + " code: " + cursor.getCode());

            tokens.add(token);
        }
        return tokens;
    }

    public boolean equals(Object token)
    {
        if (super.equals(token))
            return true; // identidad de objetos
        if (!getClass().equals(token.getClass()))
            return false; // No pueden ser iguales si son de diferente clase
        return toString().equals(token.toString()); // Mismo tipo y mismo contenido
    }

    public int hashCode()
    {
        return toString().hashCode();
    }
}
