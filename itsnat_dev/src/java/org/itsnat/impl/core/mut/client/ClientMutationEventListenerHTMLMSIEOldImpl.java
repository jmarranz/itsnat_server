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

package org.itsnat.impl.core.mut.client;

import java.util.Map;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.SVGWebInfoImpl;
import org.itsnat.impl.core.jsren.dom.node.JSRenderAttributeImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.jsren.dom.node.html.msie.JSRenderHTMLTextMSIEOldImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.events.MutationEvent;

/**
 *
 * @author jmarranz
 */
public class ClientMutationEventListenerHTMLMSIEOldImpl extends ClientMutationEventListenerHTMLImpl
{
    public ClientMutationEventListenerHTMLMSIEOldImpl(ClientDocumentStfulImpl clientDoc)
    {
        super(clientDoc);
    }

    public boolean isSVGNodeAndAdobeSVGInline(Node node)
    {
        // Si no hemos detectado SVGWeb suponemos ASV inline
        return NamespaceUtil.isSVGNode(node) &&
               !SVGWebInfoImpl.isSVGWebEnabled(clientDoc);
    }

    public Map preRenderAndSendMutationCode(MutationEvent mutEvent)
    {
        Map ctx = super.preRenderAndSendMutationCode(mutEvent);

        String type = mutEvent.getType();

        if (type.equals("DOMNodeInserted"))
        {
            Node newNode = (Node)mutEvent.getTarget();
            fixTreeNamespaces(newNode);
        }
        else if (type.equals("DOMNodeRemoved"))
        {
            Node node = (Node)mutEvent.getTarget();
            Node parentNode = node.getParentNode();
            if (isSVGNodeAndAdobeSVGInline(parentNode))
            {
                // Suponemos ASV inline
                // Ver notas en DOMNodeInserted
                // Si el nodo a eliminar es el nodo raíz de un trozo SVG (el padre ya no es SVG) no es necesario hacer
                // nada especial porque aunque se reinserte en el servidor, en el cliente será un nodo nuevo,
                // por eso preguntamos si es el padre el SVG (para evitar el caso de nodo raíz SVG).
                fixTreeRemovedSVGNodeInAdobeSVGInline(node);
            }
        }

        return ctx;
    }

    public void postRenderAndSendMutationCode(MutationEvent mutEvent,Map context)
    {
        super.postRenderAndSendMutationCode(mutEvent,context);

        // No consideramos "desregistrar" namespaces al eliminar nodos
        // con namespaces, por una parte porque la colección document.namespaces
        // no tiene método remove y por otra parte porque otro trozo de código
        // en otro lugar podría necesitarlo. No pasa nada por dejarlo declarado en el cliente.
        String type = mutEvent.getType();

        if (type.equals("DOMNodeInserted"))
        {
            Node newNode = (Node)mutEvent.getTarget();
            Node parentNode = newNode.getParentNode();
            if (isSVGNodeAndAdobeSVGInline(parentNode))
            {
                // Suponemos ASV inline
                // Sólo procesamos el nodo insertado si es hijo de un elemento SVG
                // porque si es hijo de un elemento X/HTML entonces es un trozo de SVG nuevo,
                // el ASV lo detectará y sabemos que lo procesará el propio ASV creando los elementos SVG
                // espejo (peer) automáticamente, aunque lo hará inmediatamente después de que se termine el script
                // (o que se muestre un alert) por lo que la propiedad _svg_peer no estará presente
                // y por tanto la llamada no haría nada.

                fixTreeInsertedSVGNodeInAdobeSVGInline(newNode);
            }
        }
        else if (type.equals("DOMAttrModified"))
        {
            Element elem = (Element)mutEvent.getTarget();
            if (isSVGNodeAndAdobeSVGInline(elem))
            {
                // Suponemos ASV inline
                Attr attr = (Attr)mutEvent.getRelatedNode();
                int changeType = mutEvent.getAttrChange();
                fixAttrModifiedSVGNodeInAdobeSVGInline(elem,attr,changeType);
            }
        }
        else if (type.equals("DOMCharacterDataModified"))
        {
            CharacterData charDataNode = (CharacterData)mutEvent.getTarget();
            if ((charDataNode instanceof Text) &&
                 isSVGNodeAndAdobeSVGInline(charDataNode))
            {
                // Suponemos ASV inline
                fixTextDataModifiedSVGNodeInAdobeSVGInline((Text)charDataNode);
            }
        }
    }

