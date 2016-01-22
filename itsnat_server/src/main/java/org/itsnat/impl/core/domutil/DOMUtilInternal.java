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

package org.itsnat.impl.core.domutil;

import java.util.LinkedList;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 *
 * @author jmarranz
 */
public class DOMUtilInternal
{
    /**
     * Creates a new instance of DOMUtilInternal
     */
    public DOMUtilInternal()
    {
    }
    
    public static String getLocalName(Element elem)
    {
        String localName = elem.getLocalName(); 
        if (localName != null) return localName;
        return elem.getNodeName(); // localName es nulo cuando el elemento no tiene asociado un namespace, es el caso de layouts de Android
    }
    
    public static Element getElementById(String id,Node container)
    {
        /* Este método resuelve el problema de elementos con id duplicado,
         * permitiendo buscar el elemento en un subconjunto del árbol.
         */
        if (container.getNodeType() == Node.ELEMENT_NODE) // puede ser Document inicialmente
        {
            Element elem = (Element)container;
            if (id.equals(elem.getAttribute("id")))
                return elem;
        }
        Node child = container.getFirstChild();
        while(child != null)
        {
            if (child.getNodeType() == Node.ELEMENT_NODE)
            {
                Element matchElem = getElementById(id,child);
                if (matchElem != null)
                    return matchElem;
            }
            child = child.getNextSibling();
        }
        return null;
    }

    public static Element createElement(String tagName,String text,Document doc)
    {
        Element elem = doc.createElement(tagName);
        if ((text != null)&& !text.equals(""))
            elem.appendChild(doc.createTextNode(text));
        return elem;
    }

    public static Element createElementNS(String namespaceURI,String qualifiedName,String text,Document doc)
    {
        if (namespaceURI == null)
            return createElement(qualifiedName,text,doc);
        else
        {
            Element elem = doc.createElementNS(namespaceURI,qualifiedName);
            if ((text != null)&& !text.equals(""))
                elem.appendChild(doc.createTextNode(text));
            return elem;
        }
    }

    public static String getTextContent(Element elem)
    {
        return getTextContent(elem,true);
    }

    public static String getTextContent(Element elem,boolean onlyFirstChild)
    {
        Text textNode = (Text)elem.getFirstChild();
        if (textNode == null)
            return ""; // No se puede distinguir entre nulo y vacía
        String res = textNode.getData();
        if (onlyFirstChild)
            return res;
        else
        {
            // El onlyFirstChild = false es útil cuando algún idiota ha añadido más de un nodo de texto
            // por ejemplo a un <textarea>
            if (textNode == elem.getLastChild()) // Sólo hay uno
                return res;
            StringBuilder resBuff = new StringBuilder();
            resBuff.append(res);
            textNode = (Text)textNode.getNextSibling();
            while(textNode != null)
            {
                resBuff.append(textNode.getData());
                textNode = (Text)textNode.getNextSibling();
            }
            return resBuff.toString();
        }
    }

    public static void setTextContent(Element elem,String value)
    {
        if (value == null) value = "";
        Text textNode = (Text)elem.getFirstChild();
        if (textNode == null)
        {
            if (!value.equals(""))
            {
                textNode = elem.getOwnerDocument().createTextNode(value);
                elem.appendChild(textNode);
            }
        }
        else if (value.equals(""))
            elem.removeChild(textNode);
        else
            textNode.setData(value);
    }

    public static Node extractChildren(Node parentNode)
    {
        return extractChildren(parentNode,false);
    }

    public static Node extractChildren(Node parentNode,boolean removeLastChildFirst)
    {
        Node child = parentNode.getFirstChild();
        if (child == null) return null;
        if (child.getNextSibling() == null)
        {
            // Es el único, de esta manera nos evitamos crear un DocumentFragment
            parentNode.removeChild(child);
            return child;
        }
        else
        {
            return DOMUtilInternal.extractChildrenToDocFragment(parentNode,removeLastChildFirst);
        }
    }


    public static void removeAllChildren(Node parentNode)
    {
        // Eliminamos desde el último al primero para evitar el efecto visual de "subida de persiana"
        Node child = parentNode.getLastChild();
        while (child != null)
        {
            parentNode.removeChild(child);
            child = parentNode.getLastChild();
        }
    }

