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

import org.itsnat.core.tmpl.ItsNatDroidDocumentTemplate;
import org.itsnat.impl.core.servlet.ItsNatServletConfigImpl;
import org.itsnat.impl.core.servlet.ItsNatServletImpl;

/**
 *
 * @author jmarranz
 */
public class ItsNatStfulDocumentTemplateNormalDroidImpl extends ItsNatStfulDocumentTemplateNormalImpl implements ItsNatDroidDocumentTemplate
{
    protected int bitmapDensityReference;
    
    public ItsNatStfulDocumentTemplateNormalDroidImpl(String name, String mime, MarkupSourceImpl source, ItsNatServletImpl servlet)
    {
        super(name, mime, source, servlet);
        
        ItsNatServletConfigImpl servletConfig = servlet.getItsNatServletConfigImpl();
        this.bitmapDensityReference = servletConfig.getBitmapDensityReference();        
    }

    public int getBitmapDensityReference()
    {
        return bitmapDensityReference;
    }

    public void setBitmapDensityReference(int density)
    {
        checkIsAlreadyUsed();        
        this.bitmapDensityReference = density;
    }
    
}