    private void fixTreeNamespaces(Node node)
    {
        // Solucionamos el problema de insertar via JavaScript un elemento con namespaces,
        // por ejemplo <svg:svg xmlns:svg="...">, pues al crear el elemento via createElement("svg:svg")
        // tiene dos comportamientos muy diferentes:
        // 1) Si el prefijo svg no ha sido declarado en <html> como <html xmlns:svg="..."> o bien
        //    no está en la colección document.namespaces, el método funciona pero el elemento creado
        //    es una especie de elemento desconocido que por ejemplo no admite nodos hijo.
        // 2) En caso contrario el elemento es creado correctamente siendo la propiedad tagUrn equivalente a la W3C namespaceURI
        //    y scopeName equivalente al prefix W3C.
        // Algo similar ocurre si el elemento es insertado dentro de un innerHTML.
        // Por tanto es necesario añadir el namespace al <html> o a document.namespaces antes
        // de la inserción, en xmlns:svg="..." en el propio elemento NO tiene ningún efecto.
        // Como en carga ya se soluciona este problema via añadiendo atributos en <html> antes de enviar al cliente,
        // aquí tenemos que usar document.namespaces pues tras la carga definir atributos namespace en <html> no hace nada

        // => document.namespaces : http://msdn.microsoft.com/en-us/library/ms537470%28VS.85%29.aspx
        // => namespace object : http://msdn.microsoft.com/en-us/library/ms535854%28VS.85%29.aspx

        StringBuilder code = fixTreeNamespaces(node,null);

        if ((code != null) && (code.length() > 0))
            clientDoc.addCodeToSend(code.toString());
    }

    private StringBuilder fixTreeNamespaces(Node node,StringBuilder code)
    {
        if (node.getNodeType() != Node.ELEMENT_NODE) return code;

        Element elem = (Element)node;

        if (elem.hasAttributes())
        {
            NamedNodeMap attribs = elem.getAttributes();
            for(int i = 0; i < attribs.getLength(); i++)
            {
                Attr attr = (Attr)attribs.item(i);
                String prefix = attr.getPrefix();
                if ((prefix != null) && prefix.equals("xmlns"))
                {
                    // No consideramos el caso de xmlns:itsnat="itsnat namespace"
                    // porque nunca enviaremos elementos al cliente tal y como
                    // <itsnat:nombre>... pues estos se procesan y se substituyen en el servidor.
                    // Evitando este caso, evitamos poner en <html> la declaración xmlns:itsnat que no
                    // ha hecho el programador y que puede confundirle.
                    String value = attr.getValue();
                    if (NamespaceUtil.isItsNatNamespace(value))
                        continue;

                    String localName = attr.getName().substring(prefix.length() + 1);

                    // Si el namespace ya estuviera definido lo actualiza con el nuevo URL (no hay duplicidad)
                    // esto hace que este método no sirva para casos en donde se utiliza el mismo prefijo para varios
                    // URLs diferentes.
                    if (code == null) code = new StringBuilder();
                    code.append("itsNatDoc.doc.namespaces.add(\"" + localName + "\",\"" + value + "\");\n");
                }
            }
        }

        Node child = elem.getFirstChild();
        while (child != null)
        {
            code = fixTreeNamespaces(child,code);
            child = child.getNextSibling();
        }

        return code;
    }

    private void fixTreeInsertedSVGNodeInAdobeSVGInline(Node node)
    {
        StringBuilder code = new StringBuilder();

        String methodName = "fixSVGNodeInsertedAdobeSVG";
        if (!clientDoc.isClientMethodBounded(methodName))
            code.append(bindFixSVGNodeInsertedAdobeSVGMethod(methodName));

        String jsRef = clientDoc.getNodeReference(node,true,true);

        code.append("itsNatDoc." + methodName + "(" + jsRef + ");\n");

        clientDoc.addCodeToSend(code);
    }