    public static boolean isNodeInsideDocument(Node node)
    {
        Node parentNode = node.getOwnerDocument();
        Node currParent = node.getParentNode();
        while((currParent != parentNode) &&
              (currParent != null))
        {
            node = node.getParentNode();
            currParent = node.getParentNode();
        }
        return currParent == parentNode;
    }

    public static boolean isNodeInside(Node node,Node parentNode)
    {
        if (parentNode == null) parentNode = node.getOwnerDocument();
        Node currParent = node.getParentNode();
        while((currParent != parentNode) &&
              (currParent != null))
        {
            node = node.getParentNode();
            currParent = node.getParentNode();
        }
        return currParent == parentNode;
    }


    public static boolean isTheOnlyChildNode(Node childNode)
    {
        Node parentNode = childNode.getParentNode(); // Suponemos no nulo
        return ((parentNode.getFirstChild() == childNode) &&
                (parentNode.getLastChild() == childNode));
    }

    public static void setAttribute(Element elem,String name,String value)
    {
        // Hacemos esto para evitar lo más posible los mutation events inútiles
        // pues al parecer aunque el valor sea el mismo Batik DOM elimina "el nodo de texto"
        // con el valor actual y genera mutation events de modificación del documento aunque no del tipo que procesamos nosotros
        // aunque curiosamente no genera un mutation event de modificación del atributo
        // (detecta que no ha sido modificado).
        // Por tanto aunque no haya riesgo de enviar código redundante al cliente nos
        // ahorramos operaciones internas inútiles.
        String oldValue = elem.getAttribute(name);
        if (value.equals(oldValue))
            return;
        elem.setAttribute(name,value);
    }

    public static void setBooleanAttribute(Element elem, String name, boolean value )
    {
        if (value)
            setAttribute(elem,name,name);
        else
            elem.removeAttribute(name);
    }

    public static String toString(Object obj)
    {
        return obj != null ? obj.toString() : null;
    }

    public static boolean isChildOrSame(Node node,Element parentElement)
    {
        return isChildOrSame(node,parentElement,null);
    }

    public static boolean isChildOrSame(Node node,Element parentElement,Element topLimitParent)
    {
        if (node == null) return false;

        if (node == parentElement) return true;

        return isChild(node,parentElement,topLimitParent);
    }

    public static boolean isChild(Node node,Element parentElement)
    {
        return isChild(node,parentElement,null);
    }

    public static boolean isChild(Node node,Element parentElement,Element topLimitParent)
    {
        if (node == null) return false;

        if (node == parentElement) return false;

        Node nodeParent = getChildTopMostContainingNode(node,parentElement,topLimitParent);
        return (nodeParent != null); // Es que es hijo
    }

    public static Node getChildTopMostContainingNode(Node node,Element parentElement)
    {
        return getChildTopMostContainingNode(node,parentElement,null);
    }

    public static Node getChildTopMostContainingNode(Node node,Element parentElement,Element topLimitParent)
    {
        // Devuelve el nodo padre de "node" que a su vez es hijo directo de parentElement,
        // si node no está dentro de parentElement se devuelve null
        // topLimitParent sirve para evitar subir más arriba inútilmente si no está dentro
        // de parentElement pues sabemos que pasará por topLimitParent necesariamente, topLimitParent ha de ser un elemento del cual tenemos la seguridad
        // de que node es hijo, sirve para acelerar la búsqueda en caso fallido, puede ser null o el propio parentElement
        // no tiene sentido que sea hijo de parentElement
        if (node == null) return null;

        Node parent = node.getParentNode();
        while((parent != null) && (parent != parentElement) && (parent != topLimitParent))
        {
            node = parent;
            parent = node.getParentNode();
        }

        if (parent == parentElement)
            return node;

        return null; // No es hijo, resto de los casos
    }
    
    public static Element getChildElementWithTagName(Node parent,final String tagName,final int index)
    {
        NodeConstraints rules = new NodeConstraints()
        {
            protected int elemCount = 0;
            @Override
            public boolean match(Node node, Object context)
            {
                if (!(node instanceof Element)) return false;
                Element elem = (Element)node;
                if (tagName.equals(elem.getTagName()) &&
                    (elemCount == index))
                    return true;
                elemCount++;
                return false;
            }
        };
        return (Element)getFirstContainedNodeMatching(parent,rules,null);
    }

