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

package org.itsnat.impl.core.scriptren.bsren.dom.node;


import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.scriptren.shared.dom.node.InsertAsMarkupInfoImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class BSRenderElementImpl extends BSRenderHasChildrenNodeImpl //implements NodeConstraints
{
    public static final BSRenderElementImpl SINGLETON = new BSRenderElementImpl();
    
    /** Creates a new instance of BSRenderElementImpl */
    public BSRenderElementImpl()
    {
    }

    public static BSRenderElementImpl getBSRenderElement()
    {
        return BSRenderElementImpl.SINGLETON;
    }

    protected String createNodeCode(Node node,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        Element nodeElem = (Element)node;
        return createElement(nodeElem,clientDoc);
    }

    protected String createElement(Element nodeElem,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        String tagName = nodeElem.getTagName();
        return createElement(nodeElem,tagName,clientDoc);
    }

    protected String createElement(Element nodeElem,String tagName,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        String namespaceURI = nodeElem.getNamespaceURI();
        if (namespaceURI != null)
        {
            String namespaceURIScript = shortNamespaceURI(namespaceURI);            
            return "itsNatDoc.createElementNS(" + namespaceURIScript + ",\"" + tagName + "\")";
        }
        else
            return "itsNatDoc.createElement(\"" + tagName + "\")";      
    }

    protected String addAttributesBeforeInsertNode(Node node,String elemVarName,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        Element elem = (Element)node;
        BSRenderAttributeImpl render = BSRenderAttributeImpl.getBSRenderAttribute();          
        StringBuilder code = new StringBuilder();
        NamedNodeMap attribList = elem.getAttributes();      
        for(int i = 0; i < attribList.getLength(); i++)
        {
            Attr attr = (Attr)attribList.item(i);
            code.append( render.setAttributeCode(attr,elem,elemVarName,true,clientDoc) );
        }
        return code.toString();
    }

    public boolean isAddChildNodesBeforeNode(Node parent,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
         return false;
    }
    
    public Object getInsertNewNodeCode(Node newNode,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        // No hay inserción como markup porque en cuanto a namespaces son los atributos son los que tienen namespace y al serializar el DOM no podemos conseguir
        // que los atributos automáticamente se pongan con prefijo salvo que lo especifique el usuario en el localName del setAttributeNS, el problema es que sin
        // usar prefijo funciona sin inserción markup y obligaríamos al usuario a usar siempre el prefijo y tendríamos que filtrarlo por otra parte en el servidor
        // Es un tema SOLO de rendimiento pot lo que podemos sacrificar la inserción como markup.
        InsertAsMarkupInfoImpl insertMarkupInfoNextLevel = null; 
        return super.getInsertNewNodeCode(newNode,insertMarkupInfoNextLevel,clientDoc);
    }    
}