    private String bindFixSVGNodeInsertedAdobeSVGMethod(String methodName)
    {
        // El Adobe SVG Viewer puede renderizar SVG inline dentro de un documento X/HTML
        // en MSIE usando namespaces <object classid="..."> e <import namespace...>
        // En este modo el funcionamiento es MUY PECULIAR, pues cuando se inserta un trozo de markup SVG
        // ya sea en carga, o via DOM o innerHTML, el ASV detecta esta inserción y, o bien cuando se carga
        // el plugin o cuando el script que realizó la inserción termina acaba (setTimeout(func,0) puede ser útil),
        // renderiza el markup visualmente.
        // Resultado de esta renderización es que el ASV crea un DOM simétrico al del MSIE pero siguiendo el W3C
        // con un documento por cada bloque de markup SVG inline, siendo el root el elemento
        // root del bloque SVG. Ambos DOMs (MSIE y ASV) están vinculados a través de una propiedad "_svg_peer"
        // que tienen los elementos en el DOM MSIE, el _svg_peer apunta al elemento en el DOM ASV,
        // en el DOM ASV podemos obtener el documento ASV via ownerDocument a través de un elemento ASV o "peer".
        // El problema surge si queremos modificar un bloque SVG YA renderizado, ASV sólo crea
        // el DOM ASV paralelo si el bloque no fue renderizado, pero si ya fue renderizado los cambios en el DOM MSIE
        // son ignorados.
        // Sin embargo lo interesante es que si hacemos cambios "manualmente" en el DOM ASV, el ASV
        // los procesa y modifica visualmente el bloque SVG. Al parecer el MSIE notifica al ASV cuando
        // un nodo SVG raíz se inserta (tiene que ver con los "behaviors") pero no es capaz de notificar ante cambios dentro del mismo
        // (pues no hay mutation listeners en MSIE).
        // Por tanto, nuestro objetivo es replicar los nodos con significado visual SVG del DOM MSIE en el DOM ASV
        // si detectamos que ya ha sido renderizado el bloque (hay un _svg_peer),
        // creando nodos ASV manualmente y vinculándolos con el DOM MSIE usando
        // _svg_peer definidos manualmente.
        // En los nodos de texto el ASV no define _svg_peer, por ello nos aprovechamos
        // de la idea de que en SVG los nodos de texto con texto significativo son siempre (?)
        // el único nodo hijo (aunque también puedan existir comentarios, nos referimos
        // a que nodos elemento hijos). De esta forma evitaremos indirectamente los nodos
        // con espacios etc que esos sí que estarán mezclados con elementos.

        StringBuilder code = new StringBuilder();
        code.append("var func = function(node)\n");
        code.append("{\n");
        code.append("  if (node == null) return;"); // Posible caso de nodo texto con cadena nula (en el servidor puede existir en el cliente puede que no)
        code.append("  var parentPeer = node.parentNode._svg_peer;");
        code.append("  if (!parentPeer) return;"); // El contenedor no se ha renderizado todavía
        code.append("  var svgDoc = parentPeer.ownerDocument;");
        code.append("  var nodePeer;");
        code.append("  if (node.nodeType == 1)"); // Node.ELEMENT_NODE
        code.append("  {");
        code.append("    nodePeer = svgDoc.createElement(node.nodeName);");  // Tanto nodeName como tagName no incluyen el prefijo
        code.append("    node._svg_peer = nodePeer;");
        code.append("    for(var i = 0; i < node.attributes.length; i++)");
        code.append("    {");
        code.append("      var attr = node.attributes[i];");
        code.append("      if (attr.specified) nodePeer.setAttribute(attr.name,this.getAttribute(node,attr.name));");
        code.append("    }");
        code.append("    var sibElem = node.nextSibling;");
        code.append("    while((sibElem != null) && !sibElem._svg_peer) sibElem = sibElem.nextSibling;"); // De esta manera filtramos nodos de texto etc
        code.append("    var sibElemPeer = sibElem != null? sibElem._svg_peer : null;");
        code.append("    parentPeer.insertBefore(nodePeer,sibElemPeer);");
        code.append("  }");
        code.append("  else if (node.nodeType == 3)"); // Node.TEXT_NODE
        code.append("  {");
                         // El _svg_peer no está definido en nodos de texto (sólo elementos) por el ASV
                         // Afortunadamente todos los nodos de texto con texto significativo son nodos hijo únicos en SVG (CREO, si no es así no funcionará)
        code.append("    var childNodes = node.parentNode.childNodes;");
        code.append("    var i;");
        code.append("    for(i = 0; i < childNodes.length; i++)");
        code.append("    {");
        code.append("      var currNode = childNodes[i];");
        code.append("      if (node == currNode) continue;");
        code.append("      if ((currNode.nodeType == 1)||(currNode.nodeType == 3)) break;");
        code.append("    }");
        code.append("    if (i != childNodes.length) return;"); // Además del nodo de texto hay algún elemento hijo u otro nodo de texto (los comentarios etc no nos interesan), ignoramos el nodo de texto porque seguramente es un separador
        code.append("    nodePeer = svgDoc.createTextNode(node.data);");
        code.append("    parentPeer.appendChild(nodePeer);");
        code.append("  }");
        code.append("  else return;"); // No consideramos añadir como peer otros tipos de nodos tal y como comentarios pues no tienen impacto visual.

        code.append("  if (!node.hasChildNodes()) return;");
        code.append("  for(var i = node.childNodes.length - 1; i >= 0; i--)"); // Importante iterar al revés por el cálculo del nextSibling
        code.append("  {");
        code.append("    var childNode = node.childNodes[i];");
        code.append("    this." + methodName + "(childNode);");
        code.append("  }");

        code.append("};\n");
        code.append("itsNatDoc." + methodName + " = func;\n");

        clientDoc.bindClientMethod(methodName);

        return code.toString();
    }

