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

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.tmpl.TemplateSource;
import org.xml.sax.InputSource;

/**
 *
 * @author jmarranz
 */
public class TemplateSourceWrapperImpl extends MarkupSourceImpl
{
    protected TemplateSource templateSource;

    public TemplateSourceWrapperImpl(TemplateSource templateSource)
    {
        this.templateSource = templateSource;
    }

    public long getCurrentTimestamp(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        // El timestamp es irrelevante en este tipo de source
        return 0;
    }

    public boolean isMustReload(long currentTimestamp,ItsNatServletRequest request, ItsNatServletResponse response)
    {
        // El timestamp es irrelevante en este tipo de source
        return templateSource.isMustReload(request, response);
    }

    public InputSource createInputSource(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        InputStream stream = templateSource.getInputStream(request, response);
        return new InputSource(stream);
    }

    public Object getSource()
    {
        return templateSource;
    }
}
