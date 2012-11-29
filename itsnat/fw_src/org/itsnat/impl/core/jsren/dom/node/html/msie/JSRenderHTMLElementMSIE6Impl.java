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
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLObjectElement;
import org.w3c.dom.html.HTMLSelectElement;

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLElementMSIE6Impl extends JSRenderHTMLElementMSIEOldImpl
{
    public static final JSRenderHTMLElementMSIE6Impl SINGLETON = new JSRenderHTMLElementMSIE6Impl();

    /**
     * Creates a new instance of JSRenderHTMLElementMSIE6Impl
     */
    public JSRenderHTMLElementMSIE6Impl()
    {

    }

    private StringBuffer addSpecialAttrIfDefined(Element elem,String attrName,StringBuffer spAttribs)
    {
        String value = elem.getAttribute(attrName);
        if (value.length() == 0) return spAttribs;

        if (spAttribs == null) spAttribs = new StringBuffer();
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
        StringBuffer spAttribs = null;

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

/*
    protected InnerMarkupCodeImpl appendChildrenCodeAsMarkup(String parentVarName,Element parentNode,String childrenCode,ClientDocumentStfulImpl clientDoc)
    {
        // El tag </embed> es convertido en un elemento desconocido.
        // http://msdn.microsoft.com/en-us/library/ms535245%28VS.85%29.aspx#
        // Curiosamente esto no ocurre en Pocket IE
        childrenCode = childrenCode.replaceAll("></embed>","/>");
        return super.appendChildrenCodeAsMarkup(parentVarName, parentNode, childrenCode, clientDoc);
    }
 *
 */
}

