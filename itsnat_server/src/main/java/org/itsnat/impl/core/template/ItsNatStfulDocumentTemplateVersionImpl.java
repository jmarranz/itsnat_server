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

package org.itsnat.impl.core.template;

import org.itsnat.impl.core.template.droid.ItsNatStfulDroidDocumentTemplateVersionImpl;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.markup.parse.XercesDOMParserWrapperImpl;
import org.itsnat.impl.core.template.web.html.ItsNatHTMLDocumentTemplateVersionImpl;
import org.itsnat.impl.core.template.web.otherns.ItsNatOtherNSDocumentTemplateVersionImpl;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatStfulDocumentTemplateVersionImpl extends ItsNatDocumentTemplateVersionImpl
{
    protected MarkupSourceStringMarkupImpl source; // Sólo es no nulo si es attached server y si es necesario salvar

    /** Creates a new instance of ItsNatStfulDocumentTemplateVersionImpl */
    public ItsNatStfulDocumentTemplateVersionImpl(ItsNatStfulDocumentTemplateImpl docTemplate,InputSource source,long timeStamp,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        super(docTemplate,source,timeStamp,request,response);
    }

    public static ItsNatStfulDocumentTemplateVersionImpl createItsNatStfulDocumentTemplateVersion(ItsNatStfulDocumentTemplateImpl docTemplate,InputSource source,long timeStamp,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        if (docTemplate.isMIME_HTML_or_XHTML())
            return new ItsNatHTMLDocumentTemplateVersionImpl(docTemplate,source,timeStamp,request,response);
        else if (docTemplate.isMIME_OTHERNS())
            return ItsNatOtherNSDocumentTemplateVersionImpl.createItsNatOtherNSDocumentTemplateVersion(docTemplate, source, timeStamp,request,response);
        else if (docTemplate.isMIME_ANDROID())
            return new ItsNatStfulDroidDocumentTemplateVersionImpl(docTemplate,source,timeStamp,request,response);        
        else
            return null; // No ocurre nunca.
    }

    public MarkupSourceStringMarkupImpl getMarkupSourceStringMarkup()
    {
        return source;
    }

    public void setMarkupSourceStringMarkup(MarkupSourceStringMarkupImpl source)
    {
        this.source = source;
    }

    public abstract String wrapBodyAsDocument(String source);

    @Override
    public DocumentFragment parseFragmentToDocFragment(String fragCode,ItsNatDocumentImpl itsNatDoc)
    {
        // En un futuro podría plantearse el cacheado

        String docCode = wrapBodyAsDocument(fragCode);

        XercesDOMParserWrapperImpl parser = itsNatDoc.getMarkupParser();
        Document doc = parseDocumentOrFragment(docCode,parser,true);
        templateDelegate.normalizeDocument(doc);

        Element body = getBodyParentElement(doc);
        Document docTarget = itsNatDoc.getDocument();
        return importTreeAsDocFragment(body,docTarget);
    }

    public abstract Element getBodyParentElement(Document doc);

    protected DocumentFragment importTreeAsDocFragment(Element root,Document docTarget)
    {
        Document doc = root.getOwnerDocument();
        DocumentFragment fragment = doc.createDocumentFragment();
        Node child = root.getFirstChild();
        while(child != null)
        {
            fragment.appendChild(child); // Al insertar se quita del documento
            child = root.getFirstChild();
        }

        fragment = (DocumentFragment)docTarget.importNode(fragment,true);
        return fragment;
    }

}
