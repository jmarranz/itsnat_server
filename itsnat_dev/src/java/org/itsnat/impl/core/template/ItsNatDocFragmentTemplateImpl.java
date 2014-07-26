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

import org.itsnat.impl.core.servlet.ItsNatServletImpl;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.template.html.ItsNatHTMLDocFragmentTemplateImpl;
import org.itsnat.impl.core.template.otherns.ItsNatOtherNSDocFragmentTemplateImpl;
import org.itsnat.impl.core.template.xml.ItsNatXMLDocFragmentTemplateImpl;
import org.w3c.dom.DocumentFragment;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatDocFragmentTemplateImpl extends MarkupTemplateImpl implements ItsNatDocFragmentTemplate
{

    /**
     * Creates a new instance of ItsNatDocFragmentTemplateImpl
     */
    public ItsNatDocFragmentTemplateImpl(String name,String mime,MarkupSourceImpl source,ItsNatServletImpl servlet)
    {
        super(name,mime,source,servlet);
    }

    public static ItsNatDocFragmentTemplateImpl createItsNatDocFragmentTemplate(String name,String mime,Object source,ItsNatServletImpl servlet)
    {
        MarkupSourceImpl markupSource = MarkupSourceImpl.createMarkupSource(source);
        if (NamespaceUtil.isHTMLorXHTMLMime(mime))
            return new ItsNatHTMLDocFragmentTemplateImpl(name,mime,markupSource,servlet);
        else if (NamespaceUtil.isOtherNSMime(mime))
            return new ItsNatOtherNSDocFragmentTemplateImpl(name,mime,markupSource,servlet);
        else
            return new ItsNatXMLDocFragmentTemplateImpl(name,mime,markupSource,servlet);
    }

    public boolean isDocFragment()
    {
        return true;
    }

    public MarkupTemplateNormalDelegateImpl getMarkupTemplateNormalDelegate()
    {
        return (MarkupTemplateNormalDelegateImpl)delegate;
    }

    public MarkupSourceImpl getMarkupSource()
    {
        return getMarkupTemplateNormalDelegate().getMarkupSource();
    }

    public ItsNatDocFragmentTemplateVersionImpl getNewestItsNatDocFragmentTemplateVersion(ItsNatDocumentImpl itsNatDocTarget,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        // El request y el response pueden ser null (es lo normal)
        if (request == null) request = itsNatDocTarget.getCurrentItsNatServletRequest(); // Por intentar que no sean nulos
        if (request != null) response = ((ItsNatServletRequestImpl)request).getItsNatServletResponseImpl();
        return getNewestItsNatDocFragmentTemplateVersion(request,response);
    }

    public ItsNatDocFragmentTemplateVersionImpl getNewestItsNatDocFragmentTemplateVersion(ItsNatServletRequest request,ItsNatServletResponse response)
    {
        return (ItsNatDocFragmentTemplateVersionImpl)getNewestMarkupTemplateVersion(request,response);
    }

    public DocumentFragment loadDocumentFragment(ItsNatDocument docTarget)
    {
        return loadDocumentFragment(docTarget,null,null);
    }

    public DocumentFragment loadDocumentFragment(ItsNatDocument docTarget,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        ItsNatDocumentImpl itsNatDocTarget = (ItsNatDocumentImpl)docTarget;
        return getNewestItsNatDocFragmentTemplateVersion(itsNatDocTarget,request,response).loadDocumentFragment(itsNatDocTarget);
    }

    public MarkupTemplateDelegateImpl createMarkupTemplateDelegate(MarkupSourceImpl source)
    {
        return new MarkupTemplateNormalDelegateImpl(this,source);
    }
}
