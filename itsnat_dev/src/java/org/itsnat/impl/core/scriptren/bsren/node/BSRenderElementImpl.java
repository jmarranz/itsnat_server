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

package org.itsnat.impl.core.scriptren.bsren.node;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.domutil.NodeConstraints;
import org.itsnat.impl.core.scriptren.shared.node.CannotInsertAsMarkupCauseImpl;
import org.itsnat.impl.core.scriptren.shared.node.InnerMarkupCodeImpl;
import org.itsnat.impl.core.scriptren.shared.node.InsertAsMarkupInfoImpl;
import org.itsnat.impl.core.scriptren.shared.node.JSAndBSRenderElementImpl;
import org.itsnat.impl.core.scriptren.shared.node.RenderElement;
import org.itsnat.impl.core.template.ItsNatStfulDocumentTemplateVersionImpl;
import org.itsnat.impl.core.template.MarkupTemplateVersionImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class BSRenderElementImpl extends BSRenderHasChildrenNodeImpl implements RenderElement,NodeConstraints
{
    public static boolean SUPPORT_INSERTION_AS_MARKUP = true;
    
    public static final BSRenderElementImpl SINGLETON = new BSRenderElementImpl();
    
    /** Creates a new instance of BSRenderElementImpl */
    public BSRenderElementImpl()
    {
    }

    public static BSRenderElementImpl getBSRenderElement()
    {
        return BSRenderElementImpl.SINGLETON;
    }

    public String createNodeCode(Node node,ClientDocumentStfulDelegateImpl clientDoc)
    {
        Element nodeElem = (Element)node;
        return createElement(nodeElem,clientDoc);
    }

    protected String createElement(Element nodeElem,ClientDocumentStfulDelegateImpl clientDoc)
    {
        String tagName = nodeElem.getTagName();
        return createElement(nodeElem,tagName,clientDoc);
    }

    protected String createElement(Element nodeElem,String tagName,ClientDocumentStfulDelegateImpl clientDoc)
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

    public String addAttributesBeforeInsertNode(Node node,String elemVarName,ClientDocumentStfulDelegateImpl clientDoc)
    {
        // En Droid la renderización de atributos se hace con una única instancia de BSRenderAttributeImpl por lo que hay una única forma compartida
        // de renderizar entre atributos (eso no lo podemos hacer en web, pero en Web afortunadamente se utiliza mucho el innerHTML) 
        // podemos considerar una estrategia de definir atributos con una sóla sentencia a modo de batch, se enviará mucho menos código y será más rápido de parsear en beanshell
        
        Element elem = (Element)node;
        BSRenderAttributeImpl render = BSRenderAttributeImpl.getBSRenderAttribute();          
        StringBuilder code = new StringBuilder();
        
        NamedNodeMap attribList = elem.getAttributes();    
        if (attribList.getLength() <= 1)
        {
            // No vale la pena el batch
            for(int i = 0; i < attribList.getLength(); i++)
            {
                Attr attr = (Attr)attribList.item(i);
                code.append( render.setAttributeCode(attr,elem,elemVarName,clientDoc) );
            }
       }
       else
       {
            Map<String,List<Attr>> mapByNamespace = new HashMap<String,List<Attr>>();
            List<Attr> listNoNamespace = new LinkedList<Attr>();            
            for(int i = 0; i < attribList.getLength(); i++)
            {
                Attr attr = (Attr)attribList.item(i);
                String ns = attr.getNamespaceURI();
                if (ns != null)
                {
                    List<Attr> list = mapByNamespace.get(ns);
                    if (list == null) 
                    {
                        list = new LinkedList<Attr>();
                        mapByNamespace.put(ns,list);
                    }
                    list.add(attr);
                    mapByNamespace.put(ns,list);
                }
                else listNoNamespace.add(attr);
            }       
            
            if (!mapByNamespace.isEmpty())
            {
                code.append( render.setAttributeCodeBatchNS(elem,elemVarName,mapByNamespace,clientDoc) );
            }
           
            if (!listNoNamespace.isEmpty())
            {
                code.append( render.setAttributeCodeBatch(elem,elemVarName,listNoNamespace,clientDoc) );                
            }
       }
           
        return code.toString();
    }

    @Override
    public Object appendChildNodes(Node parent, String parentVarName,boolean beforeParent,InsertAsMarkupInfoImpl insertMarkupInfo,ClientDocumentStfulDelegateImpl clientDoc)
    {
        if (SUPPORT_INSERTION_AS_MARKUP)
        {        
            CannotInsertAsMarkupCauseImpl cannotInsertMarkup = canInsertAllChildrenAsMarkup((Element)parent,clientDoc.getItsNatStfulDocument().getItsNatStfulDocumentTemplateVersion(),insertMarkupInfo);
            if (cannotInsertMarkup == null)
            {
                // Sabemos que el retorno, innerMarkup, nunca es nulo en este contexto
                InnerMarkupCodeImpl innerMarkup = appendChildrenAsMarkup(parentVarName,parent,clientDoc);
                return innerMarkup;
            }
            else
            {
                InsertAsMarkupInfoImpl insertMarkupInfoNextLevel = cannotInsertMarkup.createInsertAsMarkupInfoNextLevel(); // Puede ser null
                return super.appendChildNodes(parent,parentVarName,beforeParent,insertMarkupInfoNextLevel,clientDoc);
            }
        }
        else
        {
            InsertAsMarkupInfoImpl insertMarkupInfoNextLevel = null; // Puede ser null
            return super.appendChildNodes(parent,parentVarName,beforeParent,insertMarkupInfoNextLevel,clientDoc);            
        }
    }    
       
    @Override    
    public Object getInsertNewNodeCode(Node newNode,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        if (SUPPORT_INSERTION_AS_MARKUP)
        {
            CannotInsertAsMarkupCauseImpl cannotInsertMarkup = canInsertSingleChildNodeAsMarkup(newNode,clientDoc);
            if (cannotInsertMarkup == null)
                return appendSingleChildNodeAsMarkup(newNode,clientDoc);
            else
            {
                InsertAsMarkupInfoImpl insertMarkupInfoNextLevel = cannotInsertMarkup.createInsertAsMarkupInfoNextLevel(); // Puede ser null
                return super.getInsertNewNodeCode(newNode,insertMarkupInfoNextLevel,clientDoc);
            }
        }
        else
        {
            InsertAsMarkupInfoImpl insertMarkupInfoNextLevel = null;
            return super.getInsertNewNodeCode(newNode,insertMarkupInfoNextLevel,clientDoc);
        }
    }
    
    public boolean isAddChildNodesBeforeNode(Node parent,ClientDocumentStfulDelegateImpl clientDoc)
    {
         return false;
    }    
    
    public String getAppendChildrenCodeAsMarkupSentence(InnerMarkupCodeImpl innerMarkupRender,ClientDocumentStfulDelegateImpl clientDoc)
    {
        String parentNodeLocator = innerMarkupRender.getParentNodeLocator();
        String valueBS = toTransportableStringLiteral(innerMarkupRender.getInnerMarkup(),clientDoc.getBrowser());
        if (innerMarkupRender.isUseNodeLocation())
            return "itsNatDoc.setInnerXML2(" + parentNodeLocator + "," + valueBS + ");\n";
        else // Es directamente una variable
            return "itsNatDoc.setInnerXML(" + parentNodeLocator + "," + valueBS + ");\n";
    }

    private CannotInsertAsMarkupCauseImpl canInsertSingleChildNodeAsMarkup(Node newChildNode,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return JSAndBSRenderElementImpl.canInsertSingleChildNodeAsMarkup(newChildNode, clientDoc, this);
    }

    private InnerMarkupCodeImpl appendSingleChildNodeAsMarkup(Node newNode, ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();
        String newNodeMarkup = itsNatDoc.serializeNode(newNode);
        if (DOMUtilInternal.isTheOnlyChildNode(newNode))
        {
            // Caso de único nodo
            Element parent = (Element)newNode.getParentNode();
            return appendChildrenCodeAsMarkup(null,parent,newNodeMarkup,clientDoc);
        }
        else // Caso de último nodo, sabemos que podemos usar el último InnerMarkupCodeImpl el cual está asociado al nodo padre
        {
            InnerMarkupCodeImpl lastCode = (InnerMarkupCodeImpl)clientDoc.getClientDocumentStful().getLastCodeToSend();
            lastCode.addInnerMarkup(newNodeMarkup);
            return null; // No se añade nada y se deja como último este lastCode
        }
    }

    private InnerMarkupCodeImpl appendChildrenAsMarkup(String parentVarName, Node parentNode, ClientDocumentStfulDelegateImpl clientDoc)
    {
        // Se supone que hay nodos hijo (si no no llamar).
        ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();
        StringBuilder childrenCode = new StringBuilder();

        if (parentNode.hasChildNodes())
        {
            Node child = parentNode.getFirstChild();
            while(child != null)
            {
                String nodeCode = itsNatDoc.serializeNode(child);
                childrenCode.append( nodeCode );

                child = child.getNextSibling();
            }
        }
        
        return appendChildrenCodeAsMarkup(parentVarName,(Element)parentNode,childrenCode.toString(),clientDoc);
    }

    protected boolean isInsertChildNodesAsMarkupCapable(Element parent,MarkupTemplateVersionImpl template)
    {
        // En principio todos los elementos tienen capacidad de insertar nodos hijos como markup
        // a través de nuestro setInnerXML 
        return true;
    }

    public boolean match(Node node, Object context)
    {
        // Esto es por claridad pues "match" no nos dice mucho sobre lo que tenemos que hacer
        return isChildNotValidInsertedAsMarkup(node,(MarkupTemplateVersionImpl)context);
    }

    public boolean isChildNotValidInsertedAsMarkup(Node childNode,MarkupTemplateVersionImpl template)
    {
        // Realmente sólo hay elementos pues los nodos de texto son como mucho de espacios
        return false;
    }

    public CannotInsertAsMarkupCauseImpl canInsertChildNodeAsMarkupIgnoringOther(Element parent,Node childNode,MarkupTemplateVersionImpl template)
    {
        if (!isInsertChildNodesAsMarkupCapable(parent,template))
            return new CannotInsertAsMarkupCauseImpl(parent);

        // Preguntamos si puede insertarse como markup si fuera el único nodo hijo
        Node badChildNode = DOMUtilInternal.getNodeOrFirstContainedNodeMatching(childNode,this,template);
        if (badChildNode != null)
            return new CannotInsertAsMarkupCauseImpl(parent,badChildNode);

        return null;
    }

    public boolean canInsertAllChildrenAsMarkup(Element parent,MarkupTemplateVersionImpl template)
    {
        CannotInsertAsMarkupCauseImpl cannotInsertMarkup = canInsertAllChildrenAsMarkup(parent,template,null);
        return (cannotInsertMarkup == null);
    }

    public CannotInsertAsMarkupCauseImpl canInsertAllChildrenAsMarkup(Element parent,MarkupTemplateVersionImpl template,InsertAsMarkupInfoImpl insertMarkupInfo)
    {
        int res = InsertAsMarkupInfoImpl.DO_NOT_KNOW;
        if (insertMarkupInfo != null)
        {
            res = insertMarkupInfo.canInsertAllChildrenAsMarkup(parent);
            if (res == InsertAsMarkupInfoImpl.CANNOT_INSERT_CHILDREN_VERIFIED)
                return new CannotInsertAsMarkupCauseImpl(insertMarkupInfo); // Salvamos así en la causa todo lo que ya sabemos del "subárbol"
            // Si se llega aquí, es el caso desconocido InsertAsMarkupInfoImpl.DO_NOT_KNOW
            // o bien IS_VALID_INSERTED_AS_MARKUP
        }
        // Vemos si tiene capacidad de insertar como markup sus hijos, por ejemplo si tiene innerHTML
        if (!isInsertChildNodesAsMarkupCapable(parent,template))
            return new CannotInsertAsMarkupCauseImpl(parent);

        // Ahora vemos si sus hijos son insertables como markup:

        // Si sabemos que puede ser insertado como markup entonces
        // sabemos que los hijos pueden ser también insertados como markup
        // por tanto no lo averiguamos de nuevo y así ganamos en rendimiento
        if (res != InsertAsMarkupInfoImpl.IS_VALID_INSERTED_AS_MARKUP)
        {
            Node badChildNode = getFirstChildIsNotValidInsertedAsMarkup(parent,template);
            if (badChildNode != null)
                return new CannotInsertAsMarkupCauseImpl(parent,badChildNode);
        }
        
        // Debe haber al menos un Element como hijo para que valga la pena
        // usar serialización y parsing con DOMRender
        // Hay que tener en cuenta que DOMRender no es como una simple llamada a innerHTML

        boolean hasSomeElement = false;
        if (parent.hasChildNodes())
        {
            Node child = parent.getFirstChild();
            while(child != null)
            {
                if (child.getNodeType() == Node.ELEMENT_NODE)
                {
                    hasSomeElement = true;  // Sí merece la pena insertar como markup
                    break;
                }
                
                child = child.getNextSibling();
            }
        }
        
        if (!hasSomeElement) return new CannotInsertAsMarkupCauseImpl(parent); // No merece la pena          
        
        return null;
    }

    public Node getFirstChildIsNotValidInsertedAsMarkup(Element parent,MarkupTemplateVersionImpl template)
    {
        return DOMUtilInternal.getFirstContainedNodeMatching(parent,this,template);
    }

    public InnerMarkupCodeImpl appendChildrenCodeAsMarkup(String parentVarName,Element parentNode,String childrenCode,ClientDocumentStfulDelegateImpl clientDoc)
    {
        return JSAndBSRenderElementImpl.createInnerMarkupCode(parentVarName, parentNode, childrenCode, clientDoc, this);
    }
    
}
