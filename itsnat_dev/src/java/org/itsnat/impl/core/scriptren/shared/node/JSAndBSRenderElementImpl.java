/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.scriptren.shared.node;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.template.ItsNatStfulDocumentTemplateVersionImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class JSAndBSRenderElementImpl extends JSAndBSRenderHasChildrenNodeImpl 
{
    public static CannotInsertAsMarkupCauseImpl canInsertSingleChildNodeAsMarkup(Node newChildNode,ClientDocumentStfulDelegateImpl clientDoc,RenderElement render)
    {
        // Este es un escenario en donde queremos insertar un nuevo nodo pero puede
        // haber antes ya otros previamente insertados, por lo que sólo podemos
        // insertar como markup si este nodo es el único nodo o el último
        // y disponemos de un InnerMarkupCodeImpl

        Element parent = (Element)newChildNode.getParentNode();
        if (!DOMUtilInternal.isTheOnlyChildNode(newChildNode))
        {
            // Debe ser el último.
            // De esta manera permitimos "reusar" el innerHTML (o innerXML) en casos
            // por ejemplo de appendChild sucesivos por parte del programador (mismo padre claro)
            if (parent.getLastChild() != newChildNode)
                return new CannotInsertAsMarkupCauseImpl(parent); // No es el último
            // Los anteriores debieron ser insertados inmediatamente antes como innerHTML
            // quizás en el futuro podamos detectar que los últimos cambios realizados en el DOM no afectan
            // al último InnerMarkupCodeImpl asociado al nodo actual pero por ahora es complicado
            // y no merece la pena.
            Object lastCode = clientDoc.getClientDocumentStful().getLastCodeToSend();
            if (!(lastCode instanceof InnerMarkupCodeImpl))
                return new CannotInsertAsMarkupCauseImpl(parent); // Si existe un InnerMarkupCodeImpl debe ser lo último que se hizo
            InnerMarkupCodeImpl lastInnerCode = (InnerMarkupCodeImpl)lastCode;
            if (lastInnerCode.getParentNode() != parent)
                return new CannotInsertAsMarkupCauseImpl(parent); // Es un inner de otro nodo padre
            // Si llegamos aquí es que los anteriores al nuevo son compatibles con innerHTML o nuestro innerXML
        }

        ItsNatStfulDocumentTemplateVersionImpl template = clientDoc.getItsNatStfulDocument().getItsNatStfulDocumentTemplateVersion();
        return render.canInsertChildNodeAsMarkupIgnoringOther(parent,newChildNode,template); // En el caso de único hijo obviamente los demás se ignoran pues no hay más
    }    
    
    public static InnerMarkupCodeImpl appendSingleChildNodeAsMarkup(Node newNode, ClientDocumentStfulDelegateWebImpl clientDoc,RenderElement render)
    {
        ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();
        String newNodeMarkup = itsNatDoc.serializeNode(newNode);
        if (DOMUtilInternal.isTheOnlyChildNode(newNode))
        {
            // Caso de único nodo
            Element parent = (Element)newNode.getParentNode();
            return render.appendChildrenCodeAsMarkup(null,parent,newNodeMarkup,clientDoc);
        }
        else // Caso de último nodo, sabemos que podemos usar el último InnerMarkupCodeImpl el cual está asociado al nodo padre
        {
            InnerMarkupCodeImpl lastCode = (InnerMarkupCodeImpl)clientDoc.getClientDocumentStful().getLastCodeToSend();
            lastCode.addInnerMarkup(newNodeMarkup);
            return null; // No se añade nada y se deja como último este lastCode
        }
    }    
    
    public static InnerMarkupCodeImpl createInnerMarkupCode(String parentVarName,Element parentNode,String childrenCode,ClientDocumentStfulDelegateImpl clientDoc,RenderElement render)
    {
        boolean useNodeLocation;
        String parentNodeLocator;
        if (parentVarName == null)
        {
            useNodeLocation = true;
            NodeLocationImpl parentLoc = clientDoc.getNodeLocation(parentNode,true);
            parentNodeLocator = parentLoc.toScriptNodeLocation(true);
        }
        else
        {
            useNodeLocation = false;
            parentNodeLocator = parentVarName;
        }

        return new InnerMarkupCodeImpl(render,parentNode,parentNodeLocator,useNodeLocation,childrenCode);    
    }
}
