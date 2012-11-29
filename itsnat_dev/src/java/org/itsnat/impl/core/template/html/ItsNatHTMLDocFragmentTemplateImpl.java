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

package org.itsnat.impl.core.template.html;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.tmpl.ItsNatHTMLDocFragmentTemplate;
import org.itsnat.impl.core.template.ItsNatDocFragmentTemplateImpl;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.servlet.ItsNatServletImpl;
import org.itsnat.impl.core.template.MarkupSourceImpl;
import org.itsnat.impl.core.template.MarkupTemplateVersionImpl;
import org.w3c.dom.DocumentFragment;
import org.xml.sax.InputSource;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLDocFragmentTemplateImpl extends ItsNatDocFragmentTemplateImpl implements ItsNatHTMLDocFragmentTemplate
{

    /**
     * Creates a new instance of ItsNatHTMLDocFragmentTemplateImpl
     */
    public ItsNatHTMLDocFragmentTemplateImpl(String name,String mime,MarkupSourceImpl source,ItsNatServletImpl servlet)
    {
        super(name,mime,source,servlet);
    }

    public MarkupTemplateVersionImpl createMarkupTemplateVersion(InputSource source,long timeStamp,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        return new ItsNatHTMLDocFragmentTemplateVersionImpl(this,source,timeStamp,request,response);
    }

    public ItsNatHTMLDocFragmentTemplateVersionImpl getNewestItsNatHTMLDocFragmentTemplateVersion(ItsNatDocumentImpl itsNatDocTarget,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        return (ItsNatHTMLDocFragmentTemplateVersionImpl)getNewestItsNatDocFragmentTemplateVersion(itsNatDocTarget,request,response);
    }

    public DocumentFragment loadDocumentFragmentHead(ItsNatDocument docTarget)
    {
        return loadDocumentFragmentHead(docTarget,null,null);
    }

    public DocumentFragment loadDocumentFragmentHead(ItsNatDocument docTarget,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        ItsNatDocumentImpl itsNatDocTarget = (ItsNatDocumentImpl)docTarget;
        return getNewestItsNatHTMLDocFragmentTemplateVersion(itsNatDocTarget,request,response).loadDocumentFragmentHead(itsNatDocTarget);
    }

    public DocumentFragment loadDocumentFragmentBody(ItsNatDocument docTarget)
    {
        return loadDocumentFragmentBody(docTarget,null,null);
    }

    public DocumentFragment loadDocumentFragmentBody(ItsNatDocument docTarget,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        ItsNatDocumentImpl itsNatDocTarget = (ItsNatDocumentImpl)docTarget;
        return getNewestItsNatHTMLDocFragmentTemplateVersion(itsNatDocTarget,request,response).loadDocumentFragmentBody(itsNatDocTarget);
    }
}
