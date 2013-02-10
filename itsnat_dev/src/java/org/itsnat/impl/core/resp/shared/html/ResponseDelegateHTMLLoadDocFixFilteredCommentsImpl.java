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

package org.itsnat.impl.core.resp.shared.html;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatHTMLDocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.jsren.JSRenderImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLHeadElement;

/**
 *
 * @author jmarranz
 */
public class ResponseDelegateHTMLLoadDocFixFilteredCommentsImpl
{
    protected static final String FAKE_COMMENT_NAME_VALUE = "itsnatFakeCommName";
    protected static final String FAKE_COMMENT_ID_VALUE = "itsnatFakeCommId_";
    protected static final String PARENT_COMMENT_ID_ATTR_NAME = "idItsNatFakeCommParent";
    protected static final String NEXT_COMMENT_ID_ATTR_NAME = "idItsNatFakeCommPrevious";

    public static final Set ELEMENTS_NOT_VALID_CHILD_COMMENT = new HashSet();

    static
    {
        // Elementos que NO pueden tener elementos como hijo diferentes
        // a unos concretos y por tanto en donde no funcionaría el uso de
        // <span> o <style>

        // Es el caso de BlackBerry y S60WebKit FP 1 en donde los comentarios son siempre filtrados
        // en carga, por ello para conservarlos los substituimos temporalmente por <style> bajo <head> y <span> bajo <body>
        // pero este <span> es TAMBIEN filtrado en algunos elementos cuyos elementos hijos están
        // predeterminados. Aunque sólo
        // he estudiado el problema en TABLE, TBODY, TFOOT, THEAD y TR es razonable
        // que un <span> no puede ser hijo de elementos que sólo admiten
        // ciertos tipos de elementos como hijo, por ejemplo un <span> bajo un <table>.
        // En algunos de estos elementos los comentarios son filtrados ya a nivel
        // de servidor porque son problemáticos por ejemplo para el IE 6 sin "workaround".
        // proporcionado por ItsNat, por lo que no se dará el caso de necesitar reinserción
        // (pero por si acaso los incluimos aquí).

        ELEMENTS_NOT_VALID_CHILD_COMMENT.add("dl");
        ELEMENTS_NOT_VALID_CHILD_COMMENT.add("frameset");
        ELEMENTS_NOT_VALID_CHILD_COMMENT.add("html");
        ELEMENTS_NOT_VALID_CHILD_COMMENT.add("ol");
        ELEMENTS_NOT_VALID_CHILD_COMMENT.add("optgroup");
        ELEMENTS_NOT_VALID_CHILD_COMMENT.add("select");
        ELEMENTS_NOT_VALID_CHILD_COMMENT.add("table");
        ELEMENTS_NOT_VALID_CHILD_COMMENT.add("tbody");
        ELEMENTS_NOT_VALID_CHILD_COMMENT.add("tfoot");
        ELEMENTS_NOT_VALID_CHILD_COMMENT.add("thead");
        ELEMENTS_NOT_VALID_CHILD_COMMENT.add("tr");
        ELEMENTS_NOT_VALID_CHILD_COMMENT.add("ul");
    }

    protected LinkedList comments = new LinkedList();
    protected LinkedList fakeCommentList = new LinkedList();
    protected LinkedList holdCommentList = new LinkedList();
    protected ResponseDelegateHTMLLoadDocImpl responseParent;

    /**
     * Creates a new instance of ResponseDelegateHTMLLoadDocFixFilteredCommentsImpl
     */
    public ResponseDelegateHTMLLoadDocFixFilteredCommentsImpl(ResponseDelegateHTMLLoadDocImpl responseParent)
    {
        this.responseParent = responseParent;
    }

    public ClientDocumentStfulImpl getClientDocumentStful()
    {
        return responseParent.getClientDocumentStful();
    }

    public void preSerializeDocument()
    {
        // En este caso la estrategia es la siguiente: el DOM del documento
        // tiene nodos cacheados,
        // dichos nodos cacheados pueden tener comentarios que serán filtrados
        // en el cliente, sin embargo esos comentarios NO nos interesan pues
        // no se reflejan en el DOM del servidor por tanto no serán accedidos ni habrá cálculo
        // de paths ni path-ids de los mismos
        ItsNatHTMLDocumentImpl itsNatDoc = responseParent.getItsNatHTMLDocument();
        HTMLDocument doc = itsNatDoc.getHTMLDocument();

        replaceComments(doc);
    }

    public String postSerializeDocument(String docMarkup)
    {
        if (!comments.isEmpty())
        {
            restoreComments();

            addCodeToSendComments();
        }

        return docMarkup;
    }

