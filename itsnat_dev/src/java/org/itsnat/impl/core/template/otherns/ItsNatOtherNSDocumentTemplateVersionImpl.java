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

package org.itsnat.impl.core.template.otherns;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.impl.core.template.ItsNatStfulDocumentTemplateImpl;
import org.itsnat.impl.core.template.ItsNatStfulWebDocumentTemplateVersionImpl;
import org.itsnat.impl.core.template.MarkupTemplateVersionDelegateImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatOtherNSDocumentTemplateVersionImpl extends ItsNatStfulWebDocumentTemplateVersionImpl
{
    /**
     * Creates a new instance of ItsNatOtherNSDocumentTemplateVersionImpl
     */
    public ItsNatOtherNSDocumentTemplateVersionImpl(ItsNatStfulDocumentTemplateImpl docTemplate,InputSource source,long timeStamp,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        super(docTemplate,source,timeStamp,request,response);
    }

    public static ItsNatOtherNSDocumentTemplateVersionImpl createItsNatOtherNSDocumentTemplateVersion(ItsNatStfulDocumentTemplateImpl docTemplate,InputSource source,long timeStamp,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        if (docTemplate.isMIME_SVG())
            return new ItsNatSVGDocumentTemplateVersionImpl(docTemplate,source,timeStamp,request,response);
        else if (docTemplate.isMIME_XUL())
            return new ItsNatXULDocumentTemplateVersionImpl(docTemplate,source,timeStamp,request,response);
        else
            return null; // No ocurre nunca.
    }

    protected MarkupTemplateVersionDelegateImpl createMarkupTemplateVersionDelegate()
    {
        return new OtherNSTemplateVersionDelegateImpl(this);
    }

    public Element getBodyParentElement(Document doc)
    {
        return doc.getDocumentElement(); // <svg> o <window>
    }
}
