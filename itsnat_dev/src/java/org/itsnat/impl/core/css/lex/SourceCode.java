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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 *
 * @author jmarranz
 */
public class SourceCode implements Serializable
{
    protected LinkedList tokens;
    protected StringBuilder code;

    /** Creates a new instance of SourceCode */
    public SourceCode(String code)
    {
        this.tokens = Token.parse(code);
        this.code = new StringBuilder(code);
    }

    public SourceCode()
    {
        this.tokens = new LinkedList();
        this.code = new StringBuilder();
    }

    public SourceCode(LinkedList tokens)
    {
        this.tokens = tokens;
        this.code = new StringBuilder(toStringTokens(tokens));
    }

    public SourceCode(String code,LinkedList tokens)
    {
        this.code = new StringBuilder(code);
        this.tokens = tokens;
        // Se supone que code se corresponde con los tokens
    }

    public static SourceCode newSourceCode(String code)
    {
        return new SourceCode(code);
    }

    public boolean equals(Object other)
    {
        if (super.equals(other))
            return true;
        if (!(other instanceof SourceCode))
            return false;
        return toString().equals(other.toString());
    }

    public int hashCode()
    {
        return toString().hashCode();
    }

    public void addToken(Token token)
    {
        tokens.add(token);
        code.append( token.toString() );
    }

    public int tokenCount()
    {
        return tokens.size();
    }

    public Token getToken(int i)
    {
        return (Token)tokens.get(i);
    }

    public String toString()
    {
        return code.toString();
    }

    public String toStringTokens()
    {
        return toStringTokens(tokens);
    }

    public static String toStringTokens(LinkedList tokens)
    {
        StringBuilder code = new StringBuilder();
        for(Iterator it = tokens.iterator(); it.hasNext(); )
        {
            Token token = (Token)it.next();
            code.append( token.toString() );
        }
        return code.toString();
    }

    public SourceCode[] split(Token byTok)
    {
        // Simula la función String.split pero con tokens
        SourceCode[] resTmp = new SourceCode[tokens.size() / 2 + 1]; // En este array caben todas las posibles soluciones
        SourceCode current = new SourceCode();
        resTmp[0] = current;
        int i = 0;
        for(Iterator it = tokens.iterator(); it.hasNext(); )
        {
            Token token = (Token)it.next();
            if (token.equals(byTok))
            {
                if (current.tokenCount() > 0) // Si no se cumple es que el actual está vacío, lo ignoramos y perdemos
                    i++;
                current = new SourceCode();
                resTmp[i] = current;
            }
            else
            {
                current.addToken(token);
            }
        }

        if (current.tokenCount() == 0)
            i--; // los vacíos no se incluyen, podría ser -1

        SourceCode[] res = new SourceCode[i + 1];
        for(int j = 0; j <= i; j++)
            res[j] = resTmp[j];
        return res;
    }

    public SourceCode trim()
    {
        LinkedList resTokens = new LinkedList();
        resTokens.addAll(this.tokens);

        boolean modified = false;

        for(ListIterator it = resTokens.listIterator(); it.hasNext(); )
        {
            Token token = (Token)it.next();
            if (!token.getClass().equals(Space.class))
                break;
            it.remove();
            modified = true;
        }

        for(ListIterator it = resTokens.listIterator(resTokens.size()); it.hasPrevious(); )
        {
            Token token = (Token)it.previous();
            if (!token.getClass().equals(Space.class))
                break;
            it.remove();
            modified = true;
        }

        if (modified)
            return new SourceCode(resTokens);
        else
            return new SourceCode(code.toString(),resTokens);
    }
}
