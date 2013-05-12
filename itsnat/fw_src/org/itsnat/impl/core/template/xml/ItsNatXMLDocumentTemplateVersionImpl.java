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

package org.itsnat.impl.core.template.xml;

import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.impl.core.template.MarkupTemplateVersionDelegateImpl;
import org.itsnat.impl.core.template.ItsNatDocumentTemplateVersionImpl;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.doc.ItsNatXMLDocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.xml.sax.InputSource;

/**
 *
 * @author jmarranz
 */
public class ItsNatXMLDocumentTemplateVersionImpl extends ItsNatDocumentTemplateVersionImpl
{

    /**
     * Creates a new instance of ItsNatXMLDocumentTemplateVersionImpl
     */
    public ItsNatXMLDocumentTemplateVersionImpl(ItsNatXMLDocumentTemplateImpl docTemplate,InputSource source,long timeStamp,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        super(docTemplate,source,timeStamp,request,response);
    }

    @Override
    protected ItsNatDocumentImpl createItsNatDocument(Document doc,Browser browser,String requestURL,ItsNatSessionImpl session,boolean stateless)
    {
        // stateless NUNCA será true pero por homogeneidad
        return new ItsNatXMLDocumentImpl(doc,this,browser,requestURL,session,stateless);
    }

    protected MarkupTemplateVersionDelegateImpl createMarkupTemplateVersionDelegate()
    {
        return new XMLTemplateVersionDelegateImpl(this);
    }

    public DocumentFragment parseFragmentToDocFragment(String source,ItsNatDocumentImpl docTarget)
    {
        // No está implementado porque sólo se usa en el caso de mutation events recibidos
        // del navegador y a día de hoy ésto sólo ocurre con documentos HTML y SVG
        throw new ItsNatException("Unexpected error");
    }
}
