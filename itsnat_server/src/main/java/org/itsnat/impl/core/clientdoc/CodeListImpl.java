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

package org.itsnat.impl.core.clientdoc;

import java.util.LinkedList;
import org.itsnat.core.ItsNatException;

/**
 *
 * @author jmarranz
 */
public class CodeListImpl
{
    protected LinkedList<Object> list = new LinkedList<Object>();

    public void add(Object codeFragment)
    {
        if (codeFragment == null) throw new ItsNatException("Cannot be null");    // Lo detectamos aquí porque de otra manera dará error después al procesarse la lista

        list.add(codeFragment);
    }
    
    protected Object getLast(CodeToSendRegistryImpl codeReg)
    {
        Object last = list.getLast(); // No tiene sentido que la lista sea nula
        return codeReg.getLastCodeToSend(last);
    }
    
    protected String codeToString(CodeToSendRegistryImpl codeReg)
    {
        StringBuilder code = new StringBuilder();
        for(Object codeFragment : list)
        {
            code.append( codeReg.codeToString( codeFragment ) );
        }
        return code.toString();
    }

    @Override
    public String toString()
    {
        throw new RuntimeException("INTERNAL ERROR");
    }
}