    protected boolean replaceComments(HTMLDocument doc)
    {
        // Tenemos la garantía de que no hay comentarios en lugares donde
        // no puede ponerse un <style> bajo <head> y <span> bajo BODY pues fueron filtrados en la normalización.
        // El S60WebKit (FP 1) mueve los <style> bajo <body> a <head>
        // Sabemos que en este contexto los mutation events están desactivados

        HTMLHeadElement head = DOMUtilHTML.getHTMLHead(doc);
        replaceComments(head,"style");
        HTMLElement body = doc.getBody();
        replaceComments(body,"span");

        return !comments.isEmpty(); // Si false es que no hay ningun comentario, no se ha hecho nada
    }

    protected Node replaceComments(Node node,String localNameReplacing)
    {
        if (node.getNodeType() == Node.COMMENT_NODE)
        {
            Node nodeRes = node; // En un caso node es reemplazado por otro
            
            int index = comments.size(); // Antes de añadir, empezamos en cero
            Element parentNode = (Element)node.getParentNode();
            String idCommentValue = FAKE_COMMENT_ID_VALUE + index;
            if ((parentNode instanceof HTMLElement) &&
                 ELEMENTS_NOT_VALID_CHILD_COMMENT.contains(parentNode.getLocalName()))
            {
                Element targetNode;
                String attrName;
                Element nextNode = ItsNatTreeWalker.getNextSiblingElement(node);
                if (nextNode != null)
                {
                    targetNode = nextNode;
                    attrName = NEXT_COMMENT_ID_ATTR_NAME;
                }
                else
                {
                    targetNode = parentNode;
                    attrName = PARENT_COMMENT_ID_ATTR_NAME;
                }
                // Puede darse el caso de varios comentarios seguidos, por lo que añadimos los ids separados por comas
                String attrValue = targetNode.getAttribute(attrName);
                if (attrValue.equals("")) attrValue = idCommentValue;
                else attrValue = attrValue + "," + idCommentValue;
                DOMUtilInternal.setAttribute(targetNode,attrName,attrValue);
                holdCommentList.add(new Node[]{node,targetNode});
            }
            else
            {
                Document doc = node.getOwnerDocument();
                Element fakeComm = doc.createElementNS(NamespaceUtil.XHTML_NAMESPACE,localNameReplacing);
                DOMUtilInternal.setAttribute(fakeComm,"name",FAKE_COMMENT_NAME_VALUE);
                DOMUtilInternal.setAttribute(fakeComm,"id"  ,idCommentValue);
                parentNode.replaceChild(fakeComm, node);
                fakeCommentList.add(new Node[]{node,fakeComm});
                nodeRes = fakeComm;  // Ha sido reemplazado
            }
            comments.add(node);

            return nodeRes;
        }
        else
        {
            if (!node.hasChildNodes()) return node;

            Node child = node.getFirstChild();
            while(child != null)
            {
                // Hay que tener en cuenta que replaceComments reemplaza el nodo child si es un comentario
                // por otro                
                child = replaceComments(child,localNameReplacing);

                child = child.getNextSibling();
            }
            
            return node;
        }
   }


    protected void restoreComments()
    {
        if (comments.isEmpty()) return;

        // Restauramos el estado anterior del DOM
        if (!fakeCommentList.isEmpty())
        {
            for(Iterator it = fakeCommentList.iterator(); it.hasNext(); )
            {
                Node[] pair = (Node[])it.next();
                Comment comm = (Comment)pair[0];
                Element fakeComm = (Element)pair[1];
                fakeComm.getParentNode().replaceChild(comm,fakeComm);
            }
        }

        if (!holdCommentList.isEmpty())
        {
            for(Iterator it = holdCommentList.iterator(); it.hasNext(); )
            {
                Node[] pair = (Node[])it.next();
                //Comment comm = (Comment)pair[0];
                Element container = (Element)pair[1];
                container.removeAttribute(NEXT_COMMENT_ID_ATTR_NAME);
                container.removeAttribute(PARENT_COMMENT_ID_ATTR_NAME);
            }
        }
    }

