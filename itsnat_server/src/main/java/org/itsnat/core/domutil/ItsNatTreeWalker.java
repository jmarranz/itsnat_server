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

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Utility class used to easily iterate a DOM tree, specially DOM elements.
 *
 * <p>Most of methods have been inspired by <code>org.w3c.dom.traversal.TreeWalker</code>.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public class ItsNatTreeWalker
{
    /**
     * Informs whether the specified node has child elements.
     *
     * @param node the node to inspect.
     * @return true if the specified node has child elements.
     */
    public static boolean hasChildElements(Node node)
    {
        return (getFirstChildElement(node) != null);
    }

    /**
     * Returns the number of child elements of the specified node.
     *
     * @param node the node to inspect.
     * @return the number of child elements.
     */
    public static int getChildElementCount(Node node)
    {
        // Filtramos los nodos de texto
        int count = 0;
        Element child = getFirstChildElement(node);
        while(child != null)
        {
            count++;
            child = getNextSiblingElement(child);
        }
        return count;
    }

    /**
     * Returns the first direct child <code>org.w3c.dom.Element</code> below the specified node.
     * Any child non-element is ignored.
     *
     * @param node the node parent.
     * @return the first child element. Null if the parent node has no child element.
     */
    public static Element getFirstChildElement(Node node)
    {
        // node normalmente será un Document o un Element
        // Evitamos con esto usar un TreeWalker evitando la creación de un objeto
        Node child = node.getFirstChild();
        while(child != null)
        {
            if (child.getNodeType() == Node.ELEMENT_NODE)
                return (Element)child;

            child = child.getNextSibling();
        }
        return null;
    }

    /**
     * Returns the last direct child <code>org.w3c.dom.Element</code> below the specified node.
     * Any child non-element is ignored.
     *
     * @param node the node parent.
     * @return the last child element. Null if the parent node has no child element.
     */
    public static Element getLastChildElement(Node node)
    {
        // Ver getFirstChildElement()

        Node child = node.getLastChild();
        while(child != null)
        {
            if (child.getNodeType() == Node.ELEMENT_NODE)
                return (Element)child;

            child = child.getPreviousSibling();
        }
        return null;
    }

    /**
     * Returns the next sibling <code>org.w3c.dom.Element</code> following the specified node.
     * Any non-element is ignored.
     *
     * @param node the original node.
     * @return the next sibling element. Null if the node has no next sibling element (last child element).
     */
    public static Element getNextSiblingElement(Node node)
    {
        // Ver getFirstChildElement()
        Node sibling = node.getNextSibling();
        while(sibling != null)
        {
            if (sibling.getNodeType() == Node.ELEMENT_NODE)
                return (Element)sibling;

            sibling = sibling.getNextSibling();
        }
        return null;
    }

    /**
     * Returns the previous sibling <code>org.w3c.dom.Element</code> following the specified node.
     * Any non-element is ignored.
     *
     * @param node the original node.
     * @return the previous sibling element. Null if the node has no previous sibling element (first child element).
     */
    public static Element getPreviousSiblingElement(Node node)
    {
        // Ver getFirstChildElement()
        Node sibling = node.getPreviousSibling();
        while(sibling != null)
        {
            if (sibling.getNodeType() == Node.ELEMENT_NODE)
                return (Element)sibling;

            sibling = sibling.getPreviousSibling();
        }
        return null;
    }

    /**
     * Returns the first parent <code>org.w3c.dom.Element</code> of specified node.
     * Any non-element parent is ignored.
     *
     * @param node the original node.
     * @return the first parent element. Null if the node has no parent element (for instance, the document root element).
     */
    public static Element getParentElement(Node node)
    {
        if (node == null) return null;
        Node parent = node.getParentNode();
        if (parent == null)
            return null;
        if (parent.getNodeType() == Node.ELEMENT_NODE)
            return (Element)parent;
        return null; // Puede ser el caso de que el parent es el Document
    }

    /**
     * Returns the previous <code>org.w3c.dom.Element</code> following the specified node in document order.
     * Any non-element is ignored.
     *
     * @param node the original node.
     * @return the previous element. Null if the node has no previous element.
     */
    public static Element getPreviousElement(Node node)
    {
        if (node == null) return null;

        Element prevSibling = getPreviousSiblingElement(node);
        if (prevSibling == null)
        {
            return getParentElement(node); // puede ser null
        }

        Element lastChild  = getLastChildElement(prevSibling);
        if (lastChild == null)
            return prevSibling;  // Sí mismo no tiene elementos hijo

        Element prevChild;
        do
        {
            prevChild = lastChild;
            lastChild = getLastChildElement(prevChild);
        }
        while (lastChild != null);

        lastChild = prevChild;

        return lastChild;
    }

    /**
     * Returns the next <code>org.w3c.dom.Element</code> following the specified node in document order.
     * Any non-element is ignored.
     *
     * @param node the original node.
     * @return the next element. Null if the node has no next element.
     */
    public static Element getNextElement(Node node)
    {
        if (node == null) return null;

        Element result = getFirstChildElement(node);
        if (result != null)
            return result;

        result = getNextSiblingElement(node);
        if (result != null)
            return result;

        // return parent's 1st sibling.
        Element parent = getParentElement(node);
        while (parent != null)
        {
            result = getNextSiblingElement(parent);
            if (result != null)
                return result;
            else
                parent = getParentElement(parent);
        }

        // end , return null
        return null;
    }

    /**
     * Returns the previous node following the specified node in document order.
     *
     * @param node the original node.
     * @return the previous node. Null if the node has no previous node.
     */
    public static Node getPreviousNode(Node node)
    {
        if (node == null) return null;

        Node prevSibling = node.getPreviousSibling();
        if (prevSibling == null)
        {
            return node.getParentNode(); // puede ser null
        }

        Node lastChild  = prevSibling.getLastChild();
        if (lastChild == null)
            return prevSibling;  // Sí mismo no tiene elementos hijo

        Node prevChild;
        do
        {
            prevChild = lastChild;
            lastChild = prevChild.getLastChild();
        }
        while (lastChild != null);

        lastChild = prevChild;

        return lastChild;
    }

    /**
     * Returns the next node following the specified node in document order.
     *
     * @param node the original node.
     * @return the next node. Null if the node has no next node.
     */
    public static Node getNextNode(Node node)
    {
        if (node == null) return null;

        Node result = node.getFirstChild();
        if (result != null)
            return result;

        result = node.getNextSibling();
        if (result != null)
            return result;

        // return parent's 1st sibling.
        Node parent = node.getParentNode();
        while (parent != null)
        {
            result = parent.getNextSibling();
            if (result != null)
                return result;
            else
                parent = parent.getParentNode();
        }

        // end , return null
        return null;
    }

    /**
     * Returns the first direct child element with the specified tag name.
     *
     * @param parent the parent node.
     * @param tagName the tag name to search for, the search is case sensitive.
     * @return the first direct child element with this tag or null if not found.
     */
    public static Element getFirstChildElementWithTagName(Node parent,String tagName)
    {
        // Este método es porque getElementsByTagName recorre recursivamente
        // y no es necesario en este caso y porque métodos tal como
        // HTMLTableElement.getTBodies() crean innecesariamente objetos.
        Element child = getFirstChildElement(parent);
        while(child != null)
        {
            if (child.getTagName().equals(tagName))
                return child;
            child = getNextSiblingElement(child);
        }
        return null;
    }

    /**
     * Returns the first direct child element with the specified namespace and local name.
     *
     * @param parent the parent node.
     * @param namespaceURI the namespace of the element to search for, the search is case sensitive.
     * @param localName the local name of the element to search for, the search is case sensitive.
     * @return the first direct child element with this namespace and tag name or null if not found.
     */
    public static Element getFirstChildElementWithTagNameNS(Node parent,String namespaceURI,String localName)
    {
        // Este método es porque getElementsByTagName recorre recursivamente
        // y no es necesario en este caso y porque métodos tal como
        // HTMLTableElement.getTBodies() crean innecesariamente objetos.
        if (namespaceURI == null)
            return getFirstChildElementWithTagName(parent,localName);

        Element child = getFirstChildElement(parent);
        while(child != null)
        {
            if (namespaceURI.equals(child.getNamespaceURI()) && 
                localName.equals(child.getLocalName()))
                return child;
            child = getNextSiblingElement(child);
        }
        return null;
    }

    /**
     * Returns the first and deepest element below the specified node.
     *
     * <p>For instance if <code>node</code> is the <code>&lt;table&gt;</code> element:</p>
     * <pre>
     *   &lt;table>
     *      &lt;tbody>
     *          &lt;tr>
     *              &lt;td>One&lt;/td>
     *          &lt;/tr>
     *          &lt;tr>
     *              &lt;td>Two&lt;/td>
     *          &lt;/tr>
     *      &lt;/tbody>
     *   &lt;/table>
     * </pre>
     * <p>Returns the <code>&lt;td&gt;</code> element parent of <code>One</code></p>
     * 
     * @param node the parent node to search.
     * @return the first deepest child element. Null if there is no child element.
     */
    public static Element getFirstDeepestChildElement(Node node)
    {
        Element parent = null;
        Element child = getFirstChildElement(node);
        while(child != null)
        {
            parent = child;
            child = getFirstChildElement(parent);
        }
        return parent;
    }
}
