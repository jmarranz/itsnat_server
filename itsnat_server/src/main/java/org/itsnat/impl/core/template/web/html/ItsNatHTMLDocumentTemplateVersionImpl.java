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
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.doc.web.ItsNatHTMLDocumentImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.template.ItsNatStfulDocumentTemplateImpl;
import org.itsnat.impl.core.template.web.ItsNatStfulWebDocumentTemplateVersionImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.InputSource;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLDocumentTemplateVersionImpl extends ItsNatStfulWebDocumentTemplateVersionImpl
{
    /**
     * Creates a new instance of ItsNatHTMLDocumentTemplateVersionImpl
     */
    public ItsNatHTMLDocumentTemplateVersionImpl(ItsNatStfulDocumentTemplateImpl docTemplate,InputSource source,long timeStamp,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        super(docTemplate,source,timeStamp,request,response);
    }

    public HTMLTemplateVersionDelegateImpl getHTMLTemplateVersionDelegate()
    {
        return (HTMLTemplateVersionDelegateImpl)templateDelegate;
    }

    public HTMLDocument getHTMLDocument()
    {
        return (HTMLDocument)templateDoc;
    }

    @Override
    protected ItsNatDocumentImpl createItsNatDocument(Document doc,Browser browser,String requestURL,ItsNatSessionImpl session,boolean stateless)
    {
        return new ItsNatHTMLDocumentImpl((HTMLDocument)doc,this,browser,requestURL,session,stateless);
    }
    
    @Override
    protected MarkupTemplateVersionDelegateImpl createMarkupTemplateVersionDelegate()
    {
        return new HTMLTemplateVersionDelegateImpl(this);
    }

    @Override
    protected boolean isElementValidForCaching(Element elem)
    {
        if (!super.isElementValidForCaching(elem))
            return false;

        if (elem == getDocument().getDocumentElement())
        {
            // No permitimos cachear el contenido directo del elemento <html>
            // porque daríamos lugar a un documento HTML mal formado
            // y hacemos algunos chequeos que necesitan la existencia
            // de <head> y <body> (por ejemplo la existencia de SVGWeb etc)

            return false;
        }

        if (getHTMLTemplateVersionDelegate().isSVGWebMetaDeclaration(elem))
        {
            // No cacheamos este meta porque en tiempo de carga en el servidor
            // necesitamos chequear que se está usando el SVGWeb
            return false;
        }

        return true;
    }

    public String wrapBodyAsDocument(String source)
    {
        // Es curioso porque "source" puede contener elementos propios
        // del <head> como <title> <link> etc y el parser de Xerces se lo traga
        // y los elementos HTMLElement son del tipo adecuado así que
        // no hacemos métodos específicos para parsear elementos destinados
        // al <head>.

        String encoding = getEncoding();
        boolean isxhtml = isMIME_XHTML();

        StringBuilder code = new StringBuilder();
        code.append( "<html" );
        if (isxhtml)
            code.append(" xmlns=\"" + NamespaceUtil.XHTML_NAMESPACE + "\"");
        code.append( "><head>" );
        code.append( "<meta content=\"" + getMIME() + "; charset=" + encoding + "\" http-equiv=\"Content-Type\" />" );
        code.append( "</head><body>" );
        code.append( source );
        code.append( "</body></html>" );

        return code.toString();
    }

    public Element getBodyParentElement(Document doc)
    {
        return ((HTMLDocument)doc).getBody();
    }
}
