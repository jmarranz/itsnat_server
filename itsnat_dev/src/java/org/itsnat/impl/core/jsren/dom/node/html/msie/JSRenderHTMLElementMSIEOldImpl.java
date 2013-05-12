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

import org.itsnat.core.html.ItsNatHTMLEmbedElement;
import org.itsnat.impl.core.browser.BrowserMSIEOld;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.jsren.dom.node.html.JSRenderHTMLElementImpl;
import org.itsnat.impl.core.template.MarkupTemplateVersionImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLObjectElement;
import org.w3c.dom.html.HTMLSelectElement;

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLElementMSIEOldImpl extends JSRenderHTMLElementImpl
{
    public static final JSRenderHTMLElementMSIEOldImpl SINGLETON = new JSRenderHTMLElementMSIEOldImpl();
    
    /** Creates a new instance of JSRenderHTMLElementMSIEOldImpl */
    public JSRenderHTMLElementMSIEOldImpl()
    {
        // Internet Explorer (MSIE 6+) :
        // http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/innerhtml.asp
        // COL, COLGROUP, FRAMESET, HTML, STYLE, TABLE, TBODY, TFOOT, THEAD, TITLE, TR
        // tienen innerHTML como "readonly"
        // SCRIPT no está en la lista sin embargo no admite innerHTML para meter el texto del script
        // http://www.justatheory.com/computers/programming/javascript/ie_dom_help.html
        // porque en teoría no admite nodos hijos  (canHaveChildren es false)
        // hay que usar en ese caso la propiedad "text", evitamos usar innerHTML en este caso
        // y abordamos el problema en el appendChild
        // En teoría SELECT no está en la lista sin embargo no funciona bien
        // el innerHTML (http://support.microsoft.com/kb/276228) por ejemplo:
        // select.innerHTML="<option>Option 1</option>" además de filtrar el <option>
        // deja el </option> como nodo propio.

        tagNamesWithoutInnerHTML.add("table");
        tagNamesWithoutInnerHTML.add("tbody");
        tagNamesWithoutInnerHTML.add("tfoot");
        tagNamesWithoutInnerHTML.add("thead");
        tagNamesWithoutInnerHTML.add("tr");

        tagNamesWithoutInnerHTML.add("col");
        tagNamesWithoutInnerHTML.add("colgroup");
        tagNamesWithoutInnerHTML.add("frameset");
        tagNamesWithoutInnerHTML.add("html");
        tagNamesWithoutInnerHTML.add("style");
        tagNamesWithoutInnerHTML.add("title");
        tagNamesWithoutInnerHTML.add("script");
        tagNamesWithoutInnerHTML.add("select");
        tagNamesWithoutInnerHTML.add("pre");
        tagNamesWithoutInnerHTML.add("textarea");

        tagNamesWithoutInnerHTML.add("iframe"); // Esta la descubrí yo, no admite siquiera un ""
        tagNamesWithoutInnerHTML.add("img"); // Idem
        tagNamesWithoutInnerHTML.add("input"); // Idem
        
        tagNamesWithoutInnerHTML.add("object"); // Esta la descubrí yo
        tagNamesWithoutInnerHTML.add("applet"); // Esta la descubrí yo
        
        // Los casos <pre> y <textarea> son casos especiales, admiten innerHTML
        // pero Internet Explorer filtra erróneamente los fines de línea de un texto,
        // en el contenido textual (nodo texto) de un <textarea> o <pre>, en estos son fundamentales los fines de línea
        // http://www.quirksmode.org/bugreports/archives/2004/11/innerhtml_and_t.html
        // Podríamos considerar no usar innerHTML cuando sólo hay un nodo texto hijo pues no merece la pena usar innerHTML en este caso,
        // pero cuando un subárbol se cachea se convierte en un nodo texto como único hijo
        // del padre, este nodo texto es falso y ha de insertarse via innerHTML para insertar
        // el contenido cacheado. Por eso evitamos usar innerHTML en <pre> y <textarea>
        // y de esa manera se evita cachear dichos elementos en los que no podemos usar innerHTML.
        // Curiosamente innerHTML funciona bien con un <textarea> o <pre> dentro de una cadena insertada via innerHTML.

        //---------------------------------------------------
        // TagNames no válidos DENTRO de un innerHTML
        //---------------------------------------------------

        /* En MSIE el <script> NO es insertado via innerHTML seguramente por razones de seguridad
           es decir no se procesa el elemento.
           Ejemplo:
           var prueba = document.getElementById("prueba");
           prueba.innerHTML = "<div><script>alert('Hola');</" + "script></div>";
           Un alert(prueba.innerHTML) devolverá "<div></div>".
           Nota: el truco "</" + "script>" es para evitar un cerrado erróneo del parser pues busca el </script> del elemento. O bien <\/script>
           Sin embargo via DOM funciona correctamente.
       */
        tagNamesNotValidInsideInnerHTML.add("script");

        // Con <style> dentro de innerHTML pasa lo mismo, MSIE lo filtra.
        tagNamesNotValidInsideInnerHTML.add("style");
    }

    public static JSRenderHTMLElementMSIEOldImpl getJSMSIEHTMLElementRender(BrowserMSIEOld browser)
    {
        return JSRenderHTMLElementMSIEOldImpl.SINGLETON;
    }

    protected boolean isChildNotValidInsideInnerHTMLElementNotHTML(Element elem,MarkupTemplateVersionImpl template)
    {
        // En el caso de MSIE el caso de estudio no es con MIME "application/xhtml+xml" pues no está soportado.
        // Hay dos usos típico de uso de elementos no HTML en X/HTML (por supuesto como MIME text/html):
        // 1) SVG inline renderizado por Adobe SVG Viewer, y el ASV renderiza
        // correctamente elementos SVG dentro de innerHTML. Ya se encarga ItsNat
        // en otro lugar de añadir las declaraciones de namespaces necesarias
        // antes de la inserción.
        // 2) SVG inline renderizado por SVGWeb: en este caso el nodo SVG raíz del subárbol hay que insertarlo de forma
        //    especial.
        // Por prudencia generalizamos a todos los casos con elementos no XHTML

        return true; // Por SVGWeb
    }

    protected boolean isChildNotValidInsideInnerHTMLNotElementOrText(Node node)
    {
        // Devuelve true si el nodo dado no debe serializarse dentro de una cadena innerHTML

        int type = node.getNodeType();
        if (type == Node.COMMENT_NODE)
        {
            // El MSIE elimina los comentarios al procesar el texto del innerHTML
            return true;
        }

        // A pesar de que MSIE no procesa bien los <![CDATA[ ]]> (caso Node.CDATA_SECTION_NODE)
        // en un innerHTML, el Document.createCDataSection no está definido en HTML
        // por lo que lo tragamos como nodo válido dentro de un innerHTML porque
        // de otra manera habrá error en el cliente al generar una llamada createCDataSection

        return false;
    }

    public String getCurrentStyleObject(String itsNatDocVar,String elemName,ClientDocumentStfulImpl clientDoc)
    {
        return elemName + ".currentStyle";
    }

    private StringBuilder addSpecialAttrIfDefined(Element elem,String attrName,StringBuilder spAttribs)
    {
        String value = elem.getAttribute(attrName);
        if (value.length() == 0) return spAttribs;

        if (spAttribs == null) spAttribs = new StringBuilder();
        spAttribs.append( attrName + "='" + value + "' " );
        return spAttribs;
    }

    protected String createElement(Element elem,String tagName,ClientDocumentStfulImpl clientDoc)
    {
        // Es RARISIMO que un elemento X/HTML tenga un prefijo en un
        // documento X/HTML (porque MSIE no admite más tipos de documentos con estado)
        // pero por si acaso tomamos el localName porque createElement en JavaScript en MSIE no tolera los prefijos en el tag.
        // Ver notas en JSRenderOtherNSElementMSIEmpl

        tagName = elem.getLocalName(); 

        // Ahora añadimos los atributos especiales si hay
        StringBuilder spAttribs = null;

        spAttribs = addSpecialAttrIfDefined(elem,"name",spAttribs);

        // Esta chapuza está documentada en:
        // http://www.thunderguy.com/semicolon/2005/05/23/setting-the-name-attribute-in-internet-explorer
        // http://msdn.microsoft.com/library/default.asp?url=/workshop/author/dhtml/reference/properties/name_2.asp
        // El atributo en sí puede ponerse con setAttribute()
        // el problema es que MSIE no se entera si "name" tiene significado como por ejemplo en un anchor
        // (el anchor tiene el atributo pero no funciona como anchor)

        if (elem instanceof HTMLSelectElement)
        {
            // http://bytes.com/groups/javascript/435979-creating-select-multiple-via-dom-methods
            spAttribs = addSpecialAttrIfDefined(elem,"multiple",spAttribs);
            spAttribs = addSpecialAttrIfDefined(elem,"size",spAttribs);
        }
        else if ((elem instanceof HTMLObjectElement) ||
                 (elem instanceof ItsNatHTMLEmbedElement))
        {
            // Esto lo he descubierto yo. No es que "type" haya de añadirse
            // en el createElement sino que debe de ser añadido cuanto
            // antes, antes del atributo "src" por ejemplo y también "data"
            // en el caso de MSIE v8.
            spAttribs = addSpecialAttrIfDefined(elem,"type",spAttribs);
        }

        // El caso de "type" de input (ej. <input type="...">) 
        // no es necesario meterlo en createElement, pero sí inmediatamente después
        // de crear el objeto, pues si es añadido antes al Document, MSIE lo define como <input type="text"> (el tipo por defecto)
        // el problema es que no puede cambiarse después via JavaScript al algo diferente
        // (file, submit etc) dando error o ignorándose.
        // Esto también se aplica a select si queremos cambiar de multiple
        // a no multiple y viceversa.

        if (spAttribs != null)
            tagName = "<" + tagName + " " + spAttribs + ">";

        return super.createElement(elem,tagName,clientDoc);
    }
    
}
