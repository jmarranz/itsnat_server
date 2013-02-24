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
public class Cursor
{
    protected int pos = 0;
    protected String code;

    /** Creates a new instance of Cursor */
    public Cursor(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }

    public int getLength()
    {
        return code.length();
    }

    public boolean isFirstPos()
    {
        return pos == 0;
    }

    public boolean isLastPos()
    {
        return pos == (getLength() - 1);
    }

    public boolean isInTheEnd()
    {
        return pos == getLength();
    }

    public boolean isValidPosition()
    {
        if (isInTheEnd()) return false;
        if (getLength() == 0) return false;
        return true;
    }

    public boolean hasNext()
    {
        return (pos + 1) <= (getLength() - 1);
    }

    public char getCurrentChar()
    {
        int i = getCurrentPos();
        return code.charAt(i);
    }

    public char getNextChar()
    {
        int i = getCurrentPos();
        return code.charAt(i + 1);
    }

    public int getCurrentPos()
    {
        if (!isValidPosition())
        {
            if (getLength() == 0) throw new ItsNatException("INTERNAL ERROR: code is empty");
            if (isInTheEnd()) throw new ItsNatException("INTERNAL ERROR: cursor in the end");
        }
        return pos;
    }

    public int inc()
    {
        // Podemos ponernos en la posición siguiente a la última pero no más allá, así evitamos bucles infinitos absurdos
        if (isInTheEnd()) throw new ItsNatException("INTERNAL ERROR: cursor is already in the end");
        pos++;
        return pos;
    }

    public int dec()
    {
        if (isFirstPos()) throw new ItsNatException("INTERNAL ERROR: cursor is already in first position");
        pos--;
        return pos;
    }
}
