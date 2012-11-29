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

package org.itsnat.impl.core.jsren.dom.node.html.msie;

import java.util.HashSet;
import java.util.Set;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.template.html.HTMLTemplateVersionDelegateImpl;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLOptionElement;

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLTextMSIEPocketImpl extends JSRenderHTMLTextMSIEOldImpl
{
    public static final JSRenderHTMLTextMSIEPocketImpl SINGLETON = new JSRenderHTMLTextMSIEPocketImpl();
    public static final Set HTML_BLOCK_ELEMENTS = new HashSet();
    public static final Set HTML_ELEM_CHILD_SPACES_SENSIBLE = new HashSet();

    static
    {
        // Consideramos los elementos tipo "block" de tal manera
        // que el navegador por defecto añade un fin de línea al principio
        // y al final, es decir, el comienzo del bloque está al ppio
        // y aunque no lleque al final del área contenedora por la derecha (caso de <table>) no hay nada en ese lado
        // Estos elementos nos permiten ignorar los textos con espacios y fines
        // de línea que estén entre medias pues no serán significativos en el layout y sin embargo
        // son costosísimos.

        // http://htmlhelp.com/reference/html40/block.html

        // The following are defined as block-level elements in HTML 4:
        HTML_BLOCK_ELEMENTS.add("address");
        HTML_BLOCK_ELEMENTS.add("blockquote");
        HTML_BLOCK_ELEMENTS.add("center");
        HTML_BLOCK_ELEMENTS.add("dir");
        HTML_BLOCK_ELEMENTS.add("div");
        HTML_BLOCK_ELEMENTS.add("dl");
        HTML_BLOCK_ELEMENTS.add("fieldset");
        HTML_BLOCK_ELEMENTS.add("form");
        HTML_BLOCK_ELEMENTS.add("h1");
        HTML_BLOCK_ELEMENTS.add("h2");
        HTML_BLOCK_ELEMENTS.add("h3");
        HTML_BLOCK_ELEMENTS.add("h4");
        HTML_BLOCK_ELEMENTS.add("h5");
        HTML_BLOCK_ELEMENTS.add("h6");
        HTML_BLOCK_ELEMENTS.add("hr");
        // HTML_BLOCK_ELEMENTS.add("isindex"); No tiene hijos
        HTML_BLOCK_ELEMENTS.add("menu");
        HTML_BLOCK_ELEMENTS.add("noframes");
        HTML_BLOCK_ELEMENTS.add("noscript");
        HTML_BLOCK_ELEMENTS.add("ol");
        HTML_BLOCK_ELEMENTS.add("p");
        HTML_BLOCK_ELEMENTS.add("pre");
        HTML_BLOCK_ELEMENTS.add("table");
        HTML_BLOCK_ELEMENTS.add("ul");

        // The following elements may also be considered block-level elements since they may contain block-level elements:

        HTML_BLOCK_ELEMENTS.add("dd");
        HTML_BLOCK_ELEMENTS.add("dt");
        HTML_BLOCK_ELEMENTS.add("frameset");
        HTML_BLOCK_ELEMENTS.add("li");
        HTML_BLOCK_ELEMENTS.add("tbody");
        HTML_BLOCK_ELEMENTS.add("td");
        HTML_BLOCK_ELEMENTS.add("tfoot");
        HTML_BLOCK_ELEMENTS.add("th");
        HTML_BLOCK_ELEMENTS.add("thead");
        HTML_BLOCK_ELEMENTS.add("tr");

        // Añadimos <br> a esta lista pues encaja en nuestro objetivo
        HTML_BLOCK_ELEMENTS.add("br");

        // Listamos ahora los elementos contenedores de textos que son
        // "sensibles a espacios" en un ejemplo como éste:
        /*
           <TAG id="pruebaId"> <b>HELLO</b>
                <b>BYE</b>  </TAG>
        */
        // Es decir, cuando visualmente resulta; "HELLO BYE" (y a veces con el retorno de línea)
        // El resto de elementos, o bien no pueden contener textos, o el resultado es "HELLOBYE"
        // Esto NO quiere decir que sin los <b> los espacios no se vieran, se trata de ver si
        // los nodos de texto con espacios son inútiles ENTRE ELEMENTOS en este contexto.
        // Notar que <span> NO está en esta lista. En muchos casos tal y como <address>
        // la "culpa" la tiene el que el render no está preparado para contener más elementos (los <b>)
        // Los tags han sido probados uno a uno y la lista ha sido obtenida
        // de la clase HTMLElements del parser NekoHTML

        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("address");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("blockquote");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("button");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("caption");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("center");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("div");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("dd");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("dt");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("fieldset");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("form");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("h1");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("h2");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("h3");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("h4");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("h5");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("h6");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("layer");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("li");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("listing");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("multicol");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("p");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("plaintext");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("pre");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("td");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("th");
        HTML_ELEM_CHILD_SPACES_SENSIBLE.add("xmp");
    }


    /** Creates a new instance of JSRenderHTMLTextMSIEPocketImpl */
    public JSRenderHTMLTextMSIEPocketImpl()
    {
    }
/*
    protected String dataTextToJS(String text,ClientDocumentStfulImpl clientDoc)
    {
        // Cuando se inserta texto tal y como <b> Hola </b>
        // el parser de IE Mobile elimina TODOS los espacios al principio al final
        // (en HTML se filtran los espacios contiguos pero se deja al menos uno)
        int len = text.length();
        if (len > 0)
        {
            if (text.charAt(0) == ' ')
                text = "&nbsp;" + text.substring(1);
            if (text.charAt(text.length() - 1) == ' ')
                text = text.substring(0,text.length() - 1) + "&nbsp;";
        }

        return super.dataTextToJS(text,clientDoc);
    }
*/

    public static boolean isUnusefulTextNode(Text text,Node parent)
    {
        // La inserción de nodos de texto es MUY costosa (se usa innerHTML o similar en el padre) así evitamos insertar nodos
        // de texto inútiles funcional y visualmente y además en el cliente son filtrados.
        // Casi siempre (¿siempre?) estos nodos de texto serán espaciadores (con espacios y fines de línea)
        int type = parent.getNodeType();

        if (type != Node.ELEMENT_NODE)
        {
            if (type == Node.DOCUMENT_NODE) return true; // Bajo el documento los nodos de texto son inútiles
            return false; // por si acaso (no se me ocurre un caso)
        }

        Element elemParent = (Element)parent;
        if ((elemParent instanceof HTMLElement) &&
            HTMLTemplateVersionDelegateImpl.HTML_ELEMS_NOT_USE_CHILD_TEXT.contains(elemParent.getLocalName()))
            return true;

        if (!DOMUtilInternal.isSeparator(text)) return false; // Contiene algo más que espacios

        // Está formado por espacios, tabs y/o retornos:

        // Vemos si está en medio de elementos tipo "block" pues entonces no tiene influencia visual
        // por lo que lo mejor es filtrar.
        // Tiene que haber al menos un elemento tipo block a los lados pues es posible
        // que los espacios sean renderizados como un espacio si es lo único que tiene el padre
        // Nodos de texto contiguos y/o comentarios no tienen influencia visual
        // en *este* nodo por lo tanto buscamos los elementos cercanos.
        Element prev = ItsNatTreeWalker.getPreviousSiblingElement(text);
        Element next = ItsNatTreeWalker.getNextSiblingElement(text);
        if ((prev == null) && (next == null)) return false; // No hay elementos a los lados, aunque sean espacios puede tener influencia visual (un espacio)

        if ((prev != null) || (next != null))
        {
            if ((prev != null) && (prev instanceof HTMLElement) &&
                HTML_BLOCK_ELEMENTS.contains(prev.getLocalName()))
                return true;

            if ((next != null) && (next instanceof HTMLElement) &&
                HTML_BLOCK_ELEMENTS.contains(next.getLocalName()))
                return true;
        }

        // Hay algún elemento en los lados del texto con espacios y no es de tipo bloque.
        // Vemos si de acuerdo al tag del padre podemos ignorar el nodo de texto, pues
        // al existir algún elemento al lado (ej. <b> etc) se ignoran los espacios.
        // Ver notas en la definición de HTML_ELEM_CHILD_SPACES_SENSIBLE
        if ((elemParent instanceof HTMLElement) &&
             HTML_ELEM_CHILD_SPACES_SENSIBLE.contains(elemParent.getLocalName()))
        {
            // El elemento padre es sensible a los espacios, por ejemplo:
            // <div><b>Hello</b> <b>Bye</b></div>
            // en este caso el espacio entre medias se ve.
            // Sin embargo un posible espacio (o fin de línea) al ppio o al final NO se ve:
            // <div> <b>Hello</b> </div> es igual que <div><b>Hello</b></div>
            // Por lo que ignoraremos el nodo de texto si NO está entre DOS elementos
            if ((prev == null) || (next == null))
                return true; // Está al ppio o en el final, no se manifiesta visualmente

            return false; // Está entre dos elementos no bloque con un padre sensible a espacios, se manifiesta
        }
        
        return false;
    }

    protected String getAppendCompleteChildNode(Node parent,Node newNode,String parentVarName,ClientDocumentStfulImpl clientDoc)
    {
        Text newTextNode = (Text)newNode;
        if (isUnusefulTextNode(newTextNode,parent))
            return "";

        String jsValue = dataTextToJS(newTextNode,clientDoc);
        if (parent instanceof HTMLOptionElement)
            return setHTMLOptionText(parentVarName,jsValue);
        else
            return JSRenderHTMLFilteredNodeMSIEPocketImpl.getAppendFilteredNodeCode(parentVarName,jsValue);
    }

    public String getInsertCompleteNodeCode(Node newNode,ClientDocumentStfulImpl clientDoc)
    {
        Node parent = newNode.getParentNode();
        Text newTextNode = (Text)newNode;
        if (isUnusefulTextNode(newTextNode,parent))
            return "";

        String jsValue = dataTextToJS(newTextNode,clientDoc);
        if (parent instanceof HTMLOptionElement)
            return setHTMLOptionText((HTMLOptionElement)parent,jsValue,clientDoc);
        else
            return JSRenderHTMLFilteredNodeMSIEPocketImpl.getInsertFilteredNodeCode(newTextNode,jsValue,clientDoc);
    }

    public String getRemoveNodeCode(Node removedNode,ClientDocumentStfulImpl clientDoc)
    {
        Text removedTextNode = (Text)removedNode;
        Node parent = removedNode.getParentNode();
        if (isUnusefulTextNode(removedTextNode,parent))
            return "";  // No merece la pena eliminarlo pues será un espacio o fin de línea que seguramente no se envió al cliente

        String jsValue = dataTextToJS("",clientDoc);
        if (parent instanceof HTMLOptionElement)
            return setHTMLOptionText((HTMLOptionElement)parent,jsValue,clientDoc);
        else
            return JSRenderHTMLFilteredNodeMSIEPocketImpl.getModifiedFilteredNodeCode(removedTextNode,jsValue,clientDoc);
    }

    public String getCharacterDataModifiedCode(CharacterData node,ClientDocumentStfulImpl clientDoc)
    {
        // Redefinimos totalmente pues la gestión es muy diferente
        Text textNode = (Text)node;
        String jsValue = dataTextToJS(textNode.getData(),clientDoc);
        Node parent = textNode.getParentNode();
        if (parent instanceof HTMLOptionElement)
            return setHTMLOptionText((HTMLOptionElement)parent,jsValue,clientDoc);
        else
            return JSRenderHTMLFilteredNodeMSIEPocketImpl.getModifiedFilteredNodeCode(textNode,jsValue,clientDoc);
    }

    private String setHTMLOptionText(HTMLOptionElement parent,String jsValue,ClientDocumentStfulImpl clientDoc)
    {
        // Evitamos usar el nombre "parent" porque es una propiedad de "window" que suele ser el contexto
        StringBuffer code = new StringBuffer();
        code.append( "var parentNode = " + clientDoc.getNodeReference(parent,true,true) + ";\n" );
        code.append( setHTMLOptionText("parentNode",jsValue) );
        return code.toString();
    }

    private String setHTMLOptionText(String parentVarName,String jsValue)
    {
        return parentVarName + ".text = " + jsValue + ";\n";
    }
}
