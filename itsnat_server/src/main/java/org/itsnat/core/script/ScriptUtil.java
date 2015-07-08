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

import org.w3c.dom.Node;

/**
 * Utility interface to generate JavaScript code mainly to locate client nodes from server DOM nodes.
 *
 * <p>ItsNat provides two default implementations of this interface, these implementations
 * are got calling {@link org.itsnat.core.ItsNatDocument#getScriptUtil()} or
 * {@link org.itsnat.core.ClientDocument#getScriptUtil()}
 * </p>
 * 
 * <p>If this utility object was got calling the method {@link org.itsnat.core.ItsNatDocument#getScriptUtil()}
 * the generated JavaScript code must be sent to the client calling {@link org.itsnat.core.ItsNatDocument#addCodeToSend(Object)},
 * this JavaScript code will be received by all clients (owner and attached) of this document.
 * </p>
 *
 * <p>If this utility object was got calling the method {@link org.itsnat.core.ClientDocument#getScriptUtil()}
 * the generated JavaScript code must be sent to the client calling {@link org.itsnat.core.ClientDocument#addCodeToSend(Object)}
 * this JavaScript code will be received by the specified client (use ever the same client for generating and sending code).
 * </p>
 *
 * <p>When a DOM node is used a parameter this node must be part of the document tree
 * (server and client) otherwise an exception is thrown, and the generated code
 * must be sent as soon as possible to the client/s calling the appropriated method as explained before. *
 * </p>
 *
 * <p>ItsNat generates JavaScript code referencing the specified node in client, when this
 * utility object was got calling {@link org.itsnat.core.ItsNatDocument#getScriptUtil()}
 * this reference is valid for all clients of the document (in spite of internal node caching
 * ItsNat ensures the same internal caching id is used for all clients or no caching is used).
 * </p>
 * 
 * @author Jose Maria Arranz Santamaria
 */
public interface ScriptUtil
{
    /**
     * Generates the appropriated code to locate the specified DOM node at the client.
     *
     * <p>Returned code may be considered as a JavaScript reference to the specified node.</p>
     *
     * <p>The following example generates JavaScript to set 'City' to the "value" property of
     * the specified DOM element aNode:</p>
     *
     * <blockquote><pre>
     *  ItsNatDocument itsNatDoc = ...;
     *  Element aNode = ...;
     *  String code = itsNatDoc.getScriptUtil().getNodeReference(aNode) + ".value = 'City';";
     *  itsNatDoc.addCodeToSend(code);
     * </pre></blockquote>
     *
     * @param node the node to generate a JavaScript reference.
     * @return the JavaScript reference to send to the client.
     */
    public String getNodeReference(Node node);

    /**
     * Creates a new script expression object wrapping the specified object.
     *
     * @return a new script expression object.
     */
    public ScriptExpr createScriptExpr(Object value);

    /**
     * Generates the appropriated code to call the specified object method at the client.
     *
     * @param obj the object reference, converted to JavaScript calling {@link #toScript(Object)}.
     * @param methodName method name.
     * @param params the parameter list. Are converted to JavaScript calling {@link ScriptUtil#toScript(Object)}.
     * @param endSentence if true adds a ; at the end.
     * @return the JavaScript code.
     */
    public String getCallMethodCode(Object obj,String methodName,Object[] params,boolean endSentence);

    /**
     * Generates the appropriated code to call the specified object method at the client.
     *
     * @param obj the object reference, converted to JavaScript calling {@link #toScript(Object)}.
     * @param methodName method name.
     * @param params the parameter list. Are converted to JavaScript calling {@link ScriptUtil#toScript(Object)}.
     * @return the JavaScript code.
     */
    public String getCallMethodCode(Object obj,String methodName,Object[] params);

    /**
     * Generates the JavaScript code to set a value to the specified property.
     *
     * @param obj the object reference, converted to JavaScript calling {@link #toScript(Object)}.
     * @param propName property name.
     * @param value the value to set. Is converted to JavaScript calling {@link ScriptUtil#toScript(Object)}.
     * @param endSentence if true adds a ; at the end.
     * @return the JavaScript code.
     */
    public String getSetPropertyCode(Object obj,String propName,Object value,boolean endSentence);


