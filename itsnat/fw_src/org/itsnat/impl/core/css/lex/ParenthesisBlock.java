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

import java.util.Iterator;

/**
 *
 * @author jmarranz
 */
public class ParenthesisBlock extends Block
{

    /** Creates a new instance of ParenthesisBlock */
    public ParenthesisBlock(String code,Cursor cursor)
    {
        super(code,cursor);
    }

    public char getBeginBlockChar()
    {
        return '(';
    }

    public char getEndBlockChar()
    {
        return ')';
    }
}
