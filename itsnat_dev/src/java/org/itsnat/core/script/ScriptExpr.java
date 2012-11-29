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

package org.itsnat.core.script;

/**
 * Encapsulates a JavaScript expression used to generate JavaScript code to send the client.
 *
 * <p>If the expression have properties or methods this interface provides methods
 * to generate JavaScript code to access or call them.</p>
 *
 * @see ScriptUtil#createScriptExpr(Object)
 */
public interface ScriptExpr
{
    /**
     * Returns the JavaScript code of this expression.
     *
     * <p>The specified object is converted to JavaScript following the rules
     * of {@link ScriptUtil#toScript(Object)} with an exception: if a <code>String</code> the
     * content is not converted to a JavaScript string literal.
     *
     * @return the JavaScript code.
     */
    public String getCode();

    /**
     * Generates the JavaScript code to set a value to the specified property of the result of this expression.
     *
     * @param propName property name.
     * @param value the value to set. Is converted to JavaScript calling {@link ScriptUtil#toScript(Object)}.
     * @param endSentence if true adds a ; at the end.
     * @return the JavaScript code.
     * @see ScriptUtil#getSetPropertyCode(Object,String,Object,boolean)
     */
    public String getSetPropertyCode(String propName,Object value,boolean endSentence);

    /**
     * Generates the JavaScript code to set a value to the specified property of the result of this expression.
     *
     * @param propName property name.
     * @param value the value to set. Is converted to JavaScript calling {@link ScriptUtil#toScript(Object)}.
     * @return the JavaScript code.
     * @see ScriptUtil#getSetPropertyCode(Object,String,Object)
     */
    public String getSetPropertyCode(String propName,Object value);

    /**
     * Generates the JavaScript code to get the value of the specified property of the result of this expression.
     *
     * @param propName property name.
     * @param endSentence if true adds a ; at the end.
     * @return the JavaScript code.
     * @see ScriptUtil#getGetPropertyCode(Object,String,boolean)
     */
    public String getGetPropertyCode(String propName,boolean endSentence);

    /**
     * Generates the JavaScript code to get the value of the specified property of the result of this expression.
     *
     * @param propName property name.
     * @return the JavaScript code.
     * @see ScriptUtil#getGetPropertyCode(Object,String)
     */
    public String getGetPropertyCode(String propName);

    /**
     * Generates the JavaScript code to call the specified method of the result of this expression.
     *
     * @param methodName method name.
     * @param params the parameter list. Are converted to JavaScript calling {@link ScriptUtil#toScript(Object)}.
     * @param endSentence if true adds a ; at the end.
     * @return the JavaScript code.
     * @see ScriptUtil#getCallMethodCode(Object,String,Object[],boolean)
     */
    public String getCallMethodCode(String methodName,Object[] params,boolean endSentence);

    /**
     * Generates the JavaScript code to call the specified method of the result of this expression.
     *
     * @param methodName method name.
     * @param params the parameter list. Are converted to JavaScript calling {@link ScriptUtil#toScript(Object)}.
     * @return the JavaScript code.
     * @see ScriptUtil#getCallMethodCode(Object,String,Object[])
     */
    public String getCallMethodCode(String methodName,Object[] params);

}
