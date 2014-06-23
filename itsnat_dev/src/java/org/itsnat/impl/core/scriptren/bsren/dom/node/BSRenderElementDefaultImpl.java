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



import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.scriptren.shared.dom.node.CannotInsertAsMarkupCauseImpl;
import org.itsnat.impl.core.scriptren.shared.dom.node.InnerMarkupCodeImpl;
import org.itsnat.impl.core.scriptren.shared.dom.node.InsertAsMarkupInfoImpl;
import org.itsnat.impl.core.template.MarkupTemplateVersionImpl;
import org.itsnat.impl.core.template.otherns.ItsNatSVGDocumentTemplateVersionImpl;
import org.itsnat.impl.core.template.otherns.ItsNatUnknownDocumentTemplateVersionImpl;
import org.itsnat.impl.core.template.otherns.ItsNatXULDocumentTemplateVersionImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class BSRenderElementDefaultImpl extends BSRenderElementImpl
{
    public static final BSRenderElementDefaultImpl SINGLETON = new BSRenderElementDefaultImpl();
    
    /**
     * Creates a new instance of JSRenderOtherNSElementW3CImpl
     */
    public BSRenderElementDefaultImpl()
    {
    }


    protected boolean isInsertChildNodesAsMarkupCapable(Element parent,MarkupTemplateVersionImpl template)
    {
        // En principio todos los elementos tienen capacidad de insertar nodos hijos como markup
        // a través de nuestro setInnerXML 
        return true;
    }

    @Override
    public CannotInsertAsMarkupCauseImpl canInsertAllChildrenAsMarkup(Element parent,MarkupTemplateVersionImpl template,InsertAsMarkupInfoImpl insertMarkupInfo)
    {
        CannotInsertAsMarkupCauseImpl cannotInsertMarkup = super.canInsertAllChildrenAsMarkup(parent,template,insertMarkupInfo);
        if (cannotInsertMarkup != null)
            return cannotInsertMarkup;

        // Debe haber al menos un Element como hijo para que valga la pena
        // usar serialización y parsing con DOMRender
        // Hay que tener en cuenta que DOMRender no es como una simple llamada a innerHTML

        if (parent.hasChildNodes())
        {
            Node child = parent.getFirstChild();
            while(child != null)
            {
                if (child.getNodeType() == Node.ELEMENT_NODE)
                    return null; // Sí merece la pena insertar como markup

                child = child.getNextSibling();
            }
        }
        
        return new CannotInsertAsMarkupCauseImpl(parent); // No merece la pena
    }

    public boolean isChildNotValidInsertedAsMarkup(Node childNode,MarkupTemplateVersionImpl template)
    {
        // Para detectar si el nodo puede ser insertado como markup

        if (childNode.getNodeType() != Node.ELEMENT_NODE) return false;

        // El caso de los elementos <script> es problemático pues
        // su simple inserción (que se haría en el cliente via setInnerXML)
        // no asegura que se ejecute el código contenido

        String localName = ((Element)childNode).getLocalName();
        return "script".equals(localName);
    }

    public String getAppendChildrenCodeAsMarkupSentence(InnerMarkupCodeImpl innerMarkupRender,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
      
        String parentNodeJSLocator = innerMarkupRender.getParentNodeLocator();
        String valueJS = toTransportableStringLiteral(innerMarkupRender.getInnerMarkup(),clientDoc.getBrowserDroid());
        if (innerMarkupRender.isUseNodeLocation())
            return "itsNatDoc.setInnerXML2(" + parentNodeJSLocator + "," + valueJS + ");\n";
        else
            return "itsNatDoc.setInnerXML(" + parentNodeJSLocator + "," + valueJS + ");\n";
    }

    public InnerMarkupCodeImpl appendChildrenCodeAsMarkup(String parentVarName,Element parentNode,String childrenCode,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        childrenCode = buildOtherNSDocument(parentNode,childrenCode,clientDoc);

        return super.appendChildrenCodeAsMarkup(parentVarName, parentNode, childrenCode, clientDoc);
    }

    private String buildOtherNSDocument(Element parent,String body,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        // Tratamos de simular en un nuevo documento el contexto del elemento padre
        // y los hijos respecto al namespace, el nodo root de dicho documento
        // será similar al elemento parent respecto al prefijo, namespace etc.
        ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();
        String encoding = getEncoding(itsNatDoc);
        String prefix = parent.getPrefix();
        String defaultNS = null;
        if (prefix != null) defaultNS = itsNatDoc.getNamespace(); // Si prefix es no nulo el namespace por defecto lo impone el elemento. Hay que tener en cuenta que el namespace por defecto, salvo redefinición en el elemento via xmlns="...", lo normal es que sea el namespace del documento contenedor.

        String namespace = parent.getNamespaceURI();
        if (namespace != null)
        {
            if (NamespaceUtil.isSVGNamespace(namespace))
                return ItsNatSVGDocumentTemplateVersionImpl.wrapBodyAsDocument(body,encoding,prefix,defaultNS);
            else if (NamespaceUtil.isXULNamespace(namespace))
                return ItsNatXULDocumentTemplateVersionImpl.wrapBodyAsDocument(body,encoding,prefix,defaultNS);
            else
                return ItsNatUnknownDocumentTemplateVersionImpl.wrapBodyAsDocument(namespace,body,encoding,prefix,defaultNS);
        }
        else  // Seguramente el elemento se creó con Document.createElement()
            return ItsNatUnknownDocumentTemplateVersionImpl.wrapBodyAsDocument(null,body,encoding,prefix,defaultNS);
    }

    public String getEncoding(ItsNatStfulDocumentImpl itsNatDoc)
    {
        return itsNatDoc.getEncoding();
    }

    public boolean isTextAddedToInsertedScriptNotExecuted(Element script,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return false;
    }    
    
    public boolean isInsertedScriptNotExecuted(Element script,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return false;
    }    
}
