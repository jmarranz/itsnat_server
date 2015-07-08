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

package org.itsnat.impl.core.template.web.otherns;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.impl.core.template.MarkupTemplateVersionDelegateImpl;
import org.itsnat.impl.core.MarkupContainerImpl;
import org.itsnat.impl.core.template.web.ItsNatWebOnlyDocFragmentTemplateVersionImpl;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLBodyElement;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.InputSource;

/**
 *
 * @author jmarranz
 */
public class ItsNatOtherNSDocFragmentTemplateVersionImpl extends ItsNatWebOnlyDocFragmentTemplateVersionImpl
{
    /**
     * Creates a new instance of ItsNatOtherNSDocFragmentTemplateVersionImpl
     */
    public ItsNatOtherNSDocFragmentTemplateVersionImpl(ItsNatOtherNSDocFragmentTemplateImpl docTemplate,InputSource source,long timeStamp,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        super(docTemplate,source,timeStamp,request,response);

        // Hay que recordar que tras el crear el DocumentFragment el root queda vacío.
        this.templateDoc = null; // Para que no se vuelva a usar y para salvar memoria
    }

    public Element getContainerElement()
    {
        return getDocument().getDocumentElement();       
    }
        
    protected MarkupTemplateVersionDelegateImpl createMarkupTemplateVersionDelegate()
    {
        return new OtherNSTemplateVersionDelegateImpl(this);
    }

}
