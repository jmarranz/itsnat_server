/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2014 Jose Maria Arranz Santamaria, Spanish citizen

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

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.doc.droid.ItsNatDroidDocumentImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.template.html.HTMLTemplateVersionDelegateImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/**
 *
 * @author jmarranz
 */
public class ItsNatStfulDroidDocumentTemplateVersionImpl extends ItsNatStfulDocumentTemplateVersionImpl
{
    public ItsNatStfulDroidDocumentTemplateVersionImpl(ItsNatStfulDocumentTemplateImpl docTemplate, InputSource source, long timeStamp, ItsNatServletRequest request, ItsNatServletResponse response)
    {
        super(docTemplate, source, timeStamp, request, response);
    }

    @Override
    public String wrapBodyAsDocument(String source)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override    
    public Element getBodyParentElement(Document doc)
    {
        return doc.getDocumentElement(); 
    }
    
    @Override
    protected ItsNatDocumentImpl createItsNatDocument(Document doc, Browser browser, String requestURL, ItsNatSessionImpl session, boolean stateless)
    {
        return new ItsNatDroidDocumentImpl(doc,this,browser,requestURL,session,stateless);
    }

    @Override
    protected MarkupTemplateVersionDelegateImpl createMarkupTemplateVersionDelegate()
    {
        return new StfulDroidTemplateVersionDelegateImpl(this);        
    }
    
}
