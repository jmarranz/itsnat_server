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

package org.itsnat.impl.core.jsren.dom.node.html.w3c;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.jsren.dom.node.html.JSRenderHTMLTextImpl;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.CharacterData;
import org.w3c.dom.html.HTMLOptionElement;

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLTextNetFrontImpl extends JSRenderHTMLTextImpl
{
    public static final JSRenderHTMLTextNetFrontImpl SINGLETON = new JSRenderHTMLTextNetFrontImpl();

    /** Creates a new instance of JSRenderHTMLTextNetFrontImpl */
    public JSRenderHTMLTextNetFrontImpl()
    {
    }

    protected String getAppendCompleteChildNode(Node parent,Node newNode,String parentVarName,ClientDocumentStfulImpl clientDoc)
    {
        if (parent instanceof HTMLOptionElement)
        {
            Text newTextNode = (Text)newNode;
            return setHTMLOptionText(parentVarName,newTextNode.getData(),clientDoc);
        }
        else
            return super.getAppendCompleteChildNode(parent, newNode, parentVarName,clientDoc);
    }

    public String getInsertCompleteNodeCode(Node newNode,ClientDocumentStfulImpl clientDoc)
    {
        Node parent = newNode.getParentNode();
        if (parent instanceof HTMLOptionElement)
        {
            Text newTextNode = (Text)newNode;
            return setHTMLOptionText((HTMLOptionElement)parent,newTextNode.getData(),clientDoc);
        }
        else
            return super.getInsertCompleteNodeCode(newNode,clientDoc);
    }

    public String getRemoveNodeCode(Node removedNode,ClientDocumentStfulImpl clientDoc)
    {
        Node parent = removedNode.getParentNode();
        if (parent instanceof HTMLOptionElement)
            return setHTMLOptionText((HTMLOptionElement)parent," ",clientDoc); // El " " no es un error es porque con "" se conserva el valor antiguo
        else
            return super.getRemoveNodeCode(removedNode,clientDoc);
    }

    public String getCharacterDataModifiedCode(CharacterData node,ClientDocumentStfulImpl clientDoc)
    {
        Node parent = node.getParentNode();
        if (parent instanceof HTMLOptionElement)
            return setHTMLOptionText((HTMLOptionElement)parent,node.getData(),clientDoc);
        else
            return super.getCharacterDataModifiedCode(node, clientDoc);
    }

    private String setHTMLOptionText(HTMLOptionElement parent,String value,ClientDocumentStfulImpl clientDoc)
    {
        StringBuffer code = new StringBuffer();
        // Evitamos usar el nombre "parent" porque es una propiedad de "window" que suele ser el contexto
        code.append( "var parentNode = " + clientDoc.getNodeReference(parent,true,true) + ";\n" );
        code.append( setHTMLOptionText("parentNode",value,clientDoc) );
        return code.toString();
    }

    private String setHTMLOptionText(String parentVarName,String value,ClientDocumentStfulImpl clientDoc)
    {
        // Hay un error visual (NetFront 3.5 al menos), el <option> no tiene nada
        // pero sigue saliendo el anterior valor, removeChild tampoco funciona.
        // La solución es enviar un espacio el cual "no se ve" en el <select>
        // y total lo que cuenta es lo que hay en el servidor.
        if (value.equals("")) value = " ";
        String jsValue = dataTextToJS(value, clientDoc);
        return parentVarName + ".text = " + jsValue + ";\n";
    }
}
