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
public class TestSourceCode
{

    /** Creates a new instance of TestSourceCode */
    public TestSourceCode()
    {
    }

    public static void main(String[] args)
    {
        String code;
        SourceCode srcCode;

        code = "clip : rect(1px 2in 3cm 4); border : solid 1.5px ; name: \"hello\" ;";
        srcCode = SourceCode.newSourceCode(code);
        System.out.println(code.equals(srcCode.toStringTokens()));

        SourceCode[] splited;
        code = ";;;";
        srcCode = SourceCode.newSourceCode(code);
        splited = srcCode.split(SemiColon.getSingleton());
        System.out.println(splited.length == 0);

        code = "1;2;3;4";
        srcCode = SourceCode.newSourceCode(code);
        splited = srcCode.split(SemiColon.getSingleton());
        System.out.println(splited.length == 4);

        code = ";1;2;3;4;";
        srcCode = SourceCode.newSourceCode(code);
        splited = srcCode.split(SemiColon.getSingleton());
        System.out.println(splited.length == 4);

        code = "  hello  ";
        srcCode = SourceCode.newSourceCode(code);
        srcCode = srcCode.trim();
        System.out.println(srcCode.tokenCount() == 1);
        System.out.println(srcCode.toString().equals("hello"));

        code = "  1.5  ";
        srcCode = SourceCode.newSourceCode(code);
        srcCode = srcCode.trim();
        System.out.println(((FloatNumber)srcCode.getToken(0)).getFloat() == 1.5);
    }

}
