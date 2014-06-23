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

package org.itsnat.impl.core.scriptren.jsren;

import org.itsnat.core.script.ScriptExpr;

/**
 *
 * @author jmarranz
 */
public class JScriptExprImpl implements ScriptExpr
{
    protected JSScriptUtilImpl scriptUtil;
    protected Object value;

    /** Creates a new instance of CodeValueImpl */
    public JScriptExprImpl(Object value,JSScriptUtilImpl scriptUtil)
    {
        this.value = value;
        this.scriptUtil = scriptUtil;
    }

    public String toString()
    {
        return getCode();
    }

    public String getCode()
    {
        if (value instanceof String)
            return (String)value; // No convertimos en cadena porque consideramos value como una expresión literal, javaToJS lo convertiría en cadena
        else
        {
            // Llamamos repetidas veces porque en el caso de un Node
            // la primera vez se genera una referencia con path pero
            // la segunda puede usar el id del caché lo cual acelera
            return scriptUtil.toScript(value);
        }
    }

    public String getSetPropertyCode(String propName,Object newValue,boolean endSentence)
    {
        return scriptUtil.getSetPropertyCode(this,propName,newValue,endSentence);
    }

    public String getSetPropertyCode(String propName,Object newValue)
    {
        return scriptUtil.getSetPropertyCode(this,propName,newValue);
    }

    public String getGetPropertyCode(String propName,boolean endSentence)
    {
        return scriptUtil.getGetPropertyCode(this,propName,endSentence);
    }

    public String getGetPropertyCode(String propName)
    {
        return scriptUtil.getGetPropertyCode(this,propName);
    }

    public String getCallMethodCode(String methodName,Object[] params,boolean endSentence)
    {
        return scriptUtil.getCallMethodCode(this,methodName,params,endSentence);
    }

    public String getCallMethodCode(String methodName,Object[] params)
    {
        return scriptUtil.getCallMethodCode(this,methodName,params);
    }
}