    public static Element getChildElementWithTagNameNS(Node parent,
            final String namespaceURI,final String localName,final int index)
    {
        if (namespaceURI == null)
            return getChildElementWithTagName(parent,localName,index);

        NodeConstraints rules = new NodeConstraints()
        {
            protected int elemCount = 0;
            @Override
            public boolean match(Node node, Object context)
            {
                if (!(node instanceof Element)) return false;
                Element elem = (Element)node;
                if (namespaceURI.equals(elem.getNamespaceURI()) &&
                    localName.equals(elem.getLocalName()) &&
                    (elemCount == index))
                    return true;
                elemCount++;
                return false;
            }
        };
        return (Element)getFirstContainedNodeMatching(parent,rules,null);
    }
    
    public static boolean hasContainedElementWithTagName(Node parent,String tagName)
    {
        return (getChildElementWithTagName(parent,tagName,0) != null);
    }

    public static boolean hasContainedElementWithTagNameNS(Node parent,String namespaceURI,String localName)
    {
        return (getChildElementWithTagNameNS(parent,namespaceURI,localName,0) != null);
    }
    
    public static boolean hasContainedNodeMatching(Node parent,NodeConstraints rules,Object context)
    {
        return (getFirstContainedNodeMatching(parent,rules,context) != null);
    }

    public static Node getFirstContainedNodeMatching(Node parent,NodeConstraints rules,Object context)
    {
        // El propio nodo no se considera
        Node child = parent.getFirstChild();
        while(child != null)
        {
            if (rules.match(child, context))
                return child;
            Node result = getFirstContainedNodeMatching(child,rules,context);
            if (result != null)
                return result;
            child = child.getNextSibling();
        }
        return null;
    }

    public static boolean isNodeOrSomeContainedNodeMatching(Node parent,NodeConstraints rules,Object context)
    {
        return (getNodeOrFirstContainedNodeMatching(parent,rules,context) != null);
    }

    public static Node getNodeOrFirstContainedNodeMatching(Node parent,NodeConstraints rules,Object context)
    {
        if (rules.match(parent,context))
            return parent;
        Node child = parent.getFirstChild();
        while(child != null)
        {
            Node result = getNodeOrFirstContainedNodeMatching(child,rules,context);
            if (result != null)
                return result;
            child = child.getNextSibling();
        }
        return null;
    }

    // Este método es mucho más eficiente que Document.getElementsByTagName
    // que tiene el problema de que es tolerante a cambios => más lento
    public static LinkedList<Node> getElementListWithTagName(Node node,final String tagName,boolean recursive)
    {
        NodeConstraints rules = new NodeConstraints()
        {
            @Override
            public boolean match(Node node, Object context)
            {
                if (!(node instanceof Element)) return false;
                Element elem = (Element)node;
                return elem.getTagName().equals(tagName);
            }
        };        
        return getNodeListMatching(node,rules,recursive,null);
    }    
    
    // Este método es mucho más eficiente que Document.getElementsByTagName
    // que tiene el problema de que es tolerante a cambios => más lento
    public static LinkedList<Node> getChildElementListWithTagName(Node parent,final String tagName,boolean recursive)
    {
        NodeConstraints rules = new NodeConstraints()
        {
            @Override            
            public boolean match(Node node, Object context)
            {
                if (!(node instanceof Element)) return false;
                Element elem = (Element)node;
                return elem.getTagName().equals(tagName);
            }
        };
        return getChildNodeListMatching(parent,rules,recursive,null);
    }

    // Este método es mucho más eficiente que Document.getElementsByTagName
    // que tiene el problema de que es tolerante a cambios => más lento
    public static LinkedList<Node> getChildElementListWithTagNameNS(Node parent,
            final String namespaceURI,final String localName,boolean recursive)
    {
        if (namespaceURI == null)
            return getChildElementListWithTagName(parent,localName,recursive);
        
        NodeConstraints rules = new NodeConstraints()
        {
            @Override
            public boolean match(Node node, Object context)
            {
                if (!(node instanceof Element)) return false;
                Element elem = (Element)node;
                return (namespaceURI.equals(elem.getNamespaceURI()) &&
                        localName.equals(elem.getLocalName()));
            }
        };
        return getChildNodeListMatching(parent,rules,recursive,null);
    }
  