    /**
     * Generates the JavaScript code to set a value to the specified property.
     *
     * @param obj the object reference, converted to JavaScript calling {@link #toScript(Object)}.
     * @param propName property name.
     * @param value the value to set. Is converted to JavaScript calling {@link ScriptUtil#toScript(Object)}.
     * @return the JavaScript code.
     */
    public String getSetPropertyCode(Object obj,String propName,Object value);

    /**
     * Generates the JavaScript code to get the value of the specified property.
     *
     * @param obj the object reference, converted to JavaScript calling {@link #toScript(Object)}.
     * @param propName property name.
     * @param endSentence if true adds a ; at the end.
     * @return the JavaScript code.
     */
    public String getGetPropertyCode(Object obj,String propName,boolean endSentence);

    /**
     * Generates the JavaScript code to get the value of the specified property.
     *
     * @param obj the object reference, converted to JavaScript calling {@link #toScript(Object)}.
     * @param propName property name.
     * @return the JavaScript code.
     */
    public String getGetPropertyCode(Object obj,String propName);

    /**
     * Converts the specified Java <code>String</code> to a JavaScript string literal, this
     * string can be sent to the client.
     *
     * <p>Any special character like end of lines, tabs, " , ' , \ etc are escaped to sent
     * to the client as a JavaScript string literal.</p>
     *
     * @param text the <code>String</code> to convert.
     * @return the resulting JavaScript string literal.
     */
    public String getTransportableStringLiteral(String text);

    /**
     * Converts the specified Java <code>String</code> to a JavaScript string literal, this
     * string can be sent to the client.
     *
     * <p>Any special character like end of lines, tabs, " , ' , \ etc are escaped to sent
     * to the client as a JavaScript string literal.</p>
     *
     * @param c the Java <code>char</code> to convert.
     * @return the resulting JavaScript string literal.
     */
    public String getTransportableCharLiteral(char c);

    /**
     * Is a Java implementation of the JavaScript <code>encodeURIComponent</code> function.
     * The string encoded with this method can be unencoded using the JavaScript <code>decodeURIComponent</code> function.
     *
     * <p>This method is an alternative (slower and bigger) to {@link #getTransportableStringLiteral(String)}
     * to transport texts to the client.</p>
     *
     * <p>The encoded Java String is not a string literal. The following
     * example encloses the <code>String</code> as a literal: </p>
     *
     * <p><code> String code = "\"" + encodeURIComponent(someText) + "\""; </code></p>
     *
     * @param text the Java <code>String</code> to encode.
     * @return the encoded text.
     */
    public String encodeURIComponent(String text);

    /**
     * Is a Java implementation of the JavaScript <code>encodeURIComponent</code> function
     * applied to a Java <code>char</code>.
     *
     * <p>This method is an alternative (slower and bigger) to {@link #getTransportableCharLiteral(char)}
     * to transport characters to the client.</p>
     *
     * <p>The encoded Java char is not a char literal. The following
     * example encloses the <code>char</code> as a literal: </p>
     *
     * <p><code> String code = "'" + encodeURIComponent(someChar) + "'"; </code></p>
     *
     * @param c the Java <code>char</code> to encode.
     * @return the encoded char.
     */
    public String encodeURIComponent(char c);

    /**
     * Converts the specified object value to JavaScript code.
     *
     * <p>Conversion rules if value is a:</p>
     * <ul>
     *   <li>DOM Node: conversion is done calling {@link #getNodeReference(org.w3c.dom.Node)}.</li>
     *   <li>Boolean: calling value.toString() (returns the literals <code>true</code> and <code>false</code>.</li>
     *   <li>Character: calling {@link #getTransportableCharLiteral(char)}.</li>
     *   <li>Number: calling value.toString().</li>
     *   <li>ScriptExpr: calling {@link ScriptExpr#getCode()}.</li>
     *   <li>String: calling {@link #getTransportableStringLiteral(String)}.</li>
     *   <li>Other: calling value.toString().</li>
     * </ul>
     *
     * @param value the object to convert to JavaScript.
     * @return the JavaScript code.
     */
    public String toScript(Object value);
}
