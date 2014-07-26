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

package org.itsnat.impl.core.template.web.html;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.impl.core.template.MarkupTemplateVersionDelegateImpl;
import org.itsnat.impl.core.MarkupContainerImpl;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.itsnat.impl.core.template.web.ItsNatWebDocFragmentTemplateVersionImpl;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLBodyElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLHeadElement;
import org.xml.sax.InputSource;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLDocFragmentTemplateVersionImpl extends ItsNatWebDocFragmentTemplateVersionImpl
{
    protected DocumentFragment templateDocFragmentHead;
    protected DocumentFragment templateDocFragmentBody;

    /**
     * Creates a new instance of ItsNatHTMLDocFragmentTemplateVersionImpl
     */
    public ItsNatHTMLDocFragmentTemplateVersionImpl(ItsNatHTMLDocFragmentTemplateImpl docTemplate,InputSource source,long timeStamp,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        super(docTemplate,source,timeStamp,request,response);

        HTMLDocument templateHtmlDoc = (HTMLDocument)getDocument();

        HTMLHeadElement head = DOMUtilHTML.getHTMLHead(templateHtmlDoc);
        HTMLBodyElement body = (HTMLBodyElement)templateHtmlDoc.getBody();

        // Tenemos la seguridad de que hay <head> y <body> pues el parser normaliza siempre el HTML incluyéndolos
        // y el <html> NO se cachea.
        // Hay que recordar que tras el crear el DocumentFragment el <head> y el <body> quedan vacíos.
        this.templateDocFragmentHead = extractChildrenToDocFragment(head);
        this.templateDocFragmentBody = extractChildrenToDocFragment(body);

        this.templateDoc = null; // Para evitar que se use
    }

    public HTMLTemplateVersionDelegateImpl getHTMLTemplateVersionDelegate()
    {
        return (HTMLTemplateVersionDelegateImpl)templateDelegate;
    }

    @Override
    protected boolean isElementValidForCaching(Element elem)
    {
        if (!super.isElementValidForCaching(elem))
            return false;

        // No permitimos cachear el contenido directo de los propios nodos <head>
        // y <body>, pues al obtener los DocumentFragment del <head> y <body> del template
        // estos guardarían un simple nodo de texto en donde el padre (el <head>
        // o el <body>) se ha perdido, y en el cacheado es importante que el
        // elemento contenedor sea el padre verdadero del contenido cacheado.

        if ((elem instanceof HTMLHeadElement)||(elem instanceof HTMLBodyElement))
            return false;

        if (getHTMLTemplateVersionDelegate().isSVGWebMetaDeclaration(elem))
        {
            // No cacheamos este meta porque en tiempo de carga en el servidor
            // necesitamos chequear que se está usando el SVGWeb
            return false;
        }
        
        return true; 
    }

    public DocumentFragment loadDocumentFragmentHead(MarkupContainerImpl target)
    {
        return loadDocumentFragment(templateDocFragmentHead,target);
    }

    public DocumentFragment loadDocumentFragmentBody(MarkupContainerImpl target)
    {
        return loadDocumentFragment(templateDocFragmentBody,target);
    }

    public DocumentFragment loadDocumentFragment(MarkupContainerImpl target)
    {
        return loadDocumentFragmentBody(target);
    }

    public DocumentFragment loadDocumentFragmentByIncludeTag(MarkupContainerImpl target,Element includeElem)
    {
        if (DOMUtilHTML.isChildOfHTMLHead(includeElem)) // El elemento a substituir está en el <head>
            return loadDocumentFragmentHead(target);
        else // Está en el <body> (DEBE de estar ahí) o bien es un namespace no X/HTML en el que insertar elementos de <head> no suele tener sentido
            return loadDocumentFragmentBody(target);
    }
    
    protected MarkupTemplateVersionDelegateImpl createMarkupTemplateVersionDelegate()
    {
        return new HTMLTemplateVersionDelegateImpl(this);
    }

    @Override
    public void cleanDOMPattern()
    {
        super.cleanDOMPattern();

        this.templateDocFragmentHead = null;
        this.templateDocFragmentBody = null;
    }
}
