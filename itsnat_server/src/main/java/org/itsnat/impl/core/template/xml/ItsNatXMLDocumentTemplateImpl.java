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
import org.itsnat.impl.core.servlet.ItsNatServletImpl;
import org.itsnat.impl.core.template.MarkupTemplateVersionImpl;
import org.itsnat.impl.core.template.ItsNatDocumentTemplateImpl;
import org.itsnat.impl.core.template.MarkupSourceImpl;
import org.itsnat.impl.core.template.MarkupTemplateDelegateImpl;
import org.itsnat.impl.core.template.MarkupTemplateNormalDelegateImpl;
import org.xml.sax.InputSource;

/**
 *
 * @author jmarranz
 */
public class ItsNatXMLDocumentTemplateImpl extends ItsNatDocumentTemplateImpl
{

    /**
     * Creates a new instance of ItsNatXMLDocumentTemplateImpl
     */
    public ItsNatXMLDocumentTemplateImpl(String name,String mime,MarkupSourceImpl source,ItsNatServletImpl servlet)
    {
        super(name,mime,source,servlet);

        this.referrerEnabled = false; // Pues sólo puede funcionar con eventos
        this.eventsEnabled = false;
    }
    
    public MarkupTemplateVersionImpl createMarkupTemplateVersion(InputSource source,long timeStamp,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        return new ItsNatXMLDocumentTemplateVersionImpl(this,source,timeStamp,request,response);
    }

    public void setReferrerEnabled(boolean enabled)
    {
        if (enabled) throw new ItsNatException("Referrer does not work with XML documents because needs events",this);

        super.setReferrerEnabled(false);
    }

    public void setEventsEnabled(boolean enabled)
    {
        if (enabled) throw new ItsNatException("Events cannot be enabled in XML documents",this);

        super.setEventsEnabled(false);
    }    
  
    public MarkupTemplateDelegateImpl createMarkupTemplateDelegate(MarkupSourceImpl source)
    {
        return new MarkupTemplateNormalDelegateImpl(this,source);
    }
}
