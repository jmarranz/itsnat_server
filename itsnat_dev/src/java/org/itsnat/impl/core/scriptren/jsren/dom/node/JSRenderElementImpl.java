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

package org.itsnat.impl.core.scriptren.jsren.dom.node;


import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.scriptren.shared.dom.node.CannotInsertAsMarkupCauseImpl;
import org.itsnat.impl.core.scriptren.shared.dom.node.InsertAsMarkupInfoImpl;
import org.itsnat.impl.core.scriptren.shared.dom.node.InnerMarkupCodeImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.domutil.NodeConstraints;
import org.itsnat.impl.core.scriptren.jsren.dom.node.html.JSRenderHTMLElementImpl;
import org.itsnat.impl.core.scriptren.jsren.dom.node.otherns.JSRenderOtherNSElementImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.itsnat.impl.core.template.ItsNatStfulDocumentTemplateVersionImpl;
import org.itsnat.impl.core.template.MarkupTemplateVersionImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderElementImpl extends JSRenderHasChildrenNodeImpl implements NodeConstraints
{

    /** Creates a new instance of JSElementRender */
    public JSRenderElementImpl()
    {
    }

    public static JSRenderElementImpl getJSRenderElement(Element elem,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        if (DOMUtilHTML.isHTMLElement(elem))
            return JSRenderHTMLElementImpl.getJSRenderHTMLElement(clientDoc.getBrowserWeb());
        else  // El elemento puede estar en un documento (X)HTML pero no es HTML o XHTML. En documentos no HTML todos los elementos tienen un namespace o DEBERIA declararse en el root del documento
            return JSRenderOtherNSElementImpl.getJSRenderOtherNSElement(elem,clientDoc);
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
        return "itsNatDoc.doc.createElement(\"" + tagName + "\")";
    }

    public String addAttributesBeforeInsertNode(Node node,String elemVarName,ClientDocumentStfulDelegateImpl clientDoc)
    {
        ClientDocumentStfulDelegateWebImpl clientDocWeb = (ClientDocumentStfulDelegateWebImpl)clientDoc;
        Element elem = (Element)node;
        StringBuilder code = new StringBuilder();
        NamedNodeMap attribList = elem.getAttributes();
        for(int i = 0; i < attribList.getLength(); i++)
        {
            Attr attr = (Attr)attribList.item(i);
            JSRenderAttributeImpl render = JSRenderAttributeImpl.getJSRenderAttribute(attr,elem,clientDocWeb);
            code.append( render.setAttributeCode(attr,elem,elemVarName,true,clientDoc) );
        }
        return code.toString();
    }


    @Override
    public Object appendChildNodes(Node parent, String parentVarName,boolean beforeParent,InsertAsMarkupInfoImpl insertMarkupInfo,ClientDocumentStfulDelegateImpl clientDoc)
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

    @Override    
    public Object getInsertNewNodeCode(Node newNode,ClientDocumentStfulDelegateWebImpl clientDoc)
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

    public abstract String getAppendChildrenCodeAsMarkupSentence(InnerMarkupCodeImpl innerMarkupRender,ClientDocumentStfulDelegateWebImpl clientDoc);

    protected CannotInsertAsMarkupCauseImpl canInsertSingleChildNodeAsMarkup(Node newChildNode,ClientDocumentStfulDelegateWebImpl clientDoc)
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
        return canInsertChildNodeAsMarkupIgnoringOther(parent,newChildNode,template); // En el caso de único hijo obviamente los demás se ignoran pues no hay más
    }

    protected InnerMarkupCodeImpl appendSingleChildNodeAsMarkup(Node newNode, ClientDocumentStfulDelegateWebImpl clientDoc)
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

    protected InnerMarkupCodeImpl appendChildrenAsMarkup(String parentVarName, Node parentNode, ClientDocumentStfulDelegateImpl clientDoc)
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

    protected abstract boolean isInsertChildNodesAsMarkupCapable(Element parent,MarkupTemplateVersionImpl template);

    public boolean match(Node node, Object context)
    {
        // Esto es por claridad pues "match" no nos dice mucho sobre lo que tenemos que hacer
        return isChildNotValidInsertedAsMarkup(node,(MarkupTemplateVersionImpl)context);
    }

    public abstract boolean isChildNotValidInsertedAsMarkup(Node childNode,MarkupTemplateVersionImpl template);

    protected CannotInsertAsMarkupCauseImpl canInsertChildNodeAsMarkupIgnoringOther(Element parent,Node childNode,MarkupTemplateVersionImpl template)
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
        return null;
    }

    public Node getFirstChildIsNotValidInsertedAsMarkup(Element parent,MarkupTemplateVersionImpl template)
    {
        return DOMUtilInternal.getFirstContainedNodeMatching(parent,this,template);
    }

    protected InnerMarkupCodeImpl appendChildrenCodeAsMarkup(String parentVarName,Element parentNode,String childrenCode,ClientDocumentStfulDelegateImpl clientDoc)
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

        return new InnerMarkupCodeJSImpl(this,parentNode,parentNodeLocator,useNodeLocation,childrenCode);
    }


    public abstract String getCurrentStyleObject(String itsNatDocVar,String elemName,ClientDocumentStfulDelegateWebImpl clientDoc);

    protected String bindBackupAndSetStylePropertyMethod(String methodName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        // Usamos variables temporales asociadas al objeto Element y no al propio "style",
        // por:
        // Safari (3.1 al menos) parece que recrea el objeto style tras un display='none" en algunos casos por lo que perderíamos la variable backup.

        // Aseguramos que no se asigne un valor "undefined" pues navegadores como Gecko ignoran una asignación con valor "undefined" y no cambian el valor,
        // el problema es que debemos restaurar el valor anterior y está claro que restaurar un undefined no es posible,
        // el valor "" viene a ser el valor por defecto habitual.

        // Nota: Opera Mobile 9.5 SVG ignora la propiedad "dispose" (sin error), solucionado en 9.7

        StringBuilder code = new StringBuilder();

        code.append( "var func = function (elem,propName,newValue)" );
        code.append( "{" );
        code.append( "  if (typeof elem.style == \"undefined\") return;"); // Esto ocurre por ejemplo con <script> en algun navegador (no me acuerdo) y con <foreignObject> en SVG en Opera Mobile 9.5.
        code.append( "  var name = \"style_itsnat_\" + propName;" );
        code.append( "  var cssProp = elem.style[propName];");
        code.append( "  if (typeof cssProp == \"undefined\") cssProp = \"\";" );
        code.append( "  this.setPropInNative(elem,name,cssProp);");
        code.append( "  elem.style[propName] = newValue;" );
        code.append( "};" );

        code.append( "itsNatDoc." + methodName + " = func;\n" );

        clientDoc.bindClientMethod(methodName);

        return code.toString();
    }

    public String getBackupAndSetStylePropertyMethodName()
    {
        return "backupSetStyleProp";
    }

    public String bindBackupAndSetStylePropertyMethod(ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        StringBuilder code = new StringBuilder();

        String methodName = getBackupAndSetStylePropertyMethodName();
        if (!clientDoc.isClientMethodBounded(methodName))
            code.append(bindBackupAndSetStylePropertyMethod(methodName,clientDoc));

        return code.toString();
    }

    public String getBackupAndSetStyleProperty(String elemVarName,String propName,String newValue,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        StringBuilder code = new StringBuilder();

        String methodName = getBackupAndSetStylePropertyMethodName();
        if (!clientDoc.isClientMethodBounded(methodName))
            code.append(bindBackupAndSetStylePropertyMethod(methodName,clientDoc));

        code.append("itsNatDoc." + methodName + "(" + elemVarName + ",\"" + propName + "\",\"" + newValue + "\");\n");
        return code.toString();
    }

    protected String bindRestoreBackupStylePropertyMethod(String methodName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        StringBuilder code = new StringBuilder();

        code.append( "var func = function (elem,propName)" );
        code.append( "{" );
        code.append( "  if (typeof elem.style == \"undefined\") return;");
        code.append( "  var name = \"style_itsnat_\" + propName;" );
        code.append( "  var cssProp = this.getPropInNative(elem,name);\n");
        code.append( "  if (cssProp == null) return;\n"); // No se salvó
        code.append( "  elem.style[propName] = cssProp;\n");
        code.append( "  this.removePropInNative(elem,name);\n");
        code.append( "};" );

        code.append( "itsNatDoc." + methodName + " = func;\n" );

        clientDoc.bindClientMethod(methodName);

        return code.toString();
    }

    public String getRestoreBackupPropertyMethodName()
    {
        return "restoreBackupStyleProp";
    }

    public String bindRestoreBackupStylePropertyMethod(ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        StringBuilder code = new StringBuilder();

        String methodName = getRestoreBackupPropertyMethodName();
        if (!clientDoc.isClientMethodBounded(methodName))
            code.append(bindRestoreBackupStylePropertyMethod(methodName,clientDoc));

        return code.toString();
    }

    public String getRestoreBackupStyleProperty(String elemVarName,String propName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        StringBuilder code = new StringBuilder();

        String methodName = getRestoreBackupPropertyMethodName();
        if (!clientDoc.isClientMethodBounded(methodName))
            code.append(bindRestoreBackupStylePropertyMethod(methodName,clientDoc));

        code.append("itsNatDoc." + methodName + "(" + elemVarName + ",\"" + propName + "\");\n");
        return code.toString();
    }

    /* Problema de SVG */
    public abstract boolean isInsertedScriptNotExecuted(Element script,ClientDocumentStfulDelegateImpl clientDoc);

    // Algunos navegadores WebKit tal y como el primer S60WebKit 
    // no ejecutan el código dentro del <script> si se añade después de estar ya insertado en el documento
    public abstract boolean isTextAddedToInsertedScriptNotExecuted(Element script,ClientDocumentStfulDelegateWebImpl clientDoc);


    public boolean isScriptWithContent(Node newNode)
    {
        return (newNode.hasChildNodes() &&
                newNode.getLocalName().equals("script"));
    }

    public boolean isScriptWithSingleChildTextNode(Node newNode)
    {
        return (isScriptWithContent(newNode) &&
               (newNode.getFirstChild() == newNode.getLastChild()) && // Un único hijo
                newNode.getFirstChild() instanceof CharacterData);
    }

    @Override
    protected String getAppendCompleteChildNode(String parentVarName,Node newNode,String newNodeCode,ClientDocumentStfulDelegateImpl clientDoc)
    {
        if (isScriptWithSingleChildTextNode(newNode) && isInsertedScriptNotExecuted((Element)newNode,clientDoc) )
        {
            // Aunque el código del <script> ya ha sido insertado en el <script>, al insertar
            // el <script> de todas formas no se ejecutará, hemos de usar un eval tras insertar el <script>

            // newNodeCode debe ser la variable del nuevo <script>

            StringBuilder code = new StringBuilder();
            code.append( super.getAppendCompleteChildNode(parentVarName, newNode, newNodeCode,clientDoc) );

            // Lo anterior no sirve, no se ha ejecutado, lo ejecutamos "manualmente"
            code.append( "eval(" + newNodeCode + ".firstChild.data);");

            return code.toString();
        }
        else
            return super.getAppendCompleteChildNode(parentVarName, newNode, newNodeCode,clientDoc);
    }

    @Override
    protected String getInsertCompleteNodeCode(Node newNode,String newNodeCode,ClientDocumentStfulDelegateImpl clientDoc)
    {
        if (isScriptWithSingleChildTextNode(newNode) && isInsertedScriptNotExecuted((Element)newNode,clientDoc) )
        {
            // Aunque el código del <script> haya sido insertado en el <script>, al insertar
            // el <script> de todas formas no se ejecutará, hemos de usar un eval tras insertar el <script>

            // Si este método ha sido llamado y <script> tiene un nodo
            // de texto hijo entonces necesariamente newNodeCode es una variable JavaScript
            // pues dicha variable se necesita para insertar después de este método
            // los nodos hijos
            StringBuilder code = new StringBuilder();
            code.append( super.getInsertCompleteNodeCode(newNode, newNodeCode,clientDoc) );

            // Lo anterior no sirve, no se ha ejecutado, lo ejecutamos "manualmente"
            code.append( "eval(" + newNodeCode + ".firstChild.data);");

            return code.toString();
        }
        else
            return super.getInsertCompleteNodeCode(newNode,newNodeCode,clientDoc);
    }

    public boolean isAddChildNodesBeforeNode(Node parent,ClientDocumentStfulDelegateImpl clientDoc)
    {
        // Algunos navegadores WebKit tal y como el primer S60WebKit 
        // no ejecutan el código dentro del <script> una vez insertado en el documento
        // si dicho código se inserta después de la inserción del <script> en el documento
        // Esto es la forma normal de inserción en ItsNat, por ello en este caso
        // añadimos el nodo de texto con el código al nodo script antes de insertar en el doc.

        return isScriptWithContent(parent);
    }
}