    private static LinkedList<Node> getNodeListMatching(Node parent,NodeConstraints rules,boolean recursive,Object context)
    {
        LinkedList<Node> nodeList = null;
        if (rules.match(parent,context))
        {
            if (nodeList == null) nodeList = new LinkedList<Node>();
            nodeList.add(parent);
        }        
        
        return getChildNodeListMatching(parent,rules,recursive,nodeList,context);
    }        
    
    public static LinkedList<Node> getChildNodeListMatching(Node parent,NodeConstraints rules,boolean recursive,Object context)
    {
        return getChildNodeListMatching(parent,rules,recursive,null,context);
    }

    
    private static LinkedList<Node> getChildNodeListMatching(Node parent,NodeConstraints rules,boolean recursive,LinkedList<Node> nodeList,Object context)
    {
        // El propio nodo parent no se considera
        Node child = parent.getFirstChild();
        while(child != null)
        {
            if (rules.match(child,context))
            {
                if (nodeList == null) nodeList = new LinkedList<Node>();
                nodeList.add(child);
            }
            if (recursive)
                nodeList = getChildNodeListMatching(child,rules,recursive,nodeList,context);
            child = child.getNextSibling();
        }
        return nodeList;
    }

    public static void replaceContent(Element parentElem,Node newChild)
    {
        // newChild puede ser Element, DocumentFragment, Text etc
        // Intentamos evitar cambiar el árbol si no es necesario, así evitamos tráfico de red
        if (newChild instanceof DocumentFragment)
        {
            DocumentFragment docFrag = (DocumentFragment)newChild;
            if (isSameContent(parentElem,docFrag))
                return; // Nada que hacer
        }
        else
        {
            Node child = parentElem.getFirstChild();
            if ((child.getNextSibling() == null) &&
                 isSameContent((Node)newChild,child))
                    return; // Nada que hacer pues queremos sustituir el mismo elemento ya presente
        }

        // Eliminamos el contenido del elemento antes de insertar
        DOMUtilInternal.removeAllChildren(parentElem);
        parentElem.appendChild((Node)newChild);
    }

    public static void removeAllChildrenNotElement(Element parentElem)
    {
        Node node = parentElem.getFirstChild();
        while (node != null)
        {
            if (node.getNodeType() != Node.ELEMENT_NODE)
            {
                Node next = node.getNextSibling();
                parentElem.removeChild(node);
                node = next;
            }
            else
            {
                node = node.getNextSibling();
            }
        }
    }

