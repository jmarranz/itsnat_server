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
import org.w3c.dom.Node;
import org.w3c.dom.CharacterData;
import org.w3c.dom.html.HTMLAppletElement;
import org.w3c.dom.html.HTMLObjectElement;

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLCommentMSIE6Impl extends JSRenderHTMLCommentMSIEOldImpl
{
    public static final JSRenderHTMLCommentMSIE6Impl SINGLETON = new JSRenderHTMLCommentMSIE6Impl();

    /** Creates a new instance of JSRenderHTMLCommentMSIEPocketImpl */
    public JSRenderHTMLCommentMSIE6Impl()
    {
    }

    protected String getAppendCompleteChildNode(Node parent,Node newNode,String parentVarName,ClientDocumentStfulImpl clientDoc)
    {
        if (parent instanceof HTMLObjectElement)
            return "";  // <object> no tolera la inserción de nodos de texto hijo aunque sean espacios, ni antes ni después de haberse insertado el <object> en el árbol
        else if (parent instanceof HTMLAppletElement)
            return "";  // <applet> tampoco tolera la inserción de nodos de texto hijo aunque sean espacios, ni antes ni después de haberse insertado el <applet> en el árbol
        else
            return super.getAppendCompleteChildNode(parent,newNode,parentVarName,clientDoc);
    }

    public String getInsertCompleteNodeCode(Node newNode,ClientDocumentStfulImpl clientDoc)
    {
        // El nodo de texto de <script> y <style> es el único hijo posible y
        // necesitan técnicas específicas
        Node parent = newNode.getParentNode();
        if (parent instanceof HTMLObjectElement)
            return "";  // <object> no tolera la inserción de nodos de texto hijo aunque sean espacios, ni antes ni después de haberse insertado el <object> en el árbol
        else if (parent instanceof HTMLAppletElement)
            return "";  // <applet> tampoco tolera la inserción de nodos de texto hijo aunque sean espacios, ni antes ni después de haberse insertado el <applet> en el árbol
        else
            return super.getInsertCompleteNodeCode(newNode,clientDoc);
    }

    public String getRemoveNodeCode(Node removedNode,ClientDocumentStfulImpl clientDoc)
    {
        Node parent = removedNode.getParentNode();
        if (parent instanceof HTMLObjectElement)
            return "";  // <object> no tolera la inserción de nodos de texto hijo aunque sean espacios, ni antes ni después de haberse insertado el <object> en el árbol, los que hubiera en carga no se reflejan en el DOM
        else if (parent instanceof HTMLAppletElement)
            return "";  // <applet> tampoco tolera la inserción de nodos de texto hijo aunque sean espacios, ni antes ni después de haberse insertado el <applet> en el árbol, los que hubiera en carga no se reflejan en el DOM
        else
            return super.getRemoveNodeCode(removedNode,clientDoc);
        // En el caso de nodo texto hijo de <object> si no lo encuentra pues no hará nada
    }

    public String getCharacterDataModifiedCode(CharacterData node,ClientDocumentStfulImpl clientDoc)
    {
        Node parent = node.getParentNode();
        if (parent instanceof HTMLObjectElement)
            return "";  // <object> no tolera la inserción de nodos de texto hijo aunque sean espacios, ni antes ni después de haberse insertado el <object> en el árbol, los que hubiera en carga no se reflejan en el DOM
        else if (parent instanceof HTMLAppletElement)
            return "";  // <applet> tampoco tolera la inserción de nodos de texto hijo aunque sean espacios, ni antes ni después de haberse insertado el <applet> en el árbol, los que hubiera en carga no se reflejan en el DOM
        else
            return super.getCharacterDataModifiedCode(node, clientDoc);
    }
}
