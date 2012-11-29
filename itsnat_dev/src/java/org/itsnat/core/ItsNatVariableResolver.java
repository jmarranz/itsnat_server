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

package org.itsnat.core;

import org.w3c.dom.Node;

/**
 * Used to locate/replace text marks (variables) inside text based nodes
 * (text nodes, comments, attribute values etc) without knowing the exact position in the DOM tree.
 *
 * <p>The variable syntax follows the JSP Expression Language notation:
 * <code>${name}</code>.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatServletRequest#createItsNatVariableResolver()
 * @see ItsNatDocument#createItsNatVariableResolver()
 * @see ItsNatDocument#createItsNatVariableResolver(boolean)
 * @see ItsNatSession#createItsNatVariableResolver()
 * @see ItsNatServletContext#createItsNatVariableResolver()
 */
public interface ItsNatVariableResolver
{
    /**
     * Informs whether this variable resolver is disconnected (does not have a parent).
     * Only local variables are used.
     *
     * @return true if this variable resolver is disconnected.
     */
    public boolean isDisconnected();

    /**
     * Creates a variable resolver as a child of this resolver (bound to this resolver).
     *
     * @return a variable resolver bound to this resolver.
     */
    public ItsNatVariableResolver createItsNatVariableResolver();

    /**
     * Returns the value associated to the specified name,
     * only the local registry of this resolver is searched.
     *
     * @param name the name to search for.
     * @return the value associated to the specified name or null if none is found.
     * @see #setLocalVariable(String,Object)
     * @see #removeLocalVariable(String)
     */
    public Object getLocalVariable(String name);

    /**
     * Registers the specified variable name and value into the local registry
     * of this resolver.
     *
     * @param name the variable name.
     * @param value the variable value.
     * @return the old value or null if not found.
     * @see #getLocalVariable(String)
     */
    public Object setLocalVariable(String name,Object value);

    /**
     * Unregisters the variable with the specified name from the local registry
     * of this resolver.
     *
     * @param name the variable name.
     * @return the variable value or null if not found.
     * @see #getLocalVariable(String)
     */
    public Object removeLocalVariable(String name);

    /**
     * Introspect the specified object registering all JavaBeans properties
     * as local variables prefixed with the specified reference name.
     *
     * <p>Current implementation uses <code>jav.beans.Introspector.getBeanInfo(Class)</code>
     * to get all JavaBeans properties. The reference name and property name are used
     * to build a qualified name using a dot as separator, this
     * qualified name is used to register as variable (e.g. "person.firstName"
     * where "person" is the reference name and "firstName" is a JavaBeans property).</p>
     *
     * @param refName reference name used to qualify JavaBeans properties. If null or empty no prefix is added to property names.
     * @param obj reference to the object value to introspect and register its JavaBeans properties.
     */
    public void introspect(String refName,Object obj);

    /**
     * Returns the value associated to the specified name.
     *
     * <p>First of all the local registry is searched calling {@link #getLocalVariable(String)}.
     * If this method returns null and this resolver is "disconnected" (was created calling {@link ItsNatDocument#createItsNatVariableResolver(boolean)} with
     * parameter <code>disconnected</code> set to true) variable resolution ends returning null.
     * If "connected" (variable resolver was created with any other factory method)
     * the "owner" of this resolver is used:
     * </p>
     *
     * <p>If the variable resolver is a child of another variable resolver
     * (was created with {@link #createItsNatVariableResolver()})
     * then the method {@link #getVariable(String)} of the parent is called to continue searching.</p>
     *
     * <p>If the variable resolver was created with {@link org.itsnat.core.ItsNatServletContext#createItsNatVariableResolver()}
     * then the method <code>ServletContext.getAttribute(String)</code> is called to continue searching.</p>
     *
     * <p>If the variable resolver was created with {@link org.itsnat.core.ItsNatSession#createItsNatVariableResolver()}
     * then the method {@link org.itsnat.core.ItsNatSession#getAttribute(String)} is called to continue searching,
     * if returns null then delegates to the method <code>ServletContext.getAttribute(String)</code>.</p>
     *
     * <p>If the variable resolver was created with {@link org.itsnat.core.ItsNatDocument#createItsNatVariableResolver()}
     * then the method {@link org.itsnat.core.ItsNatDocument#getAttribute(String)} is called to continue searching,
     * if returns null then delegates to the method {@link org.itsnat.core.ItsNatSession#getAttribute(String)},
     * if returns null then delegates to the method <code>ServletContext.getAttribute(String)</code>.</p>
     *
     * @param name the name to search for.
     * @return the value associated to the specified name or null if none is found.
     */
    public Object getVariable(String name);

    /**
     * Locates and replaces any variable declaration with format ${name} contained into the specified string with
     * the variable value converted to string (<code>Object.toString()</code>).
     *
     * <p>To obtain the value the method {@link #getVariable(String)} is called,
     * if no variable is found the variable declaration is kept as is.</p>
     *
     * <p>The same variable may be repeated.</p>
     *
     * @param str the source string to resolve declared variables.
     * @return the new string with replaced variables by values. If no variable was replaced (the original string is untouched) returns null.
     * @see #resolve(org.w3c.dom.Node)
     */
    public String resolve(String str);

    /**
     * Iterates recursively into the DOM subtree resolving (replacing) if possible any variable found with the
     * format ${name} into element attributes and text based nodes.
     *
     * <p>The method {@link #resolve(String)} is used to process element attributes
     * and content of text based nodes.
     *
     * @param node the node to resolve.
     * @return true if some variable was resolved (replaced).
     */
    public boolean resolve(Node node);
}
