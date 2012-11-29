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

package org.itsnat.impl.core.jsren.dom.node.html.msie;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.path.DOMPathResolverHTMLDocMSIEPocket;
import org.itsnat.impl.core.path.NodeLocationImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLElementMSIEPocketImpl extends JSRenderHTMLElementMSIEOldImpl
{
    public static final JSRenderHTMLElementMSIEPocketImpl SINGLETON = new JSRenderHTMLElementMSIEPocketImpl();

    /** Creates a new instance of JSRenderHTMLElementMSIEPocketImpl */
    public JSRenderHTMLElementMSIEPocketImpl()
    {
        tagNamesWithoutInnerHTML.add("button");
    }

    protected String createElement(Element nodeElem,String tagName,ClientDocumentStfulImpl clientDoc)
    {
        // Es RARISIMO que un elemento X/HTML tenga un prefijo en un
        // documento X/HTML (porque MSIE no admite más tipos de documentos con estado)
        // pero por si acaso.
        // Ver notas en JSRenderOtherNSElementMSIEmpl

        String localName = nodeElem.getLocalName(); // Esto es porque no toleramos la existencia de prefijo en tagName
        return super.createElement(nodeElem, localName,clientDoc);
    }

    protected String getInsertCompleteNodeCode(Node newNode,String newNodeCode,ClientDocumentStfulImpl clientDoc)
    {
        Node nextSibling = DOMPathResolverHTMLDocMSIEPocket.getNextSignificativeSibling(newNode);
        if (nextSibling != null)
        {
            DOMPathResolverHTMLDocMSIEPocket pathResolver = (DOMPathResolverHTMLDocMSIEPocket)clientDoc.getDOMPathResolver();

            if (pathResolver.isFilteredInClient(nextSibling))
            {
                // Hay que insertar *antes* de un nodo que es filtrado en cliente 

                // IE Pocket no representa nodos en el DOM tal y como nodos de texto y comentarios,
                // en el caso de comentarios esto NO es un problema porque no son visibles ni visualmente
                // ni en el DOM, el problema son los nodos de texto que son visibles visualmente.
                // Si nextSibling es un texto y buscamos el siguiente a éste (un elemento) el NodeLocation
                // obtenido sería el del nodo contiguo (siguiente) y por tanto
                // acabaríamos insertando *después* del nodo de texto y no antes, el insertar después
                // se manifestará visualmente y en el innerHTML que en Pocket IE es el "DOM" para nodos de texto.
                // Por ello usamos en ese caso el método especial insertAfter para IE Mobile
                // en donde insertamos después del nodo anterior "visible" no filtrado al nodo invisible.
                // No hay problema en el cálculo de paths respecto al nodo nuevo (presente en el servidor pero no en el cliente)
                // pues estamos obteniendo el path de un *nodo anterior* al nuevo
                Element prevSibling = (Element)pathResolver.getPreviousSiblingInClientDOM(newNode); // Filtramos un posible comentario/nodo de texto contiguo al nodo de texto/comentario (el nextSibling), sería el caso de que newNode haya sido insertado entre medias de dos nodos "invisibles", no podemos hacer nada en ese caso, la inserción quedará en el cliente antes de los dos nodos "invisibles" pues sólo podemos usar Element como referencia

                // Si prevSibling es null entonces es insertar al principio y suponer que hay un nodo de texto/comentario al ppio de todo (el sibling)
                NodeLocationImpl prevNodeLoc = clientDoc.getNodeLocationRelativeToParent(prevSibling);

                String varAssign = "";
                if (!newNodeCode.startsWith("itsNatDoc.doc.create"))
                    varAssign = newNodeCode + " = "; // newNodeCode es una variable, no es un itsNatDoc.doc.createElement... etc

                Node parent = newNode.getParentNode();
                NodeLocationImpl parentLoc = clientDoc.getNodeLocation(parent,true);
                // prevNodeLoc puede ser una referencia nula
                return varAssign + "itsNatDoc.insertAfter(" + parentLoc.toJSArray(true) + "," + newNodeCode + "," + prevNodeLoc.toJSArray(false) + ");\n"; 
            }
            else
            {
                return super.getInsertCompleteNodeCode(newNode,newNodeCode,clientDoc);
            }
        }
        else
        {
            return super.getInsertCompleteNodeCode(newNode,newNodeCode,clientDoc);
        }
    }

    protected String addAttributesBeforeInsertNode(Node node,String elemVarName,ClientDocumentStfulImpl clientDoc)
    {
        // Redefinimos para hacer más eficiente la adición de atributos en creación del elemento
        // aunque en principio el añadido uno a uno en la forma normal también funcionaría
        Element elem = (Element)node;
        StringBuffer code = new StringBuffer();
        code.append( "var attribs = {\n" );

        NamedNodeMap attribList = elem.getAttributes();
        int len = attribList.getLength();
        for(int i = 0; i < len; i++)
        {
            Attr attr = (Attr)attribList.item(i);
            String name = attr.getName().toLowerCase(); // porque el atributo se añade usando innerHTML, no hay problema con el atributo style tampoco
            String jsValue = toTransportableStringLiteral(attr.getValue(),clientDoc.getBrowser());
            if (i > 0) code.append( "," );
            code.append( "\"" + name + "\":" + jsValue );
        }
        code.append( "  };\n" );

        code.append( elemVarName + " = itsNatDoc.addAttributes(" + elemVarName + ",attribs);\n" );
        return code.toString();
    }

}
