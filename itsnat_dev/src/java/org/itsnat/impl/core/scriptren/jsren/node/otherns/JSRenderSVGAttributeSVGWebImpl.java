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

package org.itsnat.impl.core.scriptren.jsren.node.otherns;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.scriptren.shared.dom.node.NodeScriptRefImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class JSRenderSVGAttributeSVGWebImpl extends JSRenderOtherNSAttributeImpl
{
    public final static JSRenderSVGAttributeSVGWebImpl SINGLETON = new JSRenderSVGAttributeSVGWebImpl();

    /**
     * Creates a new instance of JSRenderOtherNSAttributeW3CImpl
     */
    public JSRenderSVGAttributeSVGWebImpl()
    {
    }

    public boolean isIgnored(Attr attr,Element elem)
    {
        // xmlns="..." da problemas en SVGWeb en FireFox (no en MSIE porque en MSIE no sirve para nada),
        // Filtraríamos la declaración xmlns="http://www.w3.org/2000/svg" que no es relevante pues lo normal es que sea
        // del nodo <svg> root y esta declaración ya es gestionada por el propio SVGWeb.

        // Por otra parte los atributos tipo xmlns:prefijo="..." por ejemplo
        // la declaración xmlns:itsnat="..." da problemas en SVGWeb en navegadores
        // WebKit (Chrome y Safari) el Flash da error al parsear, el parseado
        // ocurre incluso cuando el SVG se crea via DOM, dicho DOM es provisional
        // se convierte en cadena se parsea y se recrea un nuevo DOM.

        return NamespaceUtil.isXMLNSDecAttribute(attr);
    }

    @Override
    public String setAttributeOnlyCode(Attr attr,String attrName,String jsValue,NodeScriptRefImpl nodeRef,boolean newElem,ClientDocumentStfulDelegateImpl clientDoc)
    {
        String namespaceURI = attr.getNamespaceURI();
        if (namespaceURI != null)
        {
            if (nodeRef.getNodeRef() instanceof NodeLocationImpl)
            {
                NodeLocationImpl nodeLoc = (NodeLocationImpl)nodeRef.getNodeRef();
                return "itsNatDoc.setAttributeNS2(" + nodeLoc.toScriptNodeLocation(true) + ",\"" + namespaceURI + "\",\"" + attrName + "\"," + jsValue + ");\n";
            }
            else
            {
                String elemVarName = (String)nodeRef.getNodeRef();
                return "itsNatDoc.setAttributeNS(" + elemVarName + ",\"" + namespaceURI + "\",\"" + attrName + "\"," + jsValue + ");\n";
            }
        }
        else
            return super.setAttributeOnlyCode(attr,attrName,jsValue,nodeRef,newElem,clientDoc);
    }

    @Override
    protected String removeAttributeOnlyCode(Attr attr,String attrName,Element elem,NodeScriptRefImpl nodeRef,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        String namespaceURI = attr.getNamespaceURI();
        if (namespaceURI != null)
        {
            attrName = attr.getLocalName(); // Es el localName de acuerdo a la documentación oficial de removeAttributeNS
            if (nodeRef.getNodeRef() instanceof NodeLocationImpl)
            {
                NodeLocationImpl nodeLoc = (NodeLocationImpl)nodeRef.getNodeRef();
                return "itsNatDoc.removeAttributeNS2(" + nodeLoc.toScriptNodeLocation(true) + ",\"" + namespaceURI + "\",\"" + attrName + "\");\n";
            }
            else
            {
                String elemVarName = (String)nodeRef.getNodeRef();
                return "itsNatDoc.removeAttributeNS(" + elemVarName + ",\"" + namespaceURI + "\",\"" + attrName + "\");\n";
            }
        }
        else
            return super.removeAttributeOnlyCode(attr,attrName,elem,nodeRef,clientDoc);
    }

    public boolean isRenderAttributeAlongsideProperty(String attrName, Element ele)
    {
        // Actualmente sólo tiene sentido en controles XUL, pero en general cualquier control visual de cualquier namespace que siga el espíritu
        // del HTML/XHTML según el W3C distingue entre atributos y propiedades, otra cosa es que
        // el namespace no tenga controles visuales concretos con propiedades tal y como SVG,
        // pero eso se pregunta en otro sitio, este método se llama al renderizar una propiedad.
        return true;
    }
}
