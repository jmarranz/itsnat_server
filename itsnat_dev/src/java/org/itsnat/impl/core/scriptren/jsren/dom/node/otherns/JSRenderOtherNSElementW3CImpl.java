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

package org.itsnat.impl.core.scriptren.jsren.dom.node.otherns;

import org.itsnat.impl.core.browser.web.BrowserAdobeSVG;
import org.itsnat.impl.core.browser.web.BrowserBatik;
import org.itsnat.impl.core.browser.web.BrowserW3C;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.template.MarkupTemplateVersionImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderOtherNSElementW3CImpl extends JSRenderOtherNSElementNativeImpl
{
    /**
     * Creates a new instance of JSRenderOtherNSElementW3CImpl
     */
    public JSRenderOtherNSElementW3CImpl()
    {
    }

    public static JSRenderOtherNSElementW3CImpl getJSRenderOtherNSElementW3C(BrowserW3C browser)
    {
        if (browser instanceof BrowserAdobeSVG)
            return JSRenderOtherNSElementW3CAdobeSVGImpl.SINGLETON;
        else if (browser instanceof BrowserBatik)
            return JSRenderOtherNSElementW3CBatikImpl.SINGLETON;
        else
            return JSRenderOtherNSElementW3CDefaultImpl.SINGLETON;
    }

    protected boolean isInsertChildNodesAsMarkupCapable(Element parent,MarkupTemplateVersionImpl template)
    {
        if (!super.isInsertChildNodesAsMarkupCapable(parent,template))
            return false; 

        // Si el elemento o uno de sus padres tiene el atributo ignorens en documentos XHTML con MIME HTML
        // (text/html), en dicho caso no hacemos el setInnerXML pues
        // tiende a insertar los elementos bien, es decir con namespaces.
        if (JSRenderOtherNSAttributeW3CImpl.hasIgnoreNSAttrInMIMEHTMLInTree(parent,template))
            return false;

        return true;
    }

    protected String createElement(Element nodeElem,String tagName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        String namespace = nodeElem.getNamespaceURI();
        if (namespace != null)
        {
            if (JSRenderOtherNSAttributeW3CImpl.hasIgnoreNSAttrInMIMEHTMLInTree(nodeElem,clientDoc.getItsNatStfulDocument().getItsNatStfulDocumentTemplateVersion()))
                return super.createElement(nodeElem,tagName,clientDoc);
            else
                return "itsNatDoc.doc.createElementNS(\"" + namespace + "\",\"" + tagName + "\")";
        }
        else
            return super.createElement(nodeElem,tagName,clientDoc);
    }


    public String getCurrentStyleObject(String itsNatDocVar,String elemName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return itsNatDocVar + ".win.getComputedStyle(" + elemName + ", null)";
    }

    public static boolean isElementWithSomethingOtherNSInMIMEHTML(Element elem,MarkupTemplateVersionImpl template)
    {
        if (template.isMIME_HTML() && isElementWithSomethingOtherNSInMIMEHTML(elem))
            return true;
        return false;
    }

    public static boolean isElementWithOtherNSTagNameInMIMEHTML(Element elem)
    {
        // Detectamos si el elemento tiene un tagName con namespace tal que merece reinsertarse
        // para que en el DOM se refleje como verdadero nodo con namespace
        String namespace = elem.getNamespaceURI();
        return (namespace != null) && !JSRenderOtherNSAttributeW3CImpl.isIgnoredNamespaceInMIMEHTML(namespace);
    }

    public static boolean isSomeAttrWithOtherNSInMIMEHTML(Element elem)
    {
        if (elem.hasAttributes())
        {
            NamedNodeMap attribs = elem.getAttributes();
            int len = attribs.getLength();
            for(int i = 0; i < len; i++)
            {
                // Recuerda que los atributos no heredan el namespace del elemento
                Attr attr = (Attr)attribs.item(i);
                if (JSRenderOtherNSAttributeW3CImpl.isAttrWithOtherNSInMIMEHTML(attr))
                    return true; // Declaración de namespace o de atributo concreto con namespace que merece procesar en HTML
            }
        }

        return false;
    }

    public static boolean isElementWithSomethingOtherNSInMIMEHTML(Element elem)
    {
        // El propio elemento tiene un namespace no XHTML o bien algún atributo (declaración de namespace o atributo con namespace)

        if (JSRenderOtherNSAttributeW3CImpl.hasIgnoreNSAttrInMIMEHTML(elem))
            return false;

        if (isElementWithOtherNSTagNameInMIMEHTML(elem))
            return true;

        // Sabemos que el elemento no tiene un namespace especial pero es posible que tenga
        // algún atributo con namespace que merezca reinsertar dicho atributo pues de otra manera
        // no se reflejaría este namespace en el DOM.
        // Este es el caso por ejemplo del antiguo draft de WAI-ARIA que definía atributos especiales
        // con namespace

        if (isSomeAttrWithOtherNSInMIMEHTML(elem))
            return true;

        return false;
    }

}
