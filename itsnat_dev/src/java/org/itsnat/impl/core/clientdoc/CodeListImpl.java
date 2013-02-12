/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.clientdoc;

import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author jmarranz
 */
public class CodeListImpl
{
    protected LinkedList<Object> list = new LinkedList<Object>();

    public void add(Object codeFragment)
    {
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