    private void fixTreeRemovedSVGNodeInAdobeSVGInline(Node node)
    {
        // Si el nodo a eliminar está dentro del SVG necesitamos remover los peer también

        StringBuilder code = new StringBuilder();

        String methodName = "fixSVGNodeRemovedAdobeSVG";
        if (!clientDoc.isClientMethodBounded(methodName))
            code.append(bindFixSVGNodeRemovedAdobeSVGMethod(methodName));

        String nodeRef = clientDoc.getNodeReference(node,false,true); // No cacheamos pues se va a eliminar

        code.append("itsNatDoc." + methodName + "(" + nodeRef + ");\n");

        clientDoc.addCodeToSend(code);
    }

    private String bindFixSVGNodeRemovedAdobeSVGMethod(String methodName)
    {
        StringBuilder code = new StringBuilder();
        code.append("var func = function(node)\n");
        code.append("{\n");
        code.append("  if (node == null) return;"); // Posible caso de nodo texto con cadena nula (en el servidor puede existir en el cliente puede que no)
        code.append("  var parentPeer = node.parentNode._svg_peer;");
        code.append("  if (!parentPeer) return;"); // El contenedor no se ha renderizado todavía
        code.append("  var nodePeer;");
        code.append("  if (node.nodeType == 1)"); // Node.ELEMENT_NODE
        code.append("    nodePeer = node._svg_peer;");
        code.append("  else if (node.nodeType == 3)"); // Node.TEXT_NODE
        code.append("  {");
                        // Afortunadamente todos los nodos de texto con texto significativo son nodos hijo únicos en SVG (CREO, si no es así no funcionará)
        code.append("    var childNodes = node.parentNode.childNodes;");
        code.append("    var i;");
        code.append("    for(i = 0; i < childNodes.length; i++)");
        code.append("    {");
        code.append("      var currNode = childNodes[i]; ");
        code.append("      if (node == currNode) continue;");
        code.append("      if ((currNode.nodeType == 1)||(currNode.nodeType == 3)) break;");
        code.append("    }");
        code.append("    if (i != childNodes.length) return;"); // Además del nodo de texto hay algún elemento hijo u otro nodo de texto (los comentarios etc no nos interesan), ignoramos el nodo de texto porque seguramente es un separador
        code.append("    nodePeer = parentPeer.firstChild;");
        code.append("    while((nodePeer != null)&&(nodePeer.nodeType != 3)) nodePeer = nodePeer.nextSibling;"); // Filtramos posibles comentarios etc que el propio ASV podría haber añadido como peer si estaban en el DOM MSIE cuando se renderizó por primera vez por ASV
        code.append("    if (!nodePeer) return;"); // No existe, no hace falta eliminar (probablemente en el servidor el nodo texto tenía cadena nula)
        code.append("    nodePeer.data = '';"); // ES NECESARIO
        code.append("  }");
        code.append("  else return;"); // No consideramos otros nodos sin impacto visual

        code.append("  parentPeer.removeChild(nodePeer);");
        code.append("};\n");
        code.append("itsNatDoc." + methodName + " = func;\n");

        clientDoc.bindClientMethod(methodName);

        return code.toString();
    }

