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

package org.itsnat.impl.core.jsren.dom.node.otherns;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.path.NodeJSRefImpl;
import org.itsnat.impl.core.path.NodeLocationImpl;
import org.itsnat.impl.core.template.MarkupTemplateVersionImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class JSRenderOtherNSAttributeW3CImpl extends JSRenderOtherNSAttributeImpl
{
    public final static JSRenderOtherNSAttributeW3CImpl SINGLETON = new JSRenderOtherNSAttributeW3CImpl();
    public final static String IGNORENS = "ignorens";
    
    /**
     * Creates a new instance of JSRenderOtherNSAttributeW3CImpl
     */
    public JSRenderOtherNSAttributeW3CImpl()
    {
    }

    public boolean isIgnored(Attr attr, Element elem)
    {
        return false;
    }

    public String setAttributeOnlyCode(Attr attr,String attrName,String jsValue,NodeJSRefImpl nodeRef,boolean newElem)
    {
        String namespaceURI = attr.getNamespaceURI();
        if (namespaceURI != null)
        {
            ClientDocumentStfulImpl clientDoc = nodeRef.getClientDocumentStful();
            if (hasIgnoreNSAttrInMIMEHTMLInTree(attr.getOwnerElement(),clientDoc.getItsNatStfulDocument().getItsNatStfulDocumentTemplateVersion()))
            {
                return super.setAttributeOnlyCode(attr,attrName,jsValue,nodeRef,newElem);
            }
            else
            {
                if (nodeRef.getNodeRef() instanceof NodeLocationImpl)
                {
                    NodeLocationImpl nodeLoc = (NodeLocationImpl)nodeRef.getNodeRef();
                    return "itsNatDoc.setAttributeNS2(" + nodeLoc.toJSArray(true) + ",\"" + namespaceURI + "\",\"" + attrName + "\"," + jsValue + ");\n";
                }
                else
                {
                    String elemVarName = (String)nodeRef.getNodeRef();
                    return "itsNatDoc.setAttributeNS(" + elemVarName + ",\"" + namespaceURI + "\",\"" + attrName + "\"," + jsValue + ");\n";
                }
            }
        }
        else
            return super.setAttributeOnlyCode(attr,attrName,jsValue,nodeRef,newElem);
    }

    protected String removeAttributeOnlyCode(Attr attr,String attrName,Element elem,NodeJSRefImpl nodeRef)
    {
        String namespaceURI = attr.getNamespaceURI();
        if (namespaceURI != null)
        {
            ClientDocumentStfulImpl clientDoc = nodeRef.getClientDocumentStful();
            if (hasIgnoreNSAttrInMIMEHTMLInTree(attr.getOwnerElement(),clientDoc.getItsNatStfulDocument().getItsNatStfulDocumentTemplateVersion()))
            {
                return super.removeAttributeOnlyCode(attr,attrName,elem,nodeRef);
            }
            else
            {
                attrName = attr.getLocalName(); // Es el localName de acuerdo a la documentación oficial de removeAttributeNS
                if (nodeRef.getNodeRef() instanceof NodeLocationImpl)
                {
                    NodeLocationImpl nodeLoc = (NodeLocationImpl)nodeRef.getNodeRef();
                    return "itsNatDoc.removeAttributeNS2(" + nodeLoc.toJSArray(true) + ",\"" + namespaceURI + "\",\"" + attrName + "\");\n";
                }
                else
                {
                    String elemVarName = (String)nodeRef.getNodeRef();
                    return "itsNatDoc.removeAttributeNS(" + elemVarName + ",\"" + namespaceURI + "\",\"" + attrName + "\");\n";
                }
            }
        }
        else
            return super.removeAttributeOnlyCode(attr,attrName,elem,nodeRef);
    }

    public boolean isRenderAttributeAlongsideProperty(String attrName, Element ele)
    {
        // Actualmente sólo tiene sentido en controles XUL, pero en general cualquier control visual de cualquier namespace que siga el espíritu
        // del HTML/XHTML según el W3C distingue entre atributos y propiedades, otra cosa es que
        // el namespace no tenga controles visuales concretos con propiedades tal y como SVG,
        // pero eso se pregunta en otro sitio, este método se llama al renderizar una propiedad.
        return true;
    }

    public static boolean hasIgnoreNSAttrInMIMEHTML(Element elem)
    {
        // En el futuro podrá ponerse un "*" indicando todos, o bien la lista
        // de namespaces a ignorar separados por comas
        Attr attr = elem.getAttributeNodeNS(NamespaceUtil.ITSNAT_NAMESPACE,IGNORENS);
        if (attr == null) return false;
        if (!"itsnat".equals(attr.getPrefix()))
            throw new ItsNatException("Prefix name 'itsnat' is mandatory for " + IGNORENS + " attribute",attr);
        return "true".equals(attr.getValue());
    }

    public static boolean isIgnoredNamespaceInMIMEHTML(String namespace)
    {
        return NamespaceUtil.isXHTMLNamespace(namespace) || // Inútil en HTML
               NamespaceUtil.isItsNatNamespace(namespace) || // Ignoramos el namespace de ItsNat pues los elementos con namespace ItsNat (comment, include) son procesados en tiempo de carga del template y desaparecen y los atributos como nocache etc no tienen valor alguno en el cliente
               NamespaceUtil.isXMLNamespace(namespace); // Ignoramos el namespace XML implícito de atributos tipo xml:lang
    }


    public static boolean hasIgnoreNSAttrInMIMEHTMLInTree(Element elem)
    {
        if (hasIgnoreNSAttrInMIMEHTML(elem))
            return true;
        else
        {
            Node parent = elem.getParentNode();
            if (!(parent instanceof Element)) return false;
            return hasIgnoreNSAttrInMIMEHTMLInTree((Element)parent);
        }
    }

    public static boolean hasIgnoreNSAttrInMIMEHTMLInTree(Element elem,MarkupTemplateVersionImpl template)
    {
        if (template.isMIME_HTML() && hasIgnoreNSAttrInMIMEHTMLInTree(elem))
            return true;
        return false;
    }

    public static boolean isAttrWithOtherNSInMIMEHTML(Attr attr)
    {
        String namespace = attr.getNamespaceURI();
        if (namespace == null) return false;
        if (NamespaceUtil.isXMLNSDecAttribute(attr))
        {
            // Declaración xmlns="..." o xmlns:prefix="..."
            String value = attr.getValue(); // value contiene el namespace que declaramos
            if (isIgnoredNamespaceInMIMEHTML(value)) return false;
        }
        else
        {
            // Atributo concreto
            if (isIgnoredNamespaceInMIMEHTML(namespace)) return false;
        }
        return true;
    }
}
