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

package org.itsnat.core.domutil;

import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class provides methods to do frequent DOM related tasks. Most of them
 * access DOM Element objects filtering/ignoring any other node type.
 *
 * @author Jose Maria Arranz Santamaria
 */
public class ItsNatDOMUtil
{

    /**
     * Returns the first element with the specified id attribute below or the same
     * as the specified node.
     *
     * <p>Use this method when several elements in the tree have the same id attribute
     * (duplicated id) because <code>Document.getElementById(String)</code> returns the first one.
     * </p>
     *
     * @param id the id attribute value to search.
     * @param container the top most node to search below.
     * @return the first element object with the specified id or null if not found.
     */
    public static Element getElementById(String id,Node container)
    {
        return DOMUtilInternal.getElementById(id, container);
    }

    /**
     * Creates a new DOM Element with the specified tag name containing a text node
     * with the specified text.
     *
     * @param tagName the tag name of the new element.
     * @param text the string contained by the element as a text node, if null or empty no text node is added.
     * @param doc the document owner of the new element.
     * @return the new element.
     * @see #createElementNS(String,String,String,Document)
     */
    public static Element createElement(String tagName,String text,Document doc)
    {
        return DOMUtilInternal.createElement(tagName, text, doc);
    }

    /**
     * Creates a new DOM Element with the specified namespace URI and qualified name containing a text node
     * with the specified text.
     *
     * @param namespaceURI the namespace URI of the new element.
     * @param qualifiedName the qualified name of the new element.
     * @param text the string contained by the element as a text node, if null or empty no text node is added.
     * @param doc the document owner of the new element.
     * @return the new element.
     * @see #createElement(String,String,Document)
     */
    public static Element createElementNS(String namespaceURI,String qualifiedName,String text,Document doc)
    {
        return DOMUtilInternal.createElementNS(namespaceURI, qualifiedName, text, doc);
    }

    /**
     * Returns the string content of the text node inside the specified Element.
     *
     * <p>If the element contains a child node different to a text node a cast exception is thrown.</p>
     *
     * @param elem the parent element.
     * @return the string value of the text child node. If element is empty returns an empty String.
     */
    public static String getTextContent(Element elem)
    {
        return DOMUtilInternal.getTextContent(elem);
    }

    /**
     * Sets the string content of the text node inside the specified Element.
     *
     * <p>If the element already contains a text node, then is updated with the
     * new value. If the new string value is null or empty the child text node is removed.
     * If the first child node is different to a text node a cast exception is thrown.</p>
     *
     * @param elem the parent element.
     * @param value the new string value of the text child node.
     */
    public static void setTextContent(Element elem,String value)
    {
        DOMUtilInternal.setTextContent(elem, value);
    }

    /**
     * Removes and returns the content of the specified node.
     *
     * <p>If the node is empty returns null. If only contains a child node
     * returns this child node. If contains more than one child node returns
     * a <code>DocumentFragment</code> with the child nodes.</p>
     *
     * @param parentNode the node to remove the content.
     * @return a node with the content. May be null a tree node or a <code>DocumentFragment</code>.
     */
    public static Node extractChildren(Node parentNode)
    {
        return DOMUtilInternal.extractChildren(parentNode);
    }

    /**
     * Makes empty the specified node removing all child nodes.
     *
     * @param parentNode the node to remove the content.
     */
    public static void removeAllChildren(Node parentNode)
    {
        DOMUtilInternal.removeAllChildren(parentNode);
    }

    /**
     * Returns true if the provided <code>node</code> is inside the specified <code>parentNode</code>.
     *
     * @param node the node to know whether inside parentNode subtree.
     * @param parentNode the node to know whether it is an ancestor of node. If null the document associated to the <code>node</code> parameter is used.
     * @return true if <code>node</code> is inside <code>parentNode</code>.
     */
    public static boolean isNodeInside(Node node,Node parentNode)
    {
        return DOMUtilInternal.isNodeInside(node, parentNode);
    }

}
