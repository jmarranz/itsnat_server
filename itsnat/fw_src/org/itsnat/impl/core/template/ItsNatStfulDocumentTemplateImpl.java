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

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.impl.core.servlet.ItsNatServletImpl;
import org.xml.sax.InputSource;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatStfulDocumentTemplateImpl extends ItsNatDocumentTemplateImpl
{

    /** Creates a new instance of ItsNatStfulDocumentTemplateImpl */
    public ItsNatStfulDocumentTemplateImpl(String name,String mime,MarkupSourceImpl source,ItsNatServletImpl servlet)
    {
        super(name,mime,source,servlet);
    }

    public MarkupTemplateVersionImpl createMarkupTemplateVersion(InputSource source,long timeStamp,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        return ItsNatStfulDocumentTemplateVersionImpl.createItsNatStfulDocumentTemplateVersion(this, source, timeStamp,request,response);
    }
}
