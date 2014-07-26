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
import org.itsnat.impl.core.servlet.ItsNatServletImpl;
import org.itsnat.impl.core.template.web.ItsNatWebDocFragmentTemplateImpl;
import org.itsnat.impl.core.template.MarkupSourceImpl;
import org.itsnat.impl.core.template.MarkupTemplateVersionImpl;
import org.xml.sax.InputSource;

/**
 *
 * @author jmarranz
 */
public class ItsNatOtherNSDocFragmentTemplateImpl extends ItsNatWebDocFragmentTemplateImpl
{
    /**
     * Creates a new instance of ItsNatOtherNSDocFragmentTemplateImpl
     */
    public ItsNatOtherNSDocFragmentTemplateImpl(String name,String mime,MarkupSourceImpl source,ItsNatServletImpl servlet)
    {
        super(name,mime,source,servlet);
    }

    public MarkupTemplateVersionImpl createMarkupTemplateVersion(InputSource source,long timeStamp,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        return new ItsNatOtherNSDocFragmentTemplateVersionImpl(this,source,timeStamp,request,response);
    }

}
