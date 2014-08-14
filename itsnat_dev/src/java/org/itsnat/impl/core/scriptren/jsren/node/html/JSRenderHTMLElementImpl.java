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

package org.itsnat.impl.core.scriptren.jsren.node.html;

import java.util.HashSet;
import java.util.Set;
import org.itsnat.impl.core.browser.web.BrowserMSIEOld;
import org.itsnat.impl.core.browser.web.BrowserW3C;
import org.itsnat.impl.core.browser.web.BrowserWeb;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.scriptren.shared.node.InnerMarkupCodeImpl;
import org.itsnat.impl.core.scriptren.jsren.node.JSRenderElementImpl;
import org.itsnat.impl.core.scriptren.jsren.node.html.msie.JSRenderHTMLElementMSIEOldImpl;
import org.itsnat.impl.core.scriptren.jsren.node.html.w3c.JSRenderHTMLElementW3CImpl;
import org.itsnat.impl.core.template.MarkupTemplateVersionImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLAppletElement;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLObjectElement;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderHTMLElementImpl extends JSRenderElementImpl
{
    protected final Set<String> tagNamesWithoutInnerHTML = new HashSet<String>(); // TagNames que o bien no tiene o no funciona innerHTML. No sincronizamos porque sólo se lee
    protected final Set<String> tagNamesNotValidInsideInnerHTML = new HashSet<String>();

    /** Creates a new instance of JSElementRender */
    public JSRenderHTMLElementImpl()
    {
    }

    public static JSRenderHTMLElementImpl getJSRenderHTMLElement(BrowserWeb browser)
    {
        // Evitamos así buscar el render una y otra vez pues hay muchos navegadores.
        JSRenderHTMLElementImpl render = browser.getJSRenderHTMLElement();
        if (render != null)
            return render;

        if (browser instanceof BrowserMSIEOld)
            render = JSRenderHTMLElementMSIEOldImpl.getJSMSIEHTMLElementRender((BrowserMSIEOld)browser);
        else
            render = JSRenderHTMLElementW3CImpl.getJSRenderHTMLElementW3C((BrowserW3C)browser);

        browser.setJSRenderHTMLElement(render);
        return render;
    }

    protected Set<String> getTagNamesWithoutInnerHTML()
    {
        return tagNamesWithoutInnerHTML;
    }

    protected Set<String> getTagNamesNotValidInsideInnerHTML()
    {
        return tagNamesNotValidInsideInnerHTML;
    }

    protected boolean isInsertChildNodesAsMarkupCapable(Element parent,MarkupTemplateVersionImpl template)
    {
        // Si el elemento admite innerHTML podrá usarse innerHTML

        // innerHTML también está también soportado en XHTML con MIME application/xhtml+xml
        // en FireFox 1.5+ (quizás previos), Safari 3 y Opera 9
        // Es decir básicamente todos los navegadores en donde soportamos XHTML con application/xhtml+xml
        // Incluso XHTML servido como XML (application/xml). Esto no está probado en FireFox < 2.0 (pero no importa)
        // http://annevankesteren.nl/2005/05/innerhtml
        // http://annevankesteren.nl/test/xml/script/innerHTML/001
        // http://annevankesteren.nl/test/xml/script/innerHTML/002

        // Merece la pena el esfuerzo pues la ganancia de rendimiento de innerHTML en el cliente es muy grande


        // parent es con seguridad un HTMLElement, hacemos el cast para asegurarnos
        String localName = ((HTMLElement)parent).getLocalName();
        return ! getTagNamesWithoutInnerHTML().contains(localName);
    }

    @Override    
    public String getAppendChildrenCodeAsMarkupSentence(InnerMarkupCodeImpl innerMarkupRender,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        // Usamos innerHTML que es mucho más eficaz que
        // con DOM en JavaScript pues JavaScript es muy lento
        // y via innerHTML se procesa con el parser C/C++ del navegador
        // El innerHTML funciona incluso en elementos XHTML embebidos
        // en XUL (sólo Gecko) y SVG (testeado FireFox 3.5.2, Opera 9.63,
        // Chrome 2.0 y Safari 3.1)        

        String parentNodeJSLocator = innerMarkupRender.getParentNodeLocator();
        String valueJS = toTransportableStringLiteral(innerMarkupRender.getInnerMarkup(),clientDoc.getBrowserWeb());
        if (innerMarkupRender.isUseNodeLocation())
            return "itsNatDoc.setInnerHTML2(" + parentNodeJSLocator + "," + valueJS + ");\n";
        else // Es directamente una variable
            return "itsNatDoc.setInnerHTML(" + parentNodeJSLocator + "," + valueJS + ");\n";
    }

    public boolean isChildNotValidInsertedAsMarkup(Node childNode,MarkupTemplateVersionImpl template)
    {
        return isChildNotValidInsideInnerHTML(childNode,template);
    }

    public boolean isChildNotValidInsideInnerHTML(Node childNode,MarkupTemplateVersionImpl template)
    {
        // Para detectar si el nodo puede ser insertado como markup dentro de
        // la string de un innerHTML, devolvemos true si NO puede ser insertado como markup
        int type = childNode.getNodeType();
        if (type == Node.ELEMENT_NODE)
        {
            Element elem = (Element)childNode;
            if (elem instanceof HTMLElement)
                return isChildNotValidInsideInnerHTMLHTMLElement(elem,template);
            else
                return isChildNotValidInsideInnerHTMLElementNotHTML(elem,template);
        }
        else if (type == Node.TEXT_NODE)
            return false; // NO hay ningún navegador que no admita un nodo de texto dentro de un innerHTML (sería bastante absurdo)
        else
            return isChildNotValidInsideInnerHTMLNotElementOrText(childNode); // Tiene sentido por ejemplo en comentarios (algunos navegadores no los toleran)
    }

    protected boolean isChildNotValidInsideInnerHTMLHTMLElement(Element elem,MarkupTemplateVersionImpl template)
    {
        String localName = elem.getLocalName();
        return getTagNamesNotValidInsideInnerHTML().contains(localName);
    }

    protected abstract boolean isChildNotValidInsideInnerHTMLElementNotHTML(Element elem,MarkupTemplateVersionImpl template);
    protected abstract boolean isChildNotValidInsideInnerHTMLNotElementOrText(Node node);

    public boolean isInsertedScriptNotExecuted(Element script,ClientDocumentStfulDelegateImpl clientDoc)
    {
        return false; // Sólo cosa de SVG
    }

    @Override    
    public boolean isTextAddedToInsertedScriptNotExecuted(Element script,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        // Sabemos sí o sí que que script es un HTMLScriptElement
        BrowserWeb browser = clientDoc.getBrowserWeb();
        return browser.isTextAddedToInsertedHTMLScriptNotExecuted();
    }

    @Override
    public boolean isAddChildNodesBeforeNode(Node parent,ClientDocumentStfulDelegateImpl clientDoc)
    {
        // Es necesario añadir primero los <param> al <object> o <applet> para que actúen
        // en tiempo de carga del <object> o <applet>
        if ((parent instanceof HTMLObjectElement)||
            (parent instanceof HTMLAppletElement))
            return true;
        else
            return super.isAddChildNodesBeforeNode(parent, clientDoc);
    }
}