    private void fixAttrModifiedSVGNodeInAdobeSVGInline(Element elem,Attr attr,int changeType)
    {
        String valueJS = null;
        switch(changeType)
        {
            case MutationEvent.ADDITION:
            case MutationEvent.MODIFICATION:
                // El método toJSAttrValue(...) hace cosas que no nos interesan en este contexto
                valueJS = JSRenderAttributeImpl.toTransportableStringLiteral(attr.getValue(),clientDoc.getBrowser());
                break;
            case MutationEvent.REMOVAL:
                valueJS = "null";
                break;
            // No hay más casos
        }

        StringBuilder code = new StringBuilder();

        String methodName = "fixSVGNodeAttrAdobeSVG";
        if (!clientDoc.isClientMethodBounded(methodName))
            code.append(bindFixSVGNodeAttrAdobeSVGMethod(methodName));

        String jsRef = clientDoc.getNodeReference(elem,false,true); // No cacheamos pues se va a eliminar

        code.append("itsNatDoc." + methodName + "(" + jsRef + ",\"" + attr.getName() + "\"," + valueJS + "," + changeType + ");\n");

        clientDoc.addCodeToSend(code);
    }

    private String bindFixSVGNodeAttrAdobeSVGMethod(String methodName)
    {
        StringBuilder code = new StringBuilder();
        code.append("var func = function(node,name,value,action)\n");
        code.append("{\n");
        code.append("  var nodePeer = node._svg_peer;");
        code.append("  if (!nodePeer) return;"); // El contenedor no se ha renderizado todavía
        code.append("  if (action == 3) nodePeer.removeAttribute(name);"); // MutationEvent.REMOVAL
        code.append("  else nodePeer.setAttribute(name,value);");
        code.append("};\n");
        code.append("itsNatDoc." + methodName + " = func;\n");

        clientDoc.bindClientMethod(methodName);

        return code.toString();
    }

    private void fixTextDataModifiedSVGNodeInAdobeSVGInline(Text node)
    {
        JSRenderHTMLTextMSIEOldImpl render = JSRenderHTMLTextMSIEOldImpl.SINGLETON;
        String dataJS = render.dataTextToJS(node, clientDoc);

        StringBuilder code = new StringBuilder();

        String methodName = "fixSVGNodeTextModAdobeSVG";
        if (!clientDoc.isClientMethodBounded(methodName))
            code.append(bindFixSVGNodeTextModifiedAdobeSVGMethod(methodName));

        String jsRef = clientDoc.getNodeReference(node,false,true); // No cacheamos pues se va a eliminar

        code.append("itsNatDoc." + methodName + "(" + jsRef + "," + dataJS + ");\n");

        clientDoc.addCodeToSend(code);
    }

    private String bindFixSVGNodeTextModifiedAdobeSVGMethod(String methodName)
    {
        StringBuilder code = new StringBuilder();
        code.append("var func = function(node,data)\n");
        code.append("{\n");
        code.append("  if (node == null) return;"); // Posible caso de nodo texto con cadena nula (en el servidor puede existir en el cliente puede que no)
        code.append("  var parentPeer = node.parentNode._svg_peer;");
        code.append("  if (!parentPeer) return;"); // El contenedor no se ha renderizado todavía
        code.append("  var nodePeer;");
                        // Afortunadamente todos los nodos de texto con texto significativo son nodos hijo únicos en SVG (CREO, si no es así no funcionará)
        code.append("  var childNodes = node.parentNode.childNodes;");
        code.append("  var i;");
        code.append("  for(i = 0; i < childNodes.length; i++)");
        code.append("  {");
        code.append("    var currNode = childNodes[i]; ");
        code.append("    if (node == currNode) continue;");
        code.append("    if ((currNode.nodeType == 1)||(currNode.nodeType == 3)) break;");
        code.append("  }");
        code.append("  if (i != childNodes.length) return;"); // Además del nodo de texto hay algún elemento hijo u otro nodo de texto (los comentarios etc no nos interesan), ignoramos el nodo de texto porque seguramente es un separador
        code.append("  nodePeer = parentPeer.firstChild;");
        code.append("  while((nodePeer != null) && (nodePeer.nodeType != 3)) nodePeer = nodePeer.nextSibling;"); // Filtramos posibles comentarios etc que el propio ASV podría haber añadido como peer si estaban en el DOM MSIE cuando se renderizó por primera vez por ASV
        code.append("  if (nodePeer == null)"); // No existe, probablemente en el servidor antes el nodo de texto tenía cadena nula
        code.append("  {");
        code.append("    var svgDoc = parentPeer.ownerDocument;");
        code.append("    nodePeer = svgDoc.createTextNode('');");
        code.append("    parentPeer.appendChild(nodePeer);");
        code.append("  }");
        code.append("  nodePeer.data = data;");
        code.append("};\n");
        code.append("itsNatDoc." + methodName + " = func;\n");

        clientDoc.bindClientMethod(methodName);

        return code.toString();
    }
}
