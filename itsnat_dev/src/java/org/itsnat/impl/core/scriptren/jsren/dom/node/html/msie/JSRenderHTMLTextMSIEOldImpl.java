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

package org.itsnat.impl.core.scriptren.jsren.dom.node.html.msie;

import org.itsnat.impl.core.browser.web.BrowserMSIEOld;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.scriptren.jsren.dom.node.html.JSRenderHTMLTextImpl;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.html.HTMLAppletElement;
import org.w3c.dom.html.HTMLObjectElement;
import org.w3c.dom.html.HTMLScriptElement;
import org.w3c.dom.html.HTMLStyleElement;

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLTextMSIEOldImpl extends JSRenderHTMLTextImpl
{
    public static final JSRenderHTMLTextMSIEOldImpl SINGLETON = new JSRenderHTMLTextMSIEOldImpl();
    
    /** Creates a new instance of JSRenderHTMLTextMSIEOldImpl */
    public JSRenderHTMLTextMSIEOldImpl()
    {
    }

    public static JSRenderHTMLTextMSIEOldImpl getJSRenderHTMLTextMSIEOld(BrowserMSIEOld browser)
    {
        return JSRenderHTMLTextMSIEOldImpl.SINGLETON;
    }
    
    private String setScriptTextContent(HTMLScriptElement parent,String value,ClientDocumentStfulDelegateImpl clientDoc)
    {
        String parentRef = clientDoc.getNodeReference(parent,true,true);
        return setScriptTextContent(parentRef,value,clientDoc);
    }

    private String setScriptTextContent(String parentVarName,String value,ClientDocumentStfulDelegateImpl clientDoc)
    {
        // No vale ni innerHTML ni appendChild en estos elementos
        // http://www.justatheory.com/computers/programming/javascript/ie_dom_help.html

        String valueJS = dataTextToJS(value,clientDoc);
        return parentVarName + ".text = " + valueJS + ";\n";
    }

    private String setStyleTextContent(HTMLStyleElement parent,String value,ClientDocumentStfulDelegateImpl clientDoc)
    {
        String parentRef = clientDoc.getNodeReference(parent,true,true);
        return setStyleTextContent(parentRef,value,clientDoc);
    }

    private String setStyleTextContent(String parentVarName,String value,ClientDocumentStfulDelegateImpl clientDoc)
    {
        // No vale ni innerHTML ni appendChild en estos elementos
        // http://www.phpied.com/dynamic-script-and-style-elements-in-ie/
        String valueJS = dataTextToJS(value,clientDoc);
        return parentVarName + ".styleSheet.cssText = " + valueJS + ";\n";
    }

    protected String getAppendCompleteChildNode(Node parent,Node newNode,String parentVarName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        if (parent instanceof HTMLScriptElement)
            return setScriptTextContent(parentVarName,((Text)newNode).getData(),clientDoc);
        else if (parent instanceof HTMLStyleElement)
            return setStyleTextContent(parentVarName,((Text)newNode).getData(),clientDoc);
        else if (parent instanceof HTMLObjectElement)
            return "";  // <object> no tolera la inserción de nodos de texto hijo aunque sean espacios, ni antes ni después de haberse insertado el <object> en el árbol
        else if (parent instanceof HTMLAppletElement)
            return "";  // <applet> tampoco tolera la inserción de nodos de texto hijo aunque sean espacios, ni antes ni después de haberse insertado el <applet> en el árbol
        else
            return super.getAppendCompleteChildNode(parent,newNode,parentVarName,clientDoc);
    }

    @Override
    public String getInsertCompleteNodeCode(Node newNode,ClientDocumentStfulDelegateImpl clientDoc)
    {
        // El nodo de texto de <script> y <style> es el único hijo posible y
        // necesitan técnicas específicas
        Node parent = newNode.getParentNode();
        if (parent instanceof HTMLScriptElement)
            return setScriptTextContent((HTMLScriptElement)parent,((Text)newNode).getData(),clientDoc);
        else if (parent instanceof HTMLStyleElement)
            return setStyleTextContent((HTMLStyleElement)parent,((Text)newNode).getData(),clientDoc);
        else if (parent instanceof HTMLObjectElement)
            return "";  // <object> no tolera la inserción de nodos de texto hijo aunque sean espacios, ni antes ni después de haberse insertado el <object> en el árbol
        else if (parent instanceof HTMLAppletElement)
            return "";  // <applet> tampoco tolera la inserción de nodos de texto hijo aunque sean espacios, ni antes ni después de haberse insertado el <applet> en el árbol
        else
            return super.getInsertCompleteNodeCode(newNode,clientDoc);
    }

    public String getRemoveNodeCode(Node removedNode,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        Node parent = removedNode.getParentNode();
        if (parent instanceof HTMLScriptElement)
            return setScriptTextContent((HTMLScriptElement)parent,"",clientDoc);
        else if (parent instanceof HTMLStyleElement)
            return setStyleTextContent((HTMLStyleElement)parent,"",clientDoc);
        else if (parent instanceof HTMLObjectElement)
            return "";  // <object> no tolera la inserción de nodos de texto hijo aunque sean espacios, ni antes ni después de haberse insertado el <object> en el árbol, los que hubiera en carga no se reflejan en el DOM
        else if (parent instanceof HTMLAppletElement)
            return "";  // <applet> tampoco tolera la inserción de nodos de texto hijo aunque sean espacios, ni antes ni después de haberse insertado el <applet> en el árbol, los que hubiera en carga no se reflejan en el DOM
        else
            return super.getRemoveNodeCode(removedNode,clientDoc);
        // En el caso de nodo texto hijo de <object> si no lo encuentra pues no hará nada
    }

    public String getCharacterDataModifiedCode(CharacterData node,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        Node parent = node.getParentNode();
        if (parent instanceof HTMLScriptElement)
            return setScriptTextContent((HTMLScriptElement)parent,((Text)node).getData(),clientDoc);
        else if (parent instanceof HTMLStyleElement)
            return setStyleTextContent((HTMLStyleElement)parent,((Text)node).getData(),clientDoc);
        else if (parent instanceof HTMLObjectElement)
            return "";  // <object> no tolera la inserción de nodos de texto hijo aunque sean espacios, ni antes ni después de haberse insertado el <object> en el árbol, los que hubiera en carga no se reflejan en el DOM
        else if (parent instanceof HTMLAppletElement)
            return "";  // <applet> tampoco tolera la inserción de nodos de texto hijo aunque sean espacios, ni antes ni después de haberse insertado el <applet> en el árbol, los que hubiera en carga no se reflejan en el DOM
        else
            return super.getCharacterDataModifiedCode(node, clientDoc);
    }
    
}
