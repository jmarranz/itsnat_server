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
 * Espacio en sentido amplio (espacio, final de línea etc)
 * @author jmarranz
 */
public class Space extends Token
{
    protected char c;

    /** Creates a new instance of Space */
    public Space(char c,int start)
    {
        super(start);
        this.c = c;
        this.end = start;
    }

    public Space(char c)
    {
        this(c,0);
    }

    public static boolean isSpace(char c)
    {
        return Character.isSpaceChar(c);
    }

    public String toString()
    {
        return Character.toString(c);
    }
}