    public static boolean isSeparator(Text textNode)
    {
        String text = textNode.getData();
        for(int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);
            if ((c != ' ')&&(c != '\n')&&(c != '\t'))
                return false;
        }
        return true;
    }

    public static void removeAllUnusefulChildTextNodes(Element parentElem)
    {
        Node node = parentElem.getFirstChild();
        while (node != null)
        {
            if (node.getNodeType() == Node.TEXT_NODE)
            {
                // Hay que tener cuidado de no eliminar por ejemplo nodos de texto
                // que representan a nodos cacheados, por eso sólo eliminamos nodos
                // con espacios y fines de línea
                Text text = (Text)node;
                if (isSeparator(text))
                {
                    Node next = node.getNextSibling();
                    parentElem.removeChild(node);
                    node = next;
                }
                else node = node.getNextSibling();
            }
            else
            {
                node = node.getNextSibling();
            }
        }
    }

    public static void removeAllDirectChildComments(Element parentElem)
    {
        Node node = parentElem.getFirstChild();
        while (node != null)
        {
            if (node.getNodeType() == Node.COMMENT_NODE)
            {
                Node next = node.getNextSibling();
                parentElem.removeChild(node);
                node = next;
            }
            else
            {
                node = node.getNextSibling();
            }
        }
    }

    public static Text getFirstTextNode(Element elem)
    {
        Node child = elem.getFirstChild();
        while(child != null)
        {
            if (child.getNodeType() == Node.TEXT_NODE)
                return (Text)child;

            child = child.getNextSibling();
        }

        return null;
    }

    public static void setCharacterDataContent(CharacterData node,String value)
    {
        if (value == null) value = "";
        if (!value.equals(node.getData())) // Para evitar un mutation event inútil
            node.setData(value); // Nos interesa mantener el objeto aunque contenga "" para saber donde ha de escribirse
    }

    public static boolean isSameContent(Element parentElem,DocumentFragment docFrag)
    {
        if (!parentElem.hasChildNodes() && !docFrag.hasChildNodes())
            return true;

        Node childNode1 = parentElem.getFirstChild();
        Node childNode2 = docFrag.getFirstChild();
        while(childNode1 != null || childNode2 != null)
        {
            if (!isSameContent(childNode1,childNode2)) // Si uno es null y el otro no entonces son también distintos (isSameContent está preparado)
                return false;
            // Sólo se pasa por aquí sin hasta ahora son los dos no nulos y con el mismo contenido

            childNode1 = childNode1.getNextSibling();
            childNode2 = childNode2.getNextSibling();
        }

        return true;
    }

    public static boolean isSameContent(Attr attr1,Attr attr2)
    {
        if (!attr1.getName().equals(attr2.getName()))
            return false;
        if (!attr1.getValue().equals(attr2.getValue()))
            return false;
        if (attr1.getNamespaceURI() != null)
            if (attr1.getNamespaceURI().equals(attr2.getNamespaceURI()))
            return false;
        return true;
    }

    public static boolean isSameContent(Node node1,Node node2)
    {
        // Útil para detectar que un nodo es un clon de otro

        if (node1 == node2) return true;

        if (node1.getNodeType() != node2.getNodeType())
            return false;

        int type = node1.getNodeType();
        if (type == Node.ELEMENT_NODE)
        {
            if (!((Element)node1).getTagName().equals(((Element)node2).getTagName()))
                return false;

            if (node1.getNamespaceURI() != null)
                if (node1.getNamespaceURI().equals(node2.getNamespaceURI()))
                return false;

            if (node1.hasAttributes() != node2.hasAttributes())
                return false;

            if (node1.hasAttributes())
            {
                NamedNodeMap attribs1 = node1.getAttributes();
                NamedNodeMap attribs2 = node2.getAttributes();
                if (attribs1.getLength() != attribs2.getLength())
                    return false;
                int len = attribs1.getLength();
                for(int i = 0; i < len; i++)
                {
                    Attr attr1 = (Attr)attribs1.item(i);
                    Attr attr2 = (Attr)attribs2.item(i);
                    if (!isSameContent(attr1,attr2))
                        return false;
                }
            }
        }

        if (!node1.hasChildNodes() && !node2.hasChildNodes())
            return true;

        if (node1.hasChildNodes())
        {
            Node childNode1 = node1.getFirstChild();
            Node childNode2 = node2.getFirstChild();
            while(childNode1 != null || childNode2 != null)
            {
                if (!isSameContent(childNode1,childNode2)) // Si uno es null y el otro no entonces son también distintos (isSameContent está preparado)
                    return false;
                // Sólo se pasa por aquí sin hasta ahora son los dos no nulos y con el mismo contenido

                childNode1 = childNode1.getNextSibling();
                childNode2 = childNode2.getNextSibling();
            }
        }
     
        return true;
    }


    public static boolean isNodeBoundToDocumentTree(Node node)
    {
        if (node == null) return false;

        Node parent = node;
        do
        {
            node = parent;
            parent = node.getParentNode();
        }
        while(parent != null);

        return (node.getNodeType() == Node.DOCUMENT_NODE);
    }

    public static DocumentFragment extractChildrenToDocFragment(Node parentNode)
    {
        return extractChildrenToDocFragment(parentNode,false);
    }

    public static DocumentFragment extractChildrenToDocFragment(Node parentNode,boolean removeLastChildFirst)
    {
        Document doc = parentNode.getOwnerDocument();
        DocumentFragment docFrag = doc.createDocumentFragment();
        if (removeLastChildFirst) // Eliminar el último hijo el primero
        {
            Node child = parentNode.getLastChild();
            while (child != null)
            {
                parentNode.removeChild(child); // realmente no hace falta pues al añadir al DocumentFragment se quita del árbol
                docFrag.insertBefore(child,docFrag.getFirstChild());
                child = parentNode.getLastChild();
            }
        }
        else // eliminar el primer hijo el primero
        {
            Node child = parentNode.getFirstChild();
            while (child != null)
            {
                parentNode.removeChild(child); // realmente no hace falta pues al añadir al DocumentFragment se quita del árbol
                docFrag.appendChild(child);
                child = parentNode.getFirstChild();
            }
        }
        return docFrag; // Nunca es null
    }
}