    protected void addCodeToSendComments()
    {
        ItsNatHTMLDocumentImpl itsNatDoc = responseParent.getItsNatHTMLDocument();
        ClientDocumentStfulImpl clientDoc = getClientDocumentStful();

        StringBuilder code = new StringBuilder();

        // Metemos una función para evitar dejar variables globales
        code.append("var func = function()\n");
        code.append("{\n");
        code.append("  var commMap = new Object();\n");

    int i = 0;
    for(Iterator it = comments.iterator(); it.hasNext(); )
    {
        Comment comm = (Comment)it.next();
        String text = comm.getData();
        // Sabemos que text es posible que contenga un ${} indicando que el comentario
        // fue cacheado, "descacheamos" aquí.
        text = itsNatDoc.resolveCachedNodes(text,false); // La verdad es que el resolveEntities da igual en este caso
        code.append("  commMap[\"" + (FAKE_COMMENT_ID_VALUE + i) + "\"] = " + JSRenderImpl.toTransportableStringLiteral(text,clientDoc.getBrowser()) + ";\n");
        i++;
    }

    if (!fakeCommentList.isEmpty())
    {
        // Resolvemos los comentarios que fueron substituidos por elemento "fake comment"
        code.append("  var fakeNodeList = document.getElementsByName(\"" + FAKE_COMMENT_NAME_VALUE + "\");\n");
        // Hacemos una copia porque un NodeList según el estándar es una colección viva por tanto
        // si quitamos los elementos del documento puede cambiar (de hecho es así en S60WebKit 5th v0.9)
        // y no queremos eso.
        code.append("  var len = fakeNodeList.length;\n");
        code.append("  var fakeNodeArr = new Array(len);\n");
        code.append("  for(var i=0;i < len; i++) fakeNodeArr[i] = fakeNodeList[i];\n");
        code.append("  fakeNodeList = null;\n"); // para ahorrar memoria
        code.append("  for(var i=0;i < len; i++)\n");
        code.append("  {\n");
        code.append("    var fakeCom = fakeNodeArr[i];\n");
        code.append("    var text = commMap[fakeCom.id];\n");
        code.append("    var comm = document.createComment(text);\n");
        code.append("    fakeCom.parentNode.replaceChild(comm,fakeCom);\n");
        code.append("  }\n");
        code.append("  fakeNodeArr = null;\n"); // para ahorrar memoria
    }

    if (!holdCommentList.isEmpty())
    {
        // Ahora los que fueron "recordados" a través de ids especiales
        // en el elemento padre o en el next
        // Insertaremos los comentarios en la misma zona seguidos, seguramente no coincidirá
        // con la posición respecto a los nodos de texto (si no son filtrados) pero dichos nodos
        // de texto son espacios y/o fines de línea que no influyen en el cálculo
        // de paths y es absurdo necesitar acceder a ellos desde el servidor (no valen para nada por eso
        // suelen ser filtrados).

        // Necesitamos esta función getNextNode para iterar (obtenida de ItsNatTreeWalker)
        code.append("  function getNextNode(node)\n");
        code.append("  {\n");
        code.append("    if (node == null) return null;\n");
        code.append("    var result = node.firstChild;\n");
        code.append("    if (result != null) return result;\n");
        code.append("    result = node.nextSibling;\n");
        code.append("    if (result != null) return result;\n");
        code.append("    var parentNode = node.parentNode;\n");
        code.append("    while (parentNode != null)\n");
        code.append("    {\n");
        code.append("      result = parentNode.nextSibling;\n");
        code.append("      if (result != null) return result;\n");
        code.append("      else parentNode = parentNode.parentNode;\n");
        code.append("    }\n");
        code.append("    return null;\n");
        code.append("  }\n");

        code.append("  function addComments(node,name,value,isNext,commMap)\n");
        code.append("  {\n");
        code.append("    var nextNode, parentNode;\n");
        code.append("    if (isNext) { nextNode = node; parentNode = node.parentNode; }\n");
        code.append("    else { nextNode = null; parentNode = node; }\n");
        code.append("    var idCommList = value.split(\",\");\n");
        code.append("    for(var i = 0; i < idCommList.length; i++)\n");
        code.append("    {\n");
        code.append("      var text = commMap[idCommList[i]];\n");
        code.append("      var comm = document.createComment(text);\n");
        code.append("      parentNode.insertBefore(comm,nextNode);\n");
        code.append("    }\n");
        code.append("    node.removeAttribute(name);\n"); // Ya no se necesita
        code.append("  }\n");

        code.append("  var node = document;\n");
        code.append("  do \n");
        code.append("  {\n");
        code.append("    if (node.nodeType == 1)\n");
        code.append("    {\n"); // Un mismo nodo puede ser referencia tanto padre como next
        code.append("      var name,value;\n");
        code.append("      name = \"" + NEXT_COMMENT_ID_ATTR_NAME + "\";\n");
        code.append("      value = node.getAttribute(name);\n");
        code.append("      if ((value!=null)&&(value!=\"\")) addComments(node,name,value,true,commMap);\n");
        code.append("      name = \"" + PARENT_COMMENT_ID_ATTR_NAME + "\";\n");
        code.append("      value = node.getAttribute(name);\n");
        code.append("      if ((value!=null)&&(value!=\"\")) addComments(node,name,value,false,commMap);\n");
        code.append("    }\n");
        code.append("    node = getNextNode(node);\n");
        code.append("  }\n");
        code.append("  while(node != null);\n");
    }

        code.append("};\n");
        code.append("func();\n");
        code.append("func = null;\n"); // No se necesita más, para liberar memoria

        // Insertamos AL PRINCIPIO para que el código generado por el usuario (que va después)
        // vea ya los comentarios como comentarios.
        responseParent.addFixDOMCodeToSend(code.toString());
    }
}
